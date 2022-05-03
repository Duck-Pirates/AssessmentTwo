package com.mygdx.pirategame.configs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Audio Controls
 * Creates the interface to write and read options in some files that can be retrieved easily
 *
 * @author Sam Pearson
 * @version 1.0
 */
public class audioControls {

    //Set keys that are stored in the file
    private static final String MUSIC_VOLUME = "volume";
    private static final String MUSIC_ENABLED = "music.enabled";
    private static final String SOUND_ENABLED = "sound.enabled";
    private static final String EFFECT_VOL = "sound";
    private static final String PAGE = "audio";


    protected Preferences optionsObtains() {
        return Gdx.app.getPreferences(PAGE);
    }

    public boolean isEffectsEnabled() {
        return optionsObtains().getBoolean(SOUND_ENABLED, true);
    }

    public boolean isMusicEnabled() {
        return optionsObtains().getBoolean(MUSIC_ENABLED, true);
    }

    public float getMusicVolume() {
        return optionsObtains().getFloat(MUSIC_VOLUME, 0.5f);
    }

    public float getEffectsVolume() {
        return optionsObtains().getFloat(EFFECT_VOL, 0.5f);
    }

    public void setEffectsEnabled(boolean EffectsOn) {

        optionsObtains().putBoolean(SOUND_ENABLED, EffectsOn);
        optionsObtains().flush();

    }

    public void setMusicEnabled(boolean musicOn) {

        optionsObtains().putBoolean(MUSIC_ENABLED, musicOn);
        optionsObtains().flush();

    }

    public void setMusicVolume(float volume) {

        optionsObtains().putFloat(MUSIC_VOLUME, volume);
        optionsObtains().flush();

    }

    public void setEffectsVolume(float volume) {

        optionsObtains().putFloat(EFFECT_VOL, volume);
        optionsObtains().flush();

    }

}
