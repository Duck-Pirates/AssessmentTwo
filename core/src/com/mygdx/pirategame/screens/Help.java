package com.mygdx.pirategame.screens;

import static com.mygdx.pirategame.configs.Constants.MENU;
import com.mygdx.pirategame.PirateGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


/**
 * Screen with instructions for the user
 *
 * @author Sam Pearson
 * @version 1.0
 */
public class Help implements Screen {

    private final PirateGame parent;
    private final Stage stage;
    private final TextureRegion background = new TextureRegion(new Texture("map2.png"));

    /**
     * In the constructor, the parent and stage are set. Also, the states list is set
     *
     * @param pirateGame Game data
     */
    public Help(PirateGame pirateGame){

        parent = pirateGame;
        stage = new Stage(new ScreenViewport());

    }

    /**
     * Displays help data
     */
    @Override
    public void show() {

        //Set the input processor
        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Table for the return button
        final Table Other = new Table();
        Other.setFillParent(true);
        stage.addActor(Other);

        //The skin for the actors
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //Text
        Label Controls1 = new Label("WASD to move", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label Controls2 = new Label("SPACE to fire", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label Controls3 = new Label("ESCAPE to see menu", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label objective1 = new Label("The objective is to take over or destroy all other colleges", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label objective2 = new Label("Destroy the college flag with cannons", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label objective3 = new Label("Collect coins on the way", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label skillInfo1 = new Label("Use the coins you earn to unlock abilities along the way", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label skillInfo2 = new Label("See your upgrades in the skills tab", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Label powerupInfo1 = new Label("Collect power-ups along the way to give you temporary abilities", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label powerupInfo2 = new Label("Power-ups include: Increased Speed (Star), Health Regeneration (Wrench and Bolt),", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label powerupInfo3 = new Label("Temporary Immunity (Star), Increased Damage (Ammo) and Increased Coin Earnings (Coins)", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //Return Button
        TextButton backButton = new TextButton("Return", skin);
        backButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MENU, false);
            }
        });

        table.add(backButton);
        table.row().pad(10, 0, 10, 0);
        table.left().top();

        //add return button
        Other.add(Controls1);
        Other.row();
        Other.add(Controls2);
        Other.row();
        Other.add(Controls3).padBottom((40));
        Other.row();
        Other.add(objective1);
        Other.row();
        Other.add(objective2);
        Other.row();
        Other.add(objective3).padBottom((40));
        Other.row();
        Other.add(skillInfo1);
        Other.row();
        Other.add(skillInfo2).padBottom((40));
        Other.row();
        Other.add(powerupInfo1);
        Other.row();
        Other.add(powerupInfo2);
        Other.row();
        Other.add(powerupInfo3);
        Other.center();

    }

    /**
     * Renders visual data with delta time
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.draw();

    }

    /**
     * Changes the camera size
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
     * Pauses the screen
     */
    @Override
    public void pause() {}

    /**
     * (Not Used)
     * Resumes the screen
     */
    @Override
    public void resume() {}

    /**
     * (Not Used)
     * Hides the screen
     */
    @Override
    public void hide() {}

    /**
     * Disposes screen data
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}




