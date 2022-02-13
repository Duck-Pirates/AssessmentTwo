package tk.shardsoftware;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.util.PerlinNoiseGenerator;

/**
 * @author Hector Woods
 * @author James Burnell
 */
public class WorldMap {

	public long mapSeed;
	/** The width and height of each tile */
	public int tile_size;
	/** The width of the map in tiles */
	public int width;
	/** The height of the map in tiles */
	public int height;
	/** The placement of each tile within the world */
	public HashMap<Vector2, TileType> tileMap = new HashMap<Vector2, TileType>();

	public PerlinNoiseGenerator perlin;

	/** The local random object */
	private Random rand = new Random();

	/**
	 * Constructor for WorldMap.
	 * 
	 * @param world_tile_size The dimensions of each tile (in pixels) tile width =
	 *        world_tile_size = tile height
	 * @param world_width The number of tiles on each row
	 * @param world_height The number of tiles on each column
	 */
	public WorldMap(int world_tile_size, int world_width, int world_height) {
		this.tile_size = world_tile_size;
		this.width = world_width;
		this.height = world_height;
	}

	/**
	 * Generates a heightmap based on the seed, then samples this heightmap to
	 * choose either water, sand or grass tiles.
	 */
	public void buildWorld() {
		Gdx.app.log("WorldMap", "Seed=" + mapSeed);
		// clear map to allow for regeneration
		tileMap.clear();
		// choosing these values is more of an art than a science, see
		// PerlinNoiseGenerator for more info
		this.perlin = new PerlinNoiseGenerator(2f, 100, 12, 1, 1.3f, 0.66f, width, height, mapSeed);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vector2 key = new Vector2(i, j);

				float n = perlin.noise(i, j);

				if (n > 1) {
					this.tileMap.put(key, TileType.GRASS);
				} else if (n > 0.95) {
					this.tileMap.put(key, TileType.DIRT);
				} else if (n > 0.5) {
					this.tileMap.put(key, TileType.SAND); // sand
				} else if (n > 0) {
					this.tileMap.put(key, TileType.WATER_SHALLOW);
				}
				// If none of these are satisfied then when this tile is read the result will be
				// null which is interpreted as TileType.WATER_DEEP. This was done to save
				// memory

			}
		}

	}

	/**
	 * Draw the tile located at point P=(x,y).
	 * 
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @param batch The SpriteBatch that the tile is to be drawn by
	 * @see #getTile(int, int)
	 */
	public void drawTile(int x, int y, SpriteBatch batch) {
		Texture texture = this.getTile(x, y).getTex();
		batch.draw(texture, x * tile_size, y * tile_size, tile_size, tile_size);
	}

	/**
	 * Searches the map based on the result of a function passed as an argument.
	 * Accepts any Function which accepts a Vector2 as input and returns a
	 * boolean.<br>
	 * See {@link tk.shardsoftware.screens.GameScreen#setPlayerStartPosition()} for
	 * an example of its use.
	 *
	 * @param searchCond A function that takes a vector2 as input and returns either
	 *        true or false based on whether that position is valid
	 * @return Returns either a valid Vector2 representing a position that passed
	 *         the search conditions (i.e searchCond returns true), or null if no
	 *         such position exists.
	 */
	public Vector2 searchMap(Function<Vector2, Boolean> searchCond) {
		// Generate a 2D array containing every location on the map, then shuffle it so
		// that our start position is randomised.
		// If this is not done then the lower left corner of the map is heavily favoured
		// by this function.
		Vector2[][] randomisedMap = new Vector2[width][height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				randomisedMap[i][j] = new Vector2(i, j);
			}
		}

		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				int randI = rand.nextInt(width);
				int randJ = rand.nextInt(height);
				// swap values to shuffle
				Vector2 temp = randomisedMap[randI][randJ];
				randomisedMap[randI][randJ] = randomisedMap[i][j];
				randomisedMap[i][j] = temp;
			}
		}

		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				Boolean satisfiesCond = false;
				Vector2 v = randomisedMap[i][j];
				try { // Catch any exceptions in the passed method
					satisfiesCond = searchCond.apply(v);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (satisfiesCond) {
					return v;
				}
			}
		}
		return null;
	}

	// TODO: Render once to off-screen buffer then render buffer to screen
	/**
	 * Draws all tiles in range of the camera.
	 * 
	 * @param cam The camera. Contains information about the viewport width and
	 *        height as well as the position of the camera in the world.
	 * @param batch The SpriteBatch that will draw the tiles.
	 */
	public void drawTilesInRange(Camera cam, SpriteBatch batch) {
		int numberOfTilesX = (int) (cam.viewportWidth / tile_size);
		int numberOfTilesY = (int) (cam.viewportHeight / tile_size);
		int cameraTilePosX = (int) (cam.position.x / tile_size);
		int cameraTilePosY = (int) (cam.position.y / tile_size);
		int minX = Math.max(1, cameraTilePosX - (numberOfTilesX));
		int minY = Math.max(1, cameraTilePosY - (numberOfTilesY));
		int maxX = Math.min(width, cameraTilePosX + (numberOfTilesX));
		int maxY = Math.min(height, cameraTilePosY + (numberOfTilesY));

		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				this.drawTile(i, j, batch);
			}
		}
	}

	/**
	 * Get the tile type of the tile positioned at (x,y). If there is no tile
	 * defined at this point, it will return {@link TileType#WATER_DEEP}.
	 * 
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @return The tile type at the position, or {@link TileType#WATER_DEEP} if
	 *         there is none
	 */
	public TileType getTile(int x, int y) {
		return tileMap.getOrDefault(new Vector2(x, y), TileType.WATER_DEEP);
	}

	/**
	 * Returns a collection of tiles that are within the bounds of the rectangle
	 * 
	 * @param rect the search area
	 * @param filterOnlySolid Filter only the tiles that would cause collision
	 * @return The collection of tiles and their types within the area
	 * 
	 * @author James Burnell
	 */
	public Map<Vector2, TileType> getTilesWithinArea(Rectangle rect, boolean filterOnlySolid) {
		/* Create a scaled rectangle based on tiles, not pixels */
		int x = MathUtils.floor(rect.x / tile_size);
		int y = MathUtils.floor(rect.y / tile_size);
		int width = MathUtils.ceil(rect.width / tile_size);
		int height = MathUtils.ceil(rect.height / tile_size);
//		Rectangle scaledRect = new Rectangle(x, y, width, height);

//		/* Filter out only those tiles that are within the rectangle */
//		Map<Vector2, TileType> result = tileMap.entrySet().stream()
//				.filter(e -> (filterOnlySolid ? e.getValue().solid : true)
//						&& scaledRect.contains(e.getKey()))
//				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

		Map<Vector2, TileType> result = new HashMap<Vector2, TileType>();

		for (int i = x; i <= x + width; i++) {
			for (int j = y; j <= y + height; j++) {
				Vector2 key = new Vector2(i, j);
				TileType t = tileMap.getOrDefault(key, TileType.WATER_DEEP);
				// Skip to next tile if needs to be solid and isn't
				if (filterOnlySolid && !t.isSolid()) continue;
				result.put(key, t);
			}
		}

		return result;
	}

	/**
	 * By using streams, it returns whether or not there is at least one solid tile
	 * within a given area. Given the nature of streams and filters, the efficiency
	 * is poor.
	 * 
	 * @param rect the search area
	 * @return {@code true} if there is at least one solid tile, {@code false}
	 *         otherwise
	 * 
	 * @author James Burnell
	 * @see #isSolidTileWithinArea(Rectangle)
	 */
	@Deprecated
	public boolean isSolidTileWithinAreaStream(Rectangle rect) {
		/* Create a scaled rectangle based on tiles, not pixels */
		int x = MathUtils.floor(rect.x / tile_size);
		int y = MathUtils.floor(rect.y / tile_size);
		int width = MathUtils.ceil(rect.width / tile_size);
		int height = MathUtils.ceil(rect.height / tile_size);
		Rectangle scaledRect = new Rectangle(x, y, width, height);

		return tileMap.entrySet().stream()
				.anyMatch(e -> e.getValue().isSolid() && scaledRect.contains(e.getKey()));
	}

	/**
	 * Returns whether or not there is at least one solid tile within a given area
	 * 
	 * @param rect the search area
	 * @return {@code true} if there is at least one solid tile, {@code false}
	 *         otherwise
	 * @author James Burnell
	 */
	public boolean isSolidTileWithinArea(Rectangle rect) {
		/* Create a scaled rectangle based on tiles, not pixels */
		int x = MathUtils.floor(rect.x / tile_size);
		int y = MathUtils.floor(rect.y / tile_size);
		int width = MathUtils.ceil(rect.width / tile_size);
		int height = MathUtils.ceil(rect.height / tile_size);

		for (int i = x; i <= x + width; i++) {
			for (int j = y; j <= y + height; j++) {
				if (getTile(i, j).isSolid()) return true;
			}
		}

		return false;
	}

	public void setSeed(long seed) {
		this.mapSeed = seed;
		rand.setSeed(mapSeed);
	}

}
