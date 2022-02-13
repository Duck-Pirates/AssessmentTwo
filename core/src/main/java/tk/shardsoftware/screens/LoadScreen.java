package tk.shardsoftware.screens;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.Bar;
import tk.shardsoftware.util.ResourceUtil;

/** @author James Burnell */
public class LoadScreen implements Screen {

	/**
	 * The main AssetManager for the game. Used to load the assets into memory
	 */
	private AssetManager assets;
	private SpriteBatch batch;
	private ShapeRenderer sp;
	/** The video player for the intro video */
	private VideoPlayer vPlayer;
//	/** The sound to be played during the intro */
//	private Sound logoSound;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	/** The Shard Software logo */
	private Sprite logo;
	/** The length of time the logo should be displayed in seconds */
	private static final float LOGO_DISPLAY_TIME = 2f;
	/** The length of time it takes for the logo to fade out in seconds */
	private static final float LOGO_FADE_TIME = 2f;
	/** Whether or not the logo is fading */
	private boolean fade = false;
	/** Whether the intro video has been skipped or not */
	private boolean skipped = false;
	/**
	 * The opacity of the logo. This is necessary because the sprite alpha channel
	 * is not precise enough.
	 */
	private float logoAlpha = 1f;

	private Vector2 progBarStart = new Vector2(25, Gdx.graphics.getHeight() - 25);
	private Vector2 progBarEnd = new Vector2(175, Gdx.graphics.getHeight() - 25);

	public LoadScreen(AssetManager assets, PirateGame pg) {
		this.assets = assets;
		this.pirateGameObj = pg;
		this.sp = new ShapeRenderer();
		logo = new Sprite(new Texture("textures/logo/shardlogo-fs.png"));
		logo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		// logoSound = Gdx.audio
		// .newSound(Gdx.files.internal("audio/logo/intro.mp3"));
		vPlayer = VideoPlayerCreator.createVideoPlayer();
		try {
			vPlayer.play(Gdx.files.internal("textures/logo/shardlogo.webm"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void show() {
		// logoSound.play(PirateGame.gameVolume);
		vPlayer.setOnCompletionListener(v -> {
			Timer.schedule(new Task() {
				public void run() {
					fade = true;
				}
			}, LOGO_DISPLAY_TIME);
		});
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 255);
		vPlayer.update();

		if (Gdx.input.isKeyJustPressed(Keys.SPACE) && vPlayer.getCurrentTimestamp() > 1500) {
			skipped = true;
			fade = true;
		}

		batch.begin();
		if (vPlayer.isPlaying() && !skipped) {
			batch.draw(vPlayer.getTexture(), 0, 0, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
		} else {
			if (fade && assets.isFinished()) {
				if (logo.getColor().a > 0) {
					logo.setAlpha(logoAlpha -= (delta / LOGO_FADE_TIME));
				} else {
					// Enter different state once faded
					pirateGameObj.openNewMenuScreen();
				}
			}
			logo.draw(batch);
		}
		batch.end();

		/* Render loading progress bar */
		batch.begin();
		if (!assets.update()) {

			// System.out.printf("Loaded %.1f%% of assets\n", assets.getProgress() * 100f);
			sp.begin(ShapeType.Filled);
			Bar.drawBar(batch, sp, progBarStart, progBarEnd, 1 - assets.getProgress(), Color.GRAY,
					Color.GREEN);
			sp.end();

		} else {
			ResourceUtil.debugFont.setColor(Color.DARK_GRAY);
			ResourceUtil.debugFont.draw(batch, "loaded content", 25, Gdx.graphics.getHeight() - 25);
		}
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
		// logoSound.stop();
	}

	@Override
	public void dispose() {
		batch.dispose();
		vPlayer.dispose();
		sp.dispose();
		// logoSound.dispose();
	}

}
