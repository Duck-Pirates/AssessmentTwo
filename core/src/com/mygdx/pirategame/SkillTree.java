package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


import java.util.ArrayList;
import java.util.List;

/**
 * The type for the skill tree screen.
 * It is a visual representation for the skills that the game automatically unlocks for the player.
 * Automatically unlocked when a points threshold is reached
 *
 * @author Sam Pearson
 * @version 1.0
 */
public class SkillTree implements Screen {

    private final PirateGame parent;
    private final Stage stage;

    //To store whether buttons are enabled or disabled
    private static final List<Integer> states = new ArrayList<Integer>();

    private static TextButton movement1;
    private static TextButton movement2;
    private static TextButton movement3;
    private static TextButton traverse1;
    private static TextButton traverse2;
    private static TextButton damage1;
    private static TextButton damage2;
    private static TextButton damage3;
    private static TextButton GoldMulti1;
    private static TextButton GoldMulti2;
    private static TextButton armour1;
    private static TextButton armour2;
    private static TextButton armour3;
    private static TextButton cone;

    /**
     * Instantiates a new Skill tree.
     *
     * @param pirateGame the main starting body of the game. Where screen swapping is carried out.
     */
//In the constructor, the parent and stage are set. Also the states list is set
    public SkillTree(PirateGame pirateGame){
        parent = pirateGame;
        stage = new Stage(new ScreenViewport());

        //0 = enabled, 1 = disabled
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);
        states.add(1);

    }
    /**
     * What should be displayed on the skill tree screen
     *
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

        //create skill tree buttons
        //Sets enabled or disabled
        movement1 = new TextButton("Speed + 5%", skin);
        if (states.get(0) == 1){
            movement1.setDisabled(true);
        }
        movement2 = new TextButton("Speed + 10%", skin);
        if (states.get(0) == 1){
            movement2.setDisabled(true);
        }
        movement3 = new TextButton("Speed + 15%", skin);
        if (states.get(0) == 1){
            movement3.setDisabled(true);
        }

        traverse1 = new TextButton("Traverse + 10%", skin);
        if (states.get(0) == 1){
            traverse1.setDisabled(true);
        }
        traverse2 = new TextButton("Traverse + 20%", skin);
        if (states.get(0) == 1){
            traverse2.setDisabled(true);
        }

        damage1 = new TextButton("Damage + 5", skin);
        if (states.get(3) == 1){
            damage1.setDisabled(true);
        }
        damage2 = new TextButton("Damage + 10", skin);
        if (states.get(3) == 1){
            damage2.setDisabled(true);
        }
        damage3 = new TextButton("Damage + 20", skin);
        if (states.get(3) == 1){
            damage3.setDisabled(true);
        }

        armour1 = new TextButton("Armour + 5%", skin);
        if (states.get(3) == 1){
            armour1.setDisabled(true);
        }
        armour2 = new TextButton("Armour + 10%", skin);
        if (states.get(3) == 1){
            armour2.setDisabled(true);
        }
        armour3 = new TextButton("Armour + 20%", skin);
        if (states.get(3) == 1){
            armour3.setDisabled(true);
        }

        GoldMulti1 = new TextButton("Gold Multiplier x2", skin);
        if (states.get(1) == 1){
            GoldMulti1.setDisabled(true);
        }
        GoldMulti2 = new TextButton("Gold Multiplier x3", skin);
        if (states.get(1) == 1){
            GoldMulti2.setDisabled(true);
        }

        cone = new TextButton("Cone Shot", skin);
        if (states.get(1) == 1){
            cone.setDisabled(true);
        }



        //Point unlock labels
        //TODO Made seperate labels for points and cost


        final Label unlockmovement1 = new Label("Required: 100 points \n Costs: 50 gold",skin);
        final Label unlockmovement2 = new Label("Required: 100 points \n Costs: 400 gold",skin);
        final Label unlockmovement3 = new Label("Required: 100 points \n Costs: 1000 gold",skin);
        final Label unlocktraverse1 = new Label("Required: 100 points \n Costs: 50 gold",skin);
        final Label unlocktraverse2 = new Label("Required: 100 points \n Costs: 250 gold",skin);
        final Label unlockdamage1 = new Label("Required: 100 points \n Costs: 100 gold",skin);
        final Label unlockdamage2 = new Label("Required: 100 points \n Costs: 350 gold",skin);
        final Label unlockdamage3 = new Label("Required: 100 points \n Costs: 800 gold",skin);
        final Label unlockgoldmulti1 = new Label("Required: 100 points \n Costs: 500 gold",skin);
        final Label unlockgoldmulti2 = new Label("Required: 100 points \n Costs: 1000 gold",skin);
        final Label unlockarmour1 = new Label("Required: 100 points \n Costs: 50 gold",skin);
        final Label unlockarmour2 = new Label("Required: 100 points \n Costs: 300 gold",skin);
        final Label unlockarmour3 = new Label("Required: 100 points \n Costs: 700 gold",skin);
        final Label unlockcone = new Label("Required: 1000 points \n Costs: 1000 gold",skin);




        //Return Button
        TextButton backButton = new TextButton("Return", skin);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                parent.changeScreen(PirateGame.GAME); //Return to game
            }
        });

        //add buttons and labels to main table
        table.add(movement1);
        table.add(unlockmovement1);
        //table.row().pad(10, 0, 10, 0);
        table.add(movement2);
        table.add(unlockmovement2);
        //table.row().pad(10, 0, 10, 0);
        table.add(movement3);
        table.add(unlockmovement3);
        table.row().pad(10, 0, 10, 0);

        table.add(traverse1);
        table.add(unlocktraverse1 );
        //table.row().pad(10, 0, 10, 0);
        table.add(traverse2);
        table.add(unlocktraverse2);
        table.row().pad(10, 0, 10, 0);

        table.add(damage1);
        table.add(unlockdamage1);
        //table.row().pad(10, 0, 10, 0);
        table.add(damage2);
        table.add(unlockdamage2);
        //table.row().pad(10, 0, 10, 0);
        table.add(damage3);
        table.add(unlockdamage3);
        table.row().pad(10, 0, 10, 0);

        table.add(GoldMulti1);
        table.add(unlockgoldmulti1);
        //table.row().pad(10, 0, 10, 0);
        table.add(GoldMulti2);
        table.add(unlockgoldmulti2);
        table.row().pad(10, 0, 10, 0);

        table.add(armour1);
        table.add(unlockarmour1);
        //table.row().pad(10, 0, 10, 0);
        table.add(armour2);
        table.add(unlockarmour2);
        //table.row().pad(10, 0, 10, 0);
        table.add(armour3);
        table.add(unlockarmour3);
        table.row().pad(10, 0, 10, 0);

        table.add(cone);
        table.add(unlockcone);
        table.row().pad(10, 0, 10, 0);


        //table.top();

        //add return button
        Other.add(backButton);
        Other.bottom().left();
    }

    /**
     * Allows the game to check whether a points threshold has been reached
     *
     * @param points the current amount of points
     */
    public static void pointsCheck(int points, Integer coins){

        //TODO Requires certain amount of gold and then remove that amout of gold
        //Cost of


        //States.get() checks whether it has already been unlocked. 1 = not unlocked, 0 = unlocked
        if(states.get(0) == 1 && points >= 100 && coins >= 50){ // Movement 1 (5% speed)
            //Change acceleration
            GameScreen.changeAcceleration(20F);
            //Change Max speed
            GameScreen.changeMaxSpeed(20F);
            states.set(0, 0);
        }
        else if(states.get(1) == 1 && points >= 100 && coins >= 400){ // movement 2 (10% speed)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(2) == 1 && points >= 200 && coins >= 1000){ // movement 3 (15% speed)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(3) == 1 && points >= 200 && coins >= 50){ // traverse 1 (10% traverse)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(4) == 1 && points >= 200 && coins >= 250){ // traverse 2 (20% traverse)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(5) == 1 && points >= 200 && coins >= 100){ // damage 1 (5 damage)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(6) == 1 && points >= 200 && coins >= 350){ // damage 1 (10 damage)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(7) == 1 && points >= 200 && coins >= 800){ // damage 1 (20 damage)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(11) == 1 && points >= 200 && coins >= 500){ // gold multiplier 1 (x2 gold)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(12) == 1 && points >= 200 && coins >= 1000){ // gold multiplier 2 (x3 gold)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(8) == 1 && points >= 200 && coins >= 50){ // armour 1 (5% armour )
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(9) == 1 && points >= 200 && coins >= 300){ // armour 2 (10% armour)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(10) == 1 && points >= 200 && coins >= 700){ // armour 3 (20% armour)
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }
        else if(states.get(13) == 1 && points >= 200 && coins >= 1000){ // coneshot
            //Change multiplier
            Hud.changeCoinsMulti(2);
            states.set(1, 0);
        }


        //Hud.changeCoinsMulti(2);
        //Change acceleration
        //GameScreen.changeAcceleration(20F);
        //Change Max speed
        //GameScreen.changeMaxSpeed(20F);
        //GameScreen.changeDamage(5);
    }

    /**
     * Renders the visual data for all objects
     * @param delta Delta Time
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
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
        Gdx.app.log("w", String.valueOf(width));
        Gdx.app.log("h", String.valueOf(height));
        damage1.setScale(width,height);
        stage.draw();
    }


    /**
     * (Not Used)
     * Pauses game
     */
    @Override
    public void pause() {
    }
    /**
     * (Not Used)
     * Resumes game
     */
    @Override
    public void resume() {
    }
    /**
     * (Not Used)
     * Hides game
     */
    @Override
    public void hide() {
    }
    /**
     * Disposes game data
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}




