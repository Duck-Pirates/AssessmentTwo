package tk.shardsoftware.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;

import tk.shardsoftware.World;
import tk.shardsoftware.entity.EntityShip;

/**
 * A collection of tools to help in the debugging process.
 * 
 * @author James Burnell
 */
public class DebugUtil {

	// Prevent instantiation
	private DebugUtil() {
	}

	/** Controls additional logging and on-screen data */
	public static boolean DEBUG_MODE = false;

	/**
	 * A list of maps determined to be playable. This is to have a reliable testing
	 * pool of maps.
	 */
	public static final long[] DEBUG_MAP_LIST = { -3118584395246760715l, 2529255383301275808l,
			6130359115894938221l, 7930068207639905836l, 3269761978655777716l, 4236093909831674207l,
			-509058019939334172l, 9176118902378253189l, -1725988101884335641l, 1074621698543254129l,
			-297953574914369051l, -5750812775633468735l, -1056105497059490975l,
			7959384176659588372l, 5193999824609830996l, -2640064327703070913l,
			-6518028724896499627l, -5881081205293033051l, -7501673898816366006l,
			-8107408079478611814l, -6702694815751317625l, -8743913861296944165l,
			155737000087122849l, 2377344836818596457l };

	/** Stores times of how long it took to process code snippets */
	public static HashMap<String, Long> processingTimes = new HashMap<String, Long>();

	private static long displayTime;
	private static List<String> cachedResult;

	/**
	 * Calculate the percentage time for each entry in {@link #processingTimes} and
	 * return each in a separate string, all contained in a list.
	 * 
	 * @return A list of strings that contain timing information about the different
	 *         processes
	 */
	public static List<String> processingTimePercentages() {
		if (System.currentTimeMillis() - displayTime < 1000) {
			return cachedResult;
		}
		displayTime = System.currentTimeMillis();

		ArrayList<String> result = new ArrayList<String>();

		long total = processingTimes.values().stream().reduce(0l, Long::sum);

		for (HashMap.Entry<String, Long> entry : processingTimes.entrySet()) {
			String key = entry.getKey();
			Long val = entry.getValue();
			result.add(String.format("%s: %5.2fms (%2.0f%%)", key, val / 1000000f,
					(val / (float) total) * 100));
		}

		cachedResult = result;

		return result;
	}

	/**
	 * Measure how long a code section takes to process.
	 * 
	 * @param r the code to run and measure
	 * @return The time taken to execute the code in nanoseconds
	 */
	public static long measureProcessTime(Runnable r) {
		long t1 = System.nanoTime();
		r.run();
		return System.nanoTime() - t1;
	}

	/**
	 * Measures how long it takes to run the code then saves it to the cache for
	 * comparison &#38; display.
	 * 
	 * @param name the displayed name of the code being measured
	 * @param r the code to run and measure
	 * @see #measureProcessTime(Runnable)
	 */
	public static void saveProcessTime(String name, Runnable r) {
		long time = measureProcessTime(r);
		processingTimes.put(name, time);
	}

	/**
	 * Generates the debug hud's displayed text
	 * 
	 * @param player the ship object controlled by the player
	 * @param worldObj the world of the game
	 * @param goalAngle the direction the ship should be pointing in
	 * @return list of strings to be drawn on the debug hud
	 */
	public static List<String> generateDebugStrings(EntityShip player, World worldObj,
			float goalAngle) {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(String.format("Current angle: %5.1f", player.getDirection()));
		lines.add(String.format("Goal angle: %6.1f", goalAngle));
		lines.add(String.format("Speed %4.1fp/s", player.getVelocity().len()));
		lines.add("FPS: " + Gdx.graphics.getFramesPerSecond());
		lines.add(String.format("Entities %3d", worldObj.getEntities().size()));
		lines.add("");// blank line
		lines.addAll(DebugUtil.processingTimePercentages());

		return lines;
	}

	/**
	 * Causes d damage to all entities in the game. intended for debug only.
	 * 
	 * @param worldObj the world object that contains the entities
	 * @param d the amount of damage
	 */
	public static void damageAllEntities(World worldObj, float d) {
		worldObj.getAllDamageable().forEach(e -> e.damage(d));
	}

}
