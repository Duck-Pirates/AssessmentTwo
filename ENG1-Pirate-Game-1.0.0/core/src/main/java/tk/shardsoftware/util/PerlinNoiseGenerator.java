package tk.shardsoftware.util;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Based on the Perlin Noise algorithm by Ken Perlin, specifically the method
 * described here: <a href=
 * "https://adrianb.io/2014/08/09/perlinnoise.html">https://adrianb.io/2014/08/09/perlinnoise.html</a>
 * <br>
 * You can view the original algorithm as described by Perlin here: <a href=
 * "https://mrl.cs.nyu.edu/~perlin/paper445.pdf">https://mrl.cs.nyu.edu/~perlin/paper445.pdf</a>
 *
 * @author Hector Woods
 */
public class PerlinNoiseGenerator {
	/**
	 * The number of 'layers' per sample, combined to make the overall noise
	 * function.
	 */
	public int octaves;
	/** Amplitude of our noise function, i.e how "tall" it is */
	public float amplitude;
	/** The frequency of our noise function, i.e how often we reach a peak */
	public float frequency;
	/** The frequency is adjusted by this value for each octave */
	public float lacunarity;
	/**
	 * The amplitude is adjusted by this value for each octave, i.e the higher
	 * persistence is the more effect each nth octave has
	 */
	public float persistence;

	/**
	 * How 'zoomed-in' our noise is. The higher the value, the more zoomed in we
	 * are.
	 */
	public float scale;

	private int[] permutationTable;

	/**
	 * One of four gradients as described in "Improving perlin noise"
	 * 
	 * @param permutation the permutation
	 * @return One of four diagonal directional vectors
	 */
	private Vector2 getGradient(int permutation) {
		int a = permutation % 4;
		if (a == 0) {
			return new Vector2(-1, -1);
		} else if (a == 1) {
			return new Vector2(1, -1);
		} else if (a == 2) {
			return new Vector2(-1, 1);
		} else {
			return new Vector2(1, 1);
		}
	}

	/**
	 * Set up Permutation table for choosing Pseudo-random gradients. Method for
	 * choosing one of four random gradients as described in 'Improving Perlin
	 * Noise' Generates an array of integers 1-255, twice. In generateNoiseValue we
	 * hash the values of x and y to get one of the values in this array, then in
	 * getGradient we do x mod 4 to get one of four different gradients.
	 * 
	 * @param seed the seed of the random function
	 */
	private void setUpPermutationTable(long seed) {
		int[] p = new int[512];
		// Populate array with 0-255, we do this twice to prevent overflows from our
		// hash functions.
		for (int i = 0; i < 512; i++) {
			p[i] = i % 256;
		}
		// Shuffle the array based on our seed
		Random r = new Random(seed);
		for (int i = 0; i < 512; i++) {
			// Swap a random index j with i
			int currentValue = p[i];
			int newIndex = r.nextInt(256); // random number <=256
			int newValue = p[newIndex];
			p[newIndex] = currentValue;
			p[i] = newValue;
		}
		this.permutationTable = p;
	}

	/**
	 * Linear interpolation between a1 and a2 by proportion of t, <br>
	 * e.g for lerp(t= 0.3, a1=0, a2=10) == (1/3)
	 * 
	 * @param t the ratio to interpolate by
	 * @param a1 the first value
	 * @param a2 the last value
	 * @return The linearly interpolated value
	 */
	private float lerp(float t, float a1, float a2) {
		return a1 + t * (a2 - a1);
	}

	/**
	 * Polynomial function 6t^5 - 15t^4 + 10t^3, used to ease our interpolation
	 * 
	 * @param t the value to ease
	 * @return The eased value
	 */
	private float ease(float t) {
		return ((6 * t - 15) * t + 10) * t * t * t;
	}

	/**
	 * Compute noise value -1 &#60; n &#60; 1 for point P = (x,y)
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @return The noise at the position
	 */
	private float generateNoiseValue(float x, float y) {
		// Gradient Vector points
		int X = (int) Math.floor(x); // round down
		int Y = (int) Math.floor(y);
		float xf = x - X;
		float yf = y - Y;

		// Gradient Vectors, gradients are selected based on hash functions computed on
		// the permutation table
		int[] p = this.permutationTable;
		Vector2 topRightGrad = getGradient(p[p[X + 1] + p[Y + 1]]);
		Vector2 topLeftGrad = getGradient(p[p[X] + p[Y + 1]]);
		Vector2 btmRightGrad = getGradient(p[p[X + 1] + p[Y]]);
		Vector2 btmLeftGrad = getGradient(p[p[X] + p[Y]]);

		// Distance Vectors, distance from each of the corners of the unit square for
		// point P=(x,y)
		Vector2 topRightDist = new Vector2(1 - xf, 1 - yf);
		Vector2 topLeftDist = new Vector2(xf, 1 - yf);
		Vector2 btmRightDist = new Vector2(1 - xf, yf);
		Vector2 btmLeftDist = new Vector2(xf, yf);

		// Dot products, dot product of distance vectors for each corner of unit square
		// and the gradient that corresponds to that unit square
		float dPtopRight = topRightDist.dot((topRightGrad));
		float dPtopLeft = topLeftDist.dot((topLeftGrad));
		float dPbtmRight = btmRightDist.dot((btmRightGrad));
		float dPbtmLeft = btmLeftDist.dot((btmLeftGrad));

		// Linearly-Interpolate between our dot products and (x,y) relative
		// to d00 to get a weighted average
		float u = ease(x - X); // This vector represents how where (x,y) is relative to the other
								// points. (or
								// (x,y) relative to d00)
		float v = ease(y - Y); // This is what we will interpolate by. We compute f(y-Y) where f is
								// a polynomial in order to 'smooth out' our values

		float l1 = lerp(u, dPtopLeft, dPtopRight);
		float l2 = lerp(u, dPbtmLeft, dPbtmRight);
		float n = lerp(v, l2, l1);// final weighted average of (x,y)
									// relative to d00 and all dot products

		return n;
	}

	/**
	 * This function implements the concept of multi-layered noise. The function
	 * 'layers' multiple noise functions on top of each other to allow for small
	 * changes. This is the main function to call with the PerlinNoise class
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @return The noise at the position
	 */
	public float noise(float x, float y) {
		float n = 0;
		float amp = this.amplitude;
		float freq = this.frequency;

		for (int i = 0; i < this.octaves; i++) {
			float nV = generateNoiseValue(x / this.scale * freq, y / this.scale * freq);
			n = n + (nV * amp);
			amp = amp * this.persistence;
			freq = freq * this.lacunarity;
		}
		return n;
	}

	public PerlinNoiseGenerator(float amplitude, float scale, int octaves, float frequency,
			float lacunarity, float persistence, int width, int height, long seed) {
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.octaves = octaves;
		this.lacunarity = lacunarity;
		this.persistence = persistence;
		this.scale = scale;
		setUpPermutationTable(seed);
	}
}