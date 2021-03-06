package com.mygdx.pirategame.screens;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.configs.Difficulty;

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
 * Loading Menu
 * This is the menu that the player sees whenever they try to create a new game
 *
 * @author Benjamin Whitnell
 * @version 1.0
 */
public class LoadingMenu implements Screen {

    private final PirateGame parent;
    private final Stage stage;
    private final TextureRegion background = new TextureRegion(new Texture("map2.png"));

    /**
     * Instantiates a new Main menu.
     *
     * @param PirateGame the main starting body of the game. Where screen swapping is carried out.
     */
    public LoadingMenu(PirateGame PirateGame){

        parent = PirateGame;
        stage = new Stage(new ScreenViewport());

    }

    /**
     * Display the options screen
     */
    @Override
    public void show() {

        // Set the input processor
        Gdx.input.setInputProcessor(stage);

        // Create a table for the buttons
        Table table = new Table();
        Table table2 = new Table();
        table.setFillParent(true);
        table2.bottom().left();
        stage.addActor(table);
        stage.addActor(table2);

        // The skin for the actors
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Create buttons
        TextButton EASY = new TextButton("Game Mode: Easy", skin);
        TextButton MEDIUM = new TextButton("Game Mode: Medium", skin);
        TextButton HARD = new TextButton("Game Mode: Hard", skin);

        TextButton exit = new TextButton("Exit", skin);
        TextButton backButton = new TextButton("Return", skin);

        // Add buttons to table
        table.add(EASY).fillX().uniformX();
        table.row();
        table.add(MEDIUM).fillX().uniformX();
        table.row();
        table.add(HARD).fillX().uniformX();
        table.row().pad(20, 0, 10, 0);
        table.add(exit).fillX().uniformX();

        table2.add(exit).fillX().uniformX();
        table2.row();
        table2.add(backButton).fillX().uniformX();


        Label Controls1 = new Label("WASD to move", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label Controls2 = new Label("SPACE to fire", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label Controls3 = new Label("ESCAPE to see menu", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label objective1 = new Label("The objective is to take over or destroy all other colleges", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label objective2 = new Label("Destroy the college flag with cannons", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label objective3 = new Label("Collect coins on the way", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label skillInfo1 = new Label("Use the coins you earn to unlock abilities along the way", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label skillInfo2 = new Label("See your upgrades in the skills tab", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Label powerupInfo1 = new Label("Collect power-ups along the way to give you temporary abilities", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label powerupInfo2 = new Label("Power-ups include: Increased Speed, Health Regeneration,", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label powerupInfo3 = new Label("Temporary Immunity, Increased Damage and Increased Coin Earnings", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.row();
        table.add(Controls1);
        table.row();
        table.add(Controls2);
        table.row();
        table.add(Controls3).padBottom((40));
        table.row();
        table.add(objective1);
        table.row();
        table.add(objective2);
        table.row();
        table.add(objective3).padBottom((40));
        table.row();
        table.add(skillInfo1);
        table.row();
        table.add(skillInfo2).padBottom((40));
        table.row();
        table.add(powerupInfo1);
        table.row();
        table.add(powerupInfo2);
        table.row();
        table.add(powerupInfo3).padBottom((40));
        table.center();

        // Add listeners to the buttons

        //Set difficulty
        EASY.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                PirateGame.setDifficulty(Difficulty.EASY);
                parent.changeScreen(GAME, true);
            }
        });

        MEDIUM.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
            	PirateGame.setDifficulty(Difficulty.MEDIUM);
                parent.changeScreen(GAME, true);
            }
        });

        HARD.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
            	PirateGame.setDifficulty(Difficulty.HARD);
                parent.changeScreen(GAME, true);
            }
        });


        //Quit game
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MENU, false);
            }
        });

    }

    /**
     * Renders the visual data for all objects
     * @param delta Time elapsed
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.draw();

    }

    /**
     * Changes the camera size, Scales the loading menu to match the camera
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
     *
     * Pauses the loading menu
     */
    @Override
    public void pause() {}

    /**
     * (Not Used)
     *
     * Resumes the loading menu
     */
    @Override
    public void resume() {}

    /**
     * (Not Used)
     *
     * Hides the loading menu
     */
    @Override
    public void hide() {}

    /**
     * Disposes the loading menu data
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}




