package tk.shardsoftware.util;

import static tk.shardsoftware.util.ResourceUtil.font;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import tk.shardsoftware.World;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.screens.GameScreen;

/** @author Hector Woods */
public class ChooseCollegeDisplay extends Minimap {

	/** The list of colleges present in the world */
	private List<College> collegeList;
	/** The texture of college to draw on the map */
	private Texture collegeTexture = ResourceUtil.getTexture("textures/ui/college-map-icon.png");
	/** The list of buttons the user can interact with */
	private List<Button> buttons;
	private Texture textTexture = ResourceUtil.getTexture("textures/ui/college-choice-text.png");

	/** Removes all buttons from their parent objects */
	private void removeActors() {
		Iterator<Button> iterButtons = buttons.iterator();
		while (iterButtons.hasNext()) {
			Button b = iterButtons.next();
			b.remove();
		}
	}

	/**
	 * Gets the position of a specific college
	 * 
	 * @param c the college to locate
	 * @return The position of the college
	 */
	private Vector2 getCollegePosition(College c) {
		Vector2 collegePos = c.getPosition();
		// Get the amount by which the image has been rescaled
		int originalWidth = wholeMap.getWidth();
		int originalHeight = wholeMap.getHeight();
		float widthMulti = width / originalWidth;
		float heightMulti = height / originalHeight;

		// Get the tile the college is located on
		int tileX = (int) collegePos.x / this.worldObj.worldMap.tile_size;
		int tileY = (int) collegePos.y / this.worldObj.worldMap.tile_size;

		// Convert the tile to a pixel on the image (on the original image it was 1-1,
		// so we multiply by multipliers to get the position on the new image)
		float pixelX = tileX * widthMulti;
		float pixelY = tileY * heightMulti;
		return new Vector2(pixelX, pixelY);
	}

	public ChooseCollegeDisplay(World world, float x, float y, int width, int height,
			SpriteBatch batch, Stage stage, List<College> collegeList, GameScreen gs) {
		// create minimap
		super(world, x, y, width, height, batch, stage);
		this.collegeList = collegeList;
		buttons = new ArrayList<Button>();

		// create buttons
		Drawable buttonBackground = new TextureRegionDrawable(new TextureRegion(collegeTexture));

		Iterator<College> iter = collegeList.iterator();
		while (iter.hasNext()) {
			College c = iter.next();
			// Get college info
			Vector2 collegePos = getCollegePosition(c);
			String cName = c.getName();
			Button button = new ImageButton(buttonBackground);
//			float buttonWidth = button.getWidth();
//			float buttonHeight = button.getHeight();
			stage.addActor(button);
			button.setPosition(collegePos.x, collegePos.y);
			button.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					gs.setPlayerCollege(cName);
					removeActors();
				}
			});
			buttons.add(button);
		}

	}

	public void drawColleges(SpriteBatch batch) {
		Iterator<Button> iterButtons = buttons.iterator();
		while (iterButtons.hasNext()) {
			Button b = iterButtons.next();
			b.draw(batch, 1f);
		}
		Iterator<College> iterColleges = collegeList.iterator();
		while (iterColleges.hasNext()) {
			College c = iterColleges.next();
			String cName = c.getName();
			Vector2 collegePos = getCollegePosition(c);

			GlyphLayout layout = new GlyphLayout(font, cName);
			float fontX = (float) (collegePos.x - layout.width * 0.4);
			float fontY = (float) (collegePos.y - layout.width * 0.15);

			font.draw(batch, layout, fontX, fontY);
		}

	}

	public void drawChooseCollegeDisplay(SpriteBatch batch) {
		batch.draw(wholeMap, 0, 0, width, height, 0, 0, wholeMap.getWidth(), wholeMap.getHeight(),
				false, true);
		drawColleges(batch);
		// Draw text at the top of the screen
		batch.draw(textTexture, width / 2 - (float) (width * 0.15), height - (float) (height * 0.1),
				width / 3, height / 8);
	}

}
