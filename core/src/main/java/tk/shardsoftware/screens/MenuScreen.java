package tk.shardsoftware.screens;

import static tk.shardsoftware.util.ResourceUtil.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.ResourceUtil;

/**
 * The menu screen
 * 
 * @author James Burnell
 * @author Hector Woods
 */
public class MenuScreen implements Screen {

	private SpriteBatch batch;
	private Music menuMusic = ResourceUtil.getMusic("audio/music/tiki-bar-mixer.mp3");

	/** Width of the display */
	private int width;
	/** Height of the display */
	private int height;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	private GlyphLayout text;

	/** Texture for the background */
	private Texture background = ResourceUtil.getTexture("textures/ui/menu-screen-background.png");

	private Texture shardLogo;

	/**
	 * Constructor for LossScreen
	 * 
	 * @param pg An instance of PirateGame
	 */
	public MenuScreen(PirateGame pg) {
		this.pirateGameObj = pg;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		text = new GlyphLayout();
		text.setText(font, "Press the space key to start your adventure");
		shardLogo = new Texture("textures/logo/shardlogo.png");
	}

	@Override
	public void show() {
		System.out.println("Entering the main menu...");
		// SoundManager.playMusic(menuMusic);
	}

	private void closeScreen() {
		menuMusic.stop();
		pirateGameObj.openNewGameScreen();
	}

	@Override
	public void render(float delta) {
		// Restart the game when a key is pressed
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			closeScreen();
		}

		batch.begin();
		batch.draw(background, 0, 0, width, height);
		font.draw(batch, text, (width - text.width) / 2, (height - text.height) / 2);

		batch.draw(shardLogo, 5, 5, 640 / 3, 267 / 3);

		batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		batch.dispose();
		shardLogo.dispose();
	}

}
