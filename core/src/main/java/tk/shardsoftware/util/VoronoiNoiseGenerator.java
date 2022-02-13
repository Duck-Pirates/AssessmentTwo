package tk.shardsoftware.util;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

/**
 * We didn't end up using this because it looked bad, but this is another method
 * for generating noise (based on voronoi diagrams:
 * https://en.wikipedia.org/wiki/Voronoi_diagram)
 * 
 * @author Hector Woods
 */
@Deprecated
public class VoronoiNoiseGenerator {

	public int numPoints;
	public int width;
	public int height;

	public double maxStart = 1;
	public double minStart = 0.95;

	public double minStep = -0.05;
	public double maxStep = -0.1;

	private Random r;
	private double[][] map = new double[width][height];

	private int randomRange(int min, int max) {
		return r.nextInt(max - min) + min;
	}

	public Vector2[] choosePoints() {
		Vector2[] points = new Vector2[numPoints];
		for (int i = 0; i < numPoints; i++) {
			Vector2 v = new Vector2(randomRange(0, width), randomRange(0, height));
			points[i] = v;
		}
		return points;
	}

	public void initiliaseMap() {
		for (double[] row : map) {
			Arrays.fill(row, 0);
		}
	}

	public void generate(Vector2[] points) {
		double[][] occupantMap = new double[width][height];
		for (int i = 0; i < numPoints; i++) {
			Vector2 point = points[i];

		}
	}

	public VoronoiNoiseGenerator(long seed, int width, int height) {
		this.r = new Random(seed);
		this.width = width;
		this.height = height;
		initiliaseMap();
		Vector2[] points = choosePoints();
		generate(points);
	}

}
