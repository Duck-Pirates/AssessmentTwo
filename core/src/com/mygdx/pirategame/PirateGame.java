package com.mygdx.pirategame;

import static com.mygdx.pirategame.configs.Constants.DEATH;
import static com.mygdx.pirategame.configs.Constants.GAME;
import static com.mygdx.pirategame.configs.Constants.HELP;
import static com.mygdx.pirategame.configs.Constants.LOADING;
import static com.mygdx.pirategame.configs.Constants.MENU;
import static com.mygdx.pirategame.configs.Constants.SKILL;
import static com.mygdx.pirategame.configs.Constants.VICTORY;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.pirategame.configs.Difficulty;
import com.mygdx.pirategame.configs.GameSave;
import com.mygdx.pirategame.configs.audioControls;
import com.mygdx.pirategame.screens.DeathScreen;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Help;
import com.mygdx.pirategame.screens.LoadingMenu;
import com.mygdx.pirategame.screens.MainMenu;
import com.mygdx.pirategame.screens.SkillTree;
import com.mygdx.pirategame.screens.VictoryScreen;


/**
 * The start of the program. Sets up the main back bone of the game.
 * This includes most constants used through out for collision and changing screens
 * Provides access for screens to interact with each other and the options interface
 * @author Sam Pearson
 * @version 1.0
 */
public class PirateGame extends Game {
	private SpriteBatch batch;

	//Variable for each screen
	private MainMenu menuScreen;
	private GameScreen gameScreen;
	private SkillTree skillTreeScreen;
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
		batch = new SpriteBatch();
		//Set starting screen
		MainMenu mainMenu = new MainMenu(this);
		setScreen(mainMenu);
		//Create options
		options = new audioControls();

		//Set background music and play if valid
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
	 */
	public void changeScreen(int screen) {
		//Depending on which value given, change the screen
		switch (screen) {
			case MENU:
				if (menuScreen == null) menuScreen = new MainMenu(this);
				this.setScreen(menuScreen);
				break;

			case GAME:
				if (getGameScreen() == null) gameScreen = new GameScreen(this, difficulty);
				if (getSkillTreeScreen() == null) skillTreeScreen = new SkillTree(this);
				this.setScreen(getGameScreen());
				break;

			case SKILL:
				if (getSkillTreeScreen() == null) skillTreeScreen = new SkillTree(this);
				this.setScreen(getSkillTreeScreen());
				break;

			case DEATH:
				if (deathScreen == null) deathScreen = new DeathScreen(this);
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
	 * Allows the user to interact with the audio options
	 *
	 * @return the options object
	 */
	public audioControls getPreferences() {
		return this.options;
	}

	/**
	 * Kills the game screen and skill tree so they can be refreshed on next game start
	 */
	public void killGame(){
		gameScreen.dispose();
		skillTreeScreen.dispose();
		gameScreen = null;
		skillTreeScreen = null;
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
	 * Renders the visual data for all objects
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * Disposes game data
	 */
	@Override
	public void dispose () {
		song.dispose();
		batch.dispose();
		super.dispose();
	}

	public void save() {
		GameSave gameInstance = new GameSave();
		gameInstance.save(gameScreen, skillTreeScreen);
	}

	public void load(){
		GameSave gameInstance = new GameSave();
		gameInstance.load(this);
		this.changeScreen(GAME);
	}
	
	public static void setDifficulty(Difficulty difficulty) {
		PirateGame.difficulty = difficulty;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public SkillTree getSkillTreeScreen() {
		return skillTreeScreen;
	}

	public void setSkillTreeScreen(SkillTree skillTreeScreen) {
		this.skillTreeScreen = skillTreeScreen;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	/**
	 * @return the song
	 */
	public Music getSong() {
		return song;
	}

	/**
	 * @param song the song to set
	 */
	public void setSong(Music song) {
		this.song = song;
	}
}
