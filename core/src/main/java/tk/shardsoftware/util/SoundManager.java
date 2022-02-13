package tk.shardsoftware.util;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Manages playing sounds/music, adjusting volume, muting e.t.c
 * 
 * @author Hector Woods
 */
public abstract class SoundManager {

	// Prevent instantiation
	private SoundManager() {
	}

	/** The universal game volume */
	public static float gameVolume = 1f;
	public static boolean isMuted = false;
	public static float lastGameVolume = gameVolume;
	public static int currentSongIndex = 0;
	static Music[] songs = { ResourceUtil.getMusic("audio/music/the-pyre.mp3"),
			ResourceUtil.getMusic("audio/music/folk-round.mp3") };

	/**
	 * Plays a sound with the global volume. Won't play if the game is muted.
	 * 
	 * @param sound the sound to play
	 */
	public static void playSound(Sound sound) {
		if (isMuted) return;
		sound.play(gameVolume);
	}

	/**
	 * Plays a sound with the global volume multiplied by a specific volume. Won't
	 * play if the game is muted.
	 * 
	 * @param sound the sound to play
	 * @param volume the volume to play the sound at
	 */
	public static void playSound(Sound sound, float volume) {
		if (isMuted) return;
		sound.play(gameVolume * volume);
	}

	/**
	 * Plays music with the global volume. Won't play if the game is muted.
	 * 
	 * @param music the music to play
	 */
	public static void playMusic(Music music) {
		if (isMuted) return;
		music.play();
		music.setVolume(gameVolume);
	}

	/** Mutes the game volume */
	public static void muteVolume() {
		lastGameVolume = gameVolume;
		isMuted = true;
		gameVolume = 0;
		stopMusic();
	}

	public static void playRandomMusic() {
		// Set completion listener for each song so that it plays the next song in
		// Songs[]
		for (int i = 0; i < songs.length; i++) {
			songs[i].setOnCompletionListener(new Music.OnCompletionListener() {
				@Override
				public void onCompletion(Music music) {
					currentSongIndex = (currentSongIndex + 1) % songs.length;
					System.out.println("Song Finished. Now playing song " + currentSongIndex);
					playMusic(songs[currentSongIndex]);
				}
			});
		}
		// Choose a random song in the list and play it
		currentSongIndex = new Random().nextInt(songs.length);
		Gdx.app.log("SoundManager", "Playing song " + currentSongIndex);
		playMusic(songs[currentSongIndex]);
	}

	/** Stops all music playing */
	public static void stopMusic() {
		for (int i = 0; i < songs.length; i++) {
			if (songs[i].isPlaying()) {
				songs[i].stop();
			}
		}
	}

	public static void toggleMute() {
		isMuted = !isMuted;
		if (isMuted) {
			muteVolume();
		} else {
			gameVolume = lastGameVolume;
			playRandomMusic();
		}
	}

	public static void setVolume(float volume) {
		gameVolume = volume;
	}

}
