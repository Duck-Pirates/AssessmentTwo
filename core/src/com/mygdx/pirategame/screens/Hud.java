package com.mygdx.pirategame.screens;

import com.mygdx.pirategame.configs.Difficulty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Hud
 * Produces a hud for the player
 *
 * @author Ethan Alabaster, Sam Pearson
 * @version 1.0
 */
public class Hud implements Disposable {

    public static Stage stage;

    public Viewport viewport;

    public GameScreen screen;

    public float timeCount;
    public float Constant_timeCount;
    public float hpConstant_Time;
    public static Integer score;
    public static Integer health;
    public Texture hp;
    public Texture boxBackground;
    public Texture coinPic;
    public static Texture PowerupPic;

    public static Label scoreLabel;
    public static Label healthLabel;
    public static Label coinLabel;
    public static Label pointsText;
    public static Integer coins;
    public static Integer coinMulti;

    public static Boolean PowerupTimerBool = false;
    public static float PowerupTimer;
    public static Integer PowerUpType;

    public Image hpImg;
    public Image box;
    public Image coin;
    public static Image powerUp;


    /**
     * Retrieves information and displays it in the hud
     * Adjusts hud with viewport
     *
     * @param sb Batch of images used in the hud
     */
    public Hud(SpriteBatch sb, GameScreen screen) {

        this.screen = screen;

        health = GameScreen.getDifficulty().getHP();
        score = 0;
        coins = 0;
        coinMulti = 1;

        //Set images
        hp = new Texture("hp.png");
        boxBackground = new Texture("hudBG.png");
        coinPic = new Texture("coin.png");
        PowerupPic = new Texture("blank.png");

        hpImg = new Image(hp);
        box = new Image(boxBackground);
        coin = new Image(coinPic);
        powerUp = new Image(PowerupPic);


        viewport = new ScreenViewport();
        stage = new Stage(viewport, sb);

        //Creates tables
        Table table1 = new Table(); //Counters
        Table table2 = new Table(); //Pictures or points label
        Table table3 = new Table(); //Background

        table1.top().right();
        table1.setFillParent(true);
        table2.top().right();
        table2.setFillParent(true);
        table3.top().right();
        table3.setFillParent(true);

        powerUp.setFillParent(false);
        powerUp.setScale(0.3f,0.3f );


        scoreLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        if (GameScreen.getDifficulty() == Difficulty.EASY){
            healthLabel = new Label(String.format("%03d", health), new Label.LabelStyle(new BitmapFont(), Color.RED));
        }
        else{
            healthLabel = new Label(String.format("%02d", health), new Label.LabelStyle(new BitmapFont(), Color.RED));
        }
        coinLabel = new Label(String.format("%03d", coins), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        pointsText = new Label("Points:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        table3.add(box).width(160).height(160).padBottom(15).padLeft(30);
        table2.add(hpImg).width(32).height(32).padTop(16).padRight(90);
        table2.row();
        table2.add(coin).width(32).height(32).padTop(8).padRight(90);
        table2.row();
        table2.add(pointsText).width(32).height(32).padTop(6).padRight(90);
        table2.row();

        table1.add(healthLabel).padTop(20).top().right().padRight(40);
        table1.row();
        table1.add(coinLabel).padTop(20).top().right().padRight(40);
        table1.row();
        table1.add(scoreLabel).padTop(22).top().right().padRight(40);

        stage.addActor(table3);
        stage.addActor(table2);
        stage.addActor(table1);
        stage.addActor(powerUp);

        PowerupTimer = 0f;

    }

    /**
     * Updates the state of the hud
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void update(float delta) {

        timeCount += delta;
        Constant_timeCount += delta;
        hpConstant_Time += delta;

        coinLabel.setText(String.format("%03d", coins));

        if(timeCount >= 1) {

            // Regen health every second
            if(hpConstant_Time >= 3) {
                if (health != GameScreen.getDifficulty().getHP()) {
                    health += 1;
                    healthLabel.setText(String.format("%02d", health));
                    hpConstant_Time = 0;
                }
            }
            // Gain point every second
            score += 5;
            scoreLabel.setText(String.format("%03d", score));
            timeCount = 0;

            // Check if a point's boundary is met
            Shop.pointsCheck(score, coins);

        }

        if (PowerupTimerBool){ // This checks if a powerup has been picked up
            if (PowerupTimer == 0f){
                PowerupTimer = Constant_timeCount;
            }
            int PowerupTime = 15;
            if (Constant_timeCount > PowerupTimer + PowerupTime){
                // Remove powerup abilities
                // Respawn that powerup
                GameScreen.getDifficulty().previousPowerupStats();
                PowerupPic = new Texture("blank.png");
                powerUp = new Image(PowerupPic);
                powerUp.setFillParent(false);
                powerUp.setScale(0.3f,0.3f );
                stage.addActor(powerUp);
                PowerupTimer = 0f;
                PowerupTimerBool = false;
            }
        }

    }

    /**
     * (Not Used)
     *
     * Disposes HUD data
     */
    @Override
    public void dispose() {}

    /**
     * Changes the camera size, Scales the hud to match the camera
     *
     * @param width the width of the viewable area
     * @param height the height of the viewable area
     */
    public static void resize(int width, int height){ stage.getViewport().update(width, height, true); }

    public static Integer getHealth(){
        return health;
    }

    public static Integer getCoins(){
        return coins;
    }

    /**
     * Changes health by value increase
     *
     * @param value Increase to health
     */
    public static void changeHealth(int value) {

        if (health + value > GameScreen.getDifficulty().getHP()){
            health = GameScreen.getDifficulty().getHP();
        }else{
            health += value;
        }
        healthLabel.setText(String.format("%02d", health));

    }

    /**
     * Changes coins by value increase
     *
     * @param value Increase to coins
     */
    public static void changeCoins(int value) {

        if (value > 0) {
            coins += value * coinMulti;
            coinLabel.setText(String.format("%03d", coins));
        }

    }

    /**
     * Changes points by value increase
     *
     * @param value Increase to points
     */
    public static void changePoints(int value) {

        score += value;
        scoreLabel.setText(String.format("%03d", score));
        //Check if a point's boundary is met
        Shop.pointsCheck(score, coins);

    }

    /**
     * Changes the Powerup image
     *
     * @param type The Powerup Type
     */
    public static void ChangePowerUpImage(Integer type){

        PowerUpType= type;
        Gdx.app.log("time", String.valueOf(type));
        if (PowerUpType == 0) {
            PowerupPic = new Texture("Ammo.png");

        } else if (PowerUpType == 1){
            PowerupPic = new Texture("Lightning.png");
        } else if (PowerUpType == 2){
            PowerupPic = new Texture("Money.png");
        } else if (PowerUpType == 3){
            PowerupPic = new Texture("Repair.png");
        } else if (PowerUpType == 4){
            PowerupPic = new Texture("Star.png");
        }

        powerUp = new Image(PowerupPic);
        //
        //powerUp.toFront();
        powerUp.setFillParent(false);
        powerUp.setFillParent(false);
        //Size of powerup On screen
        powerUp.setScale(0.3f,0.3f );

        //powerUp.setScale(Gdx.graphics.getWidth()*03f,Gdx.graphics.getHeight() *3f );
        stage.addActor(powerUp);
        PowerupTimerBool = true;

    }

    public static void SubtractCoin(Integer value){
        coins -= value;
    }

}

