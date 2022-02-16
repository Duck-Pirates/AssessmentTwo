package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Provides a UI for the user to interact with the audioControls interface
 * @author Sam Pearson
 * @version 1.0
 */
public class Options implements Screen {

    private final PirateGame PirateGame;
    private final Screen parent;
    private final Stage stage;

    /**
     * Instantiates a new Options screen
     *
     * @param pirateGame the main starting body of the game. Where screen swapping is carried out.
     * @param parent     the screen that called the options screen. Allows for easy return
     */
    public Options(PirateGame pirateGame, Screen parent){
        this.PirateGame = pirateGame;
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
    }

    /**
     * What should be displayed on the options screen
     *
     */
    @Override
    public void show() {
        //Set the input processor
        Gdx.input.setInputProcessor(stage);
        stage.clear();
        // Create the main table
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //The skin for the actors
        Skin skin = new Skin(Gdx.files.internal("skin\\uiskin.json"));

        //Music Sliders and Check boxes
        final Slider volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );

        //Set value to current option
        volumeMusicSlider.setValue( PirateGame.getPreferences().getMusicVolume() );

        volumeMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                PirateGame.getPreferences().setMusicVolume(volumeMusicSlider.getValue());  //Change music value in options to slider
                PirateGame.song.setVolume(PirateGame.getPreferences().getMusicVolume()); //Change the volume

                return false;
            }
        });

        final CheckBox musicCheckbox = new CheckBox(null, skin);

        //Check if it should be checked or unchecked by default
        musicCheckbox.setChecked( PirateGame.getPreferences().isMusicEnabled() );

        musicCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked(); //Get checked value
                PirateGame.getPreferences().setMusicEnabled( enabled ); //Set

                if(PirateGame.getPreferences().isMusicEnabled()){ //Play or don't
                    PirateGame.song.play();
                }
                else {
                    PirateGame.song.pause();}

                return false;
            }
        });

        //EFFECTS
        final Slider volumeEffectSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeEffectSlider.setValue( PirateGame.getPreferences().getEffectsVolume() ); //Set value to current option
        volumeEffectSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                PirateGame.getPreferences().setEffectsVolume(volumeEffectSlider.getValue()); //Change effect value in options to slider
                return false;
            }
        });

        final CheckBox effectCheckbox = new CheckBox(null, skin);
        effectCheckbox.setChecked( PirateGame.getPreferences().isEffectsEnabled() );
        effectCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = effectCheckbox.isChecked(); //Get checked value
                PirateGame.getPreferences().setEffectsEnabled( enabled ); //Set
                return false;
            }
        });

        // return to main screen button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PirateGame.setScreen(parent);
            }
        });



        Label titleLabel = new Label("Options", skin);
        Label musicLabel = new Label("Music Volume", skin);
        Label effectLabel = new Label("Effect Volume", skin);
        Label musicOnLabel = new Label("Music On/Off", skin);
        Label effectOnLabel = new Label("Effect On/Off", skin);

        //add buttons,sliders and labels to table
        table.add(titleLabel).colspan(2);
        table.row().pad(10,0,0,0);
        table.add(musicLabel).left();
        table.add(volumeMusicSlider);
        table.row().pad(10,0,0,0);
        table.add(musicOnLabel).left();
        table.add(musicCheckbox);
        table.row().pad(10,0,0,0);
        table.add(effectLabel).left();
        table.add(volumeEffectSlider);
        table.row().pad(10,0,0,0);
        table.add(effectOnLabel).left();
        table.add(effectCheckbox);
        table.row().pad(10,0,0,10);
        table.add(backButton);

    }
    /**
     * Renders the visual data for all objects
     * @param delta Delta Time
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();



    }
    /**
     * Changes the camera size, Scales the hud to match the camera
     *
     * @param width the width of the viewable area
     * @param height the height of the viewable area
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * (Not Used)
     * Pauses game
     */
    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    /**
     * (Not Used)
     * Resumes game
     */
    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }
    /**
     * (Not Used)
     * Hides game
     */

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    /**
     * Disposes game data
     */
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }
}




