package com.mygdx.pirategame.screens;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.PirateGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Main menu is the first screen the player sees. Allows them to navigate where they want to go to
 *
 * @author Sam Pearson
 * @version 1.1
 */
public class MainMenu implements Screen {

    private final PirateGame parent;
    private final Stage stage;
    private final TextureRegion background = new TextureRegion(new Texture("map2.png"));

    /**
     * Instantiates a new Main menu.
     *
     * @param PirateGame the main starting body of the game. Where screen swapping is carried out.
     */
    public MainMenu(PirateGame PirateGame){

        parent = PirateGame;
        stage = new Stage(new ScreenViewport());

    }

    /**
     * Displays the Main Menu
     */
    @Override
    public void show() {

        // Set the input processor
        Gdx.input.setInputProcessor(stage);
        // Create a table for the buttons

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // The skin for the actors
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Create buttons
        TextButton newGame = new TextButton("New Game", skin);
        TextButton LoadSaved = new TextButton("Load Saved Game", skin);
        TextButton help = new TextButton("Help", skin);
        TextButton options = new TextButton("Options", skin);
        TextButton exit = new TextButton("Exit", skin);

        // Add buttons to table
        table.add(newGame).fillX().uniformX();
        table.row();
        table.add(LoadSaved).fillX().uniformX();
        table.row();
        table.add(help).fillX().uniformX();
        table.row();
        table.add(options).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        // Add listeners to the buttons

        // Start a game
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                //parent.changeScreen(GAME);
                parent.changeScreen(LOADING, false);
            }
        });

        // Help Screen
        help.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.changeScreen(HELP, false);
            }
        });

        // Go to edit options
        options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.setScreen(new Options(parent,parent.getScreen()));
            }
        });

        // Load a Saved game
        LoadSaved.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.load();
            }
        });

        // Quit game
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

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
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
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
     *
     * Pauses the Main Menu
     */
    @Override
    public void pause() {}

    /**
     * (Not Used)
     *
     * Resumes the Main Menu
     */
    @Override
    public void resume() {}

    /**
     * (Not Used)
     *
     * Hides the Main Menu
     */
    @Override
    public void hide() {}

    /**
     * Disposes the Main Menu data
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

}




