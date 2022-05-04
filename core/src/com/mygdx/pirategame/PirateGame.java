package com.mygdx.pirategame;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.configs.*;
import com.mygdx.pirategame.screens.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * The start of the program. Sets up the main backbone of the game.
 * This includes most constants used throughout for collision and changing screens
 * Provides access for screens to interact with each other and the options interface
 *
 * @author Sam Pearson
 * @version 1.0
 */
public class PirateGame extends Game {

	private SpriteBatch batch;

	// Variables for each screen
	private MainMenu menuScreen;
	private GameScreen gameScreen;
	private Shop shopScreen;
	private DeathScreen deathScreen;
	private Help helpScreen;
	private VictoryScreen victoryScreen;
	private LoadingMenu loadingScreen;

	private audioControls options;
	private Music song;
	private static Difficulty difficulty;

	/**
	 * Creates the main body of the game.
	 * Establishes the batch for the whole game as well as sets the first screen
	 * Also sets up audio interface
	 */
	@Override
	public void create () {

		// Set starting screen
		setScreen(menuScreen);

		// Create options
		options = new audioControls();

		// Set background music and play if valid
		setSong(Gdx.audio.newMusic(Gdx.files.internal("pirate-music.mp3")));
		getSong().setLooping(true);
		if(getPreferences().isMusicEnabled()){
			getSong().play();
		}
		getSong().setVolume(getPreferences().getMusicVolume());

	}

	/**
	 * Changes the screen without killing the prior screen. Allows for the screens to be returned to without making new ones
	 *
	 * @param screen the number of the screen that the user wants to swap to
	 * @param newScreen This boolean is passed to make creating a new game after loading one possible
	 */
	public void changeScreen(int screen, boolean newScreen) {

		switch (screen) {

			case MENU:
				if (menuScreen == null) menuScreen = new MainMenu(this);
				this.setScreen(menuScreen);
				break;

			case GAME:
				if (getGameScreen() == null || newScreen) gameScreen = new GameScreen(this, difficulty);
				if (getShopScreen() == null ||  newScreen) shopScreen = new Shop(this);
				this.setScreen(getGameScreen());
				break;

			case SKILL:
				if (getShopScreen() == null) shopScreen = new Shop(this);
				this.setScreen(getShopScreen());
				break;

			case DEATH:
				if (deathScreen == null || newScreen) deathScreen = new DeathScreen(this);
				this.setScreen(deathScreen);
				break;

			case HELP:
				if (helpScreen == null) helpScreen = new Help(this);
				this.setScreen(helpScreen);
				break;

			case VICTORY:
				if (victoryScreen == null) victoryScreen = new VictoryScreen(this);
				this.setScreen(victoryScreen);
				break;

			case LOADING:
				if (loadingScreen == null) loadingScreen = new LoadingMenu(this);
				this.setScreen(loadingScreen);
				break;
				
		}

	}

	/**
	 * Kills the game screen and skill tree, so they can be refreshed on next game start
	 */
	public void killGame(){

		gameScreen.dispose();
		shopScreen.dispose();
		gameScreen = null;
		shopScreen = null;

	}

	/**
	 * Kill end screens so they can be made again.
	 */
	public void killEndScreen(){
		deathScreen.dispose();
		victoryScreen.dispose();
		deathScreen = null;
		victoryScreen = null;
	}

	/**
	 * (Not Used)
	 *
	 * Renders the visual data for all objects
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * Disposes the Pirate Game data
	 */
	@Override
	public void dispose () {

		song.dispose();
		batch.dispose();
		super.dispose();

	}

	/**
	 * Start the saving process whenever the player clicks on the save button
	 */
	public void save() {

		GameSave gameInstance = new GameSave();
		gameInstance.save(gameScreen);

	}

	/**
	 * Start the loading process whenever the player clicks on the load button
	 */
	public void load(){

		GameSave gameInstance = new GameSave();
		killGame();
		gameInstance.load(this);
		this.changeScreen(GAME, false);

	}

	public SpriteBatch getBatch() { return batch; }

	public Music getSong() {
		return song;
	}

	public audioControls getPreferences() { return this.options; }

	public GameScreen getGameScreen() { return gameScreen; }
	
	public static void setDifficulty(Difficulty difficulty) { PirateGame.difficulty = difficulty; }

	public void setGameScreen(GameScreen gameScreen) { this.gameScreen = gameScreen; }

	public Shop getShopScreen() { return shopScreen; }

	public void setShopScreen(Shop shopScreen) { this.shopScreen = shopScreen; }

	public void setSong(Music song) {
		this.song = song;
	}

}
