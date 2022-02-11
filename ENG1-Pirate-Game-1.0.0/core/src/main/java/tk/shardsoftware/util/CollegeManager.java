package tk.shardsoftware.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.WorldMap;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.EntityShip;

/**
 * @author James Burnell
 * @author Hector Woods
 */
public abstract class CollegeManager {

	// Prevent instantiation
	private CollegeManager() {
	}

	public static ArrayList<College> collegeList = new ArrayList<College>();
	public static ArrayList<String> availableCollegeNames;

	/**
	 * Generates a random college name from {@link #availableCollegeNames}, and
	 * removes it from the list so that each college name is unique.
	 * 
	 * @return A college name (string) from {@link #availableCollegeNames}
	 */
	public static String getRandomCollegeName() {
		if (availableCollegeNames.size() <= 0) {
			return "ERROR_NO_AVAILABLE_COLLEGE_NAMES";
		}
		int i = new Random().nextInt(availableCollegeNames.size());
		return availableCollegeNames.remove(i);
	}

	/**
	 * Gets a college of the specified name.
	 * 
	 * @param name Name of the college
	 * @return college with the specified name if it exists, otherwise {@code null}
	 */
	public static College getCollegeWithName(String name) {
		for (College c : collegeList) {
			if (c.getName() == name) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Sets one college friendly to the player, and all others to unfriendly.
	 * 
	 * @param name Name of the college the player chose
	 */
	public static void setFriendlyCollege(String name) {
		for (College c : collegeList) {
			if (c.getName() == name) {
				c.isFriendly = true;
			} else {
				/* Only one college is friendly so all others are set to not friendly */
				c.isFriendly = false;
			}
		}
	}

	/**
	 * Static method that generates numColleges College, provided that a valid
	 * position exists
	 * 
	 * @param worldObj A valid worldObj that the colleges will be located in
	 * @param numColleges The number of colleges in the world
	 * @param collegeMinDist Minimum distance between each college (magnitude of the
	 *        distance vector between the two)
	 * @param player the ship entity controlled by the player
	 */
	public static void generateColleges(World worldObj, int numColleges, float collegeMinDist,
			EntityShip player) {
		// FIXME: Add a check to ensure all colleges are within the accessible area
		availableCollegeNames = new ArrayList<>(
				Arrays.asList("James", "Constantine", "Alcuin", "Anne Lister", "David Kato",
						"Derwent", "Goodricke", "Halifax", "Langwith", "Vanbrugh", "Wentworth"));
		collegeList = new ArrayList<College>();
		WorldMap map = worldObj.worldMap;
		Function<Vector2, Boolean> collegePositionConds = vector2 -> {
			int x = (int) vector2.x;
			int y = (int) vector2.y;

			// Check that the college is not too close to the edges of the map
			if (x < 25 || y < 25 || x > map.width - 25 || y > map.height - 25) {
				return false;
			}

			// Check that the college is located on land
			TileType collegeTile = map.getTile(x, y);
			if (collegeTile == TileType.WATER_DEEP || collegeTile == TileType.WATER_SHALLOW) {
				return false;
			}
			// Check that the college is directly adjacent to some water
			if (!(map.getTile(x + 1, y) == TileType.WATER_SHALLOW)
					&& !(map.getTile(x - 1, y) == TileType.WATER_SHALLOW)
					&& !(map.getTile(x, y + 1) == TileType.WATER_SHALLOW)
					&& !(map.getTile(x, y - 1) == TileType.WATER_SHALLOW)) {
				return false;
			}
			/*
			 * Check that the college is not too close to other colleges in the world (as
			 * determined by collegeMinDist)
			 */
			for (College c : collegeList) {
				Vector2 pos = c.getPosition();
				Vector2 collegePosTiles = new Vector2(pos.x / map.tile_size, pos.y / map.tile_size);
				// Compare distance from the college to the point we want to place on
				float dist = vector2.dst(collegePosTiles);
				if (dist < collegeMinDist) {
					return false;
				}
			}

			// If all the above is true, this is a valid tile for a college to be placed
			return true;
		};
		for (int i = 0; i < numColleges; i++) {
			Vector2 collegePos = worldObj.worldMap.searchMap(collegePositionConds);
			if (collegePos != null) {
				String name = getRandomCollegeName();
				College c = new College(worldObj, name, collegePos.x * worldObj.worldMap.tile_size,
						collegePos.y * worldObj.worldMap.tile_size, map.tile_size * 6,
						map.tile_size * 6, player);
				c.setDirection(270);
				collegeList.add(c);
			}
		}
	}

}
