package tk.shardsoftware.util;

import static tk.shardsoftware.util.ResourceUtil.miniMapFont;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.Entity;
import tk.shardsoftware.entity.EntityAIShip;

/**
 * Draws a Minimap to the screen to help the player navigate.
 * 
 * @author Hector Woods
 * @author James Burnell
 */
public class Minimap implements Disposable {

	protected World worldObj;
	private Texture miniMapBorder;
	public Texture wholeMap;
	private Texture playerIcon;
	private Texture npcIcon;
	private Texture collegeIcon;

	/** Minimap button **/
	public Stage stage;
	private Drawable closeDrawable;
	private Drawable expandDrawable;
	private Drawable minimiseDrawable;
	private ImageButton bigMiniMapButton;
	private ImageButton closeMapButton;

	private float x;
	private float y;
	public int width;
	public int height;

	private float fullSizeX;
	private float fullSizeY;

	private static boolean drawBigmap = false;

	private static final int BORDER_WIDTH = 4;

	/**
	 * @param world the World object of the game
	 * @param x the x position to draw the minimap
	 * @param y the y position to draw the minimap
	 * @param width the width of the minimap
	 * @param height the height of the minimap
	 * @param batch the SpriteBatch used to draw the map
	 * @param stage the stage to control the minimap
	 */
	public Minimap(World world, float x, float y, int width, int height, SpriteBatch batch,
			Stage stage) {
		this.worldObj = world;
		miniMapBorder = ResourceUtil.getTexture("textures/ui/minimap-border.png");
		playerIcon = ResourceUtil.getTexture("textures/ui/player-map-icon.png");
		npcIcon = ResourceUtil.getTexture("textures/ui/enemy-map-icon.png");
		collegeIcon = ResourceUtil.getTexture("textures/ui/college-map-icon.png");
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		prepareMap();

		this.stage = stage;
		expandDrawable = new TextureRegionDrawable(
				new TextureRegion(ResourceUtil.getTexture("textures/ui/expand-map-button.png")));
		minimiseDrawable = new TextureRegionDrawable(
				new TextureRegion(ResourceUtil.getTexture("textures/ui/minimise-map-button.png")));
		closeDrawable = new TextureRegionDrawable(
				new TextureRegion(ResourceUtil.getTexture("textures/ui/close-map-button.png")));
		bigMiniMapButton = new ImageButton(expandDrawable, minimiseDrawable, minimiseDrawable);
		bigMiniMapButton.setPosition(145, Gdx.graphics.getHeight() - 170);
		bigMiniMapButton.setSize(25, 25);
		closeMapButton = new ImageButton(closeDrawable);
		closeMapButton.setPosition(fullSizeX + wholeMap.getWidth(),
				fullSizeY + wholeMap.getHeight());
		closeMapButton.setSize(35, 35);
		closeMapButton.setVisible(drawBigmap); // false by default
		Gdx.input.setInputProcessor(stage);

		/** Detect when big minimap button pressed **/
		bigMiniMapButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				drawBigmap = !drawBigmap;
				closeMapButton.setVisible(drawBigmap);
			}
		});

		closeMapButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				drawBigmap = false;
				closeMapButton.setVisible(false);
				if (bigMiniMapButton.isChecked()) { // Toggle the button
					bigMiniMapButton.setChecked(false);
				} else {
					bigMiniMapButton.setChecked(true);
				}
			}
		});
		stage.addActor(bigMiniMapButton);
		stage.addActor(closeMapButton);
	}

	/** Generate an image of the entire map */
	public void prepareMap() {
		Pixmap screen = new Pixmap(World.WORLD_WIDTH, World.WORLD_HEIGHT, Format.RGB888);

		Map<TileType, Integer> colors = getTileColors();

		for (int i = 0; i < World.WORLD_WIDTH; i++) {
			for (int j = 0; j < World.WORLD_HEIGHT; j++) {
				TileType tile = worldObj.worldMap.getTile(i, j);
				screen.setColor(colors.get(tile));
				screen.drawPixel(i, j);

			}
		}
		wholeMap = new Texture(screen);
		screen.dispose();

		this.fullSizeX = Gdx.graphics.getWidth() / 2 - wholeMap.getWidth() / 2;
		this.fullSizeY = Gdx.graphics.getHeight() / 2 - wholeMap.getHeight() / 2;
		if (closeMapButton != null) closeMapButton.setPosition(fullSizeX + wholeMap.getWidth(),
				fullSizeY + wholeMap.getHeight());
	}

	/**
	 * Generate a map of tile types and their corner pixel color
	 * 
	 * @return A map of tile types and their corresponding colors (RGBA8888)
	 * @see Pixmap.Format#RGBA8888
	 */
	private Map<TileType, Integer> getTileColors() {
		HashMap<TileType, Integer> result = new HashMap<TileType, Integer>();
		for (TileType t : TileType.values()) {
			t.getTex().getTextureData().prepare();
			result.put(t, t.getTex().getTextureData().consumePixmap().getPixel(0, 0));
		}

		return result;
	}

	private void drawEntities(SpriteBatch batch, int startX, int startY, int playerTileX,
			int playerTileY, float x, float y, float width, float height) {
		List<Entity> entities = this.worldObj.getEntities();
		Iterator<Entity> iter = entities.iterator();
		// keep track of original font colour
		Color oldColour = miniMapFont.getColor();
		while (iter.hasNext()) {
			Entity e = iter.next();
			if (e instanceof EntityAIShip) {
				Vector2 pos = e.getPosition();
				int tileX = (int) pos.x / worldObj.worldMap.tile_size;
				int tileY = (int) pos.y / worldObj.worldMap.tile_size;
				if (tileX > startX && tileY > startY && tileX - startX < width
						&& tileY - startY < height) { // if within range of the map
					batch.draw(npcIcon, x + tileX - startX, y + tileY - startY, 5, 5);
				}
			} else if (e instanceof College) {
				Vector2 pos = e.getPosition();
				int tileX = (int) pos.x / worldObj.worldMap.tile_size;
				int tileY = (int) pos.y / worldObj.worldMap.tile_size;
				if (tileX > startX && tileY > startY && tileX - startX < width
						&& tileY - startY < height) { // if within range of the map
					String cName = ((College) e).getName();

					// Get the width of the text after we draw it
					GlyphLayout gLayout = new GlyphLayout();
					gLayout.setText(miniMapFont, cName);
					float w = gLayout.width;

					if (((College) e).isFriendly) {
						miniMapFont.setColor(Color.BLUE);
					} else {
						miniMapFont.setColor(Color.WHITE);
					}
					miniMapFont.draw(batch, cName, x + tileX - startX - w / 2, y + tileY - startY);
					batch.draw(collegeIcon, x + tileX - startX, y + tileY - startY, 5, 5);
				}
			}
		}
		// reset font colour
		miniMapFont.setColor(oldColour);
		// Draw player
		batch.draw(playerIcon, x + playerTileX - startX, y + playerTileY - startY, 5, 5);

	}

	private void drawEntireMap(SpriteBatch batch, Vector2 playerPos) {
		// Draw minimap border
		batch.draw(miniMapBorder, fullSizeX, fullSizeY, wholeMap.getWidth(), wholeMap.getHeight());
		// Draw entire map
//		batch.draw(wholeMap, xPos + BORDER_WIDTH, yPos + BORDER_WIDTH, fullWidth - BORDER_WIDTH * 2,
//				fullHeight - BORDER_WIDTH * 2);
		batch.draw(wholeMap, fullSizeX + BORDER_WIDTH, fullSizeY + BORDER_WIDTH,
				wholeMap.getWidth() - BORDER_WIDTH * 2, wholeMap.getHeight() - BORDER_WIDTH * 2, 0,
				0, wholeMap.getWidth(), wholeMap.getHeight(), false, true);
		int playerTileX = (int) playerPos.x / worldObj.worldMap.tile_size;
		int playerTileY = (int) playerPos.y / worldObj.worldMap.tile_size;
		drawEntities(batch, 0, 0, playerTileX, playerTileY, fullSizeX, fullSizeY,
				worldObj.worldMap.width, worldObj.worldMap.height);
	}

	public void drawMap(SpriteBatch batch, Vector2 playerPos) {
		if (drawBigmap) {
			drawEntireMap(batch, playerPos);
		}

		int playerTileX = (int) playerPos.x / worldObj.worldMap.tile_size;
		int playerTileY = (int) playerPos.y / worldObj.worldMap.tile_size;

		int startX = (int) (playerTileX - width / 2 + 1);
		int startY = (int) (playerTileY - height / 2);

		if (startX < 0) {
			startX = 0;
		}
		if (startY < 0) {
			startY = 0;
		}

		if (startX > wholeMap.getWidth() - width) {
			startX = wholeMap.getWidth() - width;
		}
		if (startY > wholeMap.getHeight() - height) {
			startY = wholeMap.getHeight() - height;
		}

		// Draw minimap border
		batch.draw(miniMapBorder, x, y, width, height);

		// Draw a portion of the texture
		batch.draw(wholeMap, x + BORDER_WIDTH, y + BORDER_WIDTH, 0, 0, width - BORDER_WIDTH * 2,
				height - BORDER_WIDTH * 2, 1, 1, 0, startX, startY, width, height, false, true);
		drawEntities(batch, startX, startY, playerTileX, playerTileY, x, y, width, height);
	}

	@Override
	public void dispose() {
		wholeMap.dispose();
		miniMapBorder.dispose();
	}

	/** Called when the minimap toggle key was just pressed */
	public void onToggleKeyJustPressed() {
		drawBigmap = !drawBigmap;
		if (bigMiniMapButton.isChecked()) { // Toggle the button
			bigMiniMapButton.setChecked(false);
		} else {
			bigMiniMapButton.setChecked(true);
		}
		closeMapButton.setVisible(drawBigmap);
	}
}
