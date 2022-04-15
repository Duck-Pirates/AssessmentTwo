package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Hud
 * Produces a hud for the player
 *
 *@author Ethan Alabaster, Sam Pearson
 *@version 1.0
 */
public class Hud implements Disposable {
    public static Stage stage;

    private Viewport viewport;

    private GameScreen screen;

    private float timeCount;
    private float Constant_timeCount;
    public static Integer score;
    public static Integer health;
    private Texture hp;
    private Texture boxBackground;
    private Texture coinPic;
    private static Texture PowerupPic;

    private static Label scoreLabel;
    private static Label healthLabel;
    private static Label coinLabel;
    private static Label pointsText;
    //private static Label powerupLabel;
    public static Integer coins;
    private static Integer coinMulti;

    public static Boolean PowerupTimerBool = Boolean.FALSE;
    private static float PowerupTimer;
    private static Integer PowerUpType;

    private Image hpImg;
    private Image box;
    private Image coin;
    private static Image powerUp;



    /**
     * Retrieves information and displays it in the hud
     * Adjusts hud with viewport
     *
     * @param sb Batch of images used in the hud
     */
    public Hud(SpriteBatch sb, GameScreen screen) {
        this.screen = screen;
        health = screen.difficulty.getHP();
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
        if (screen.difficulty == Difficulty.EASY){
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

        //table2.add(powerUp).width(40).height(40).padTop(40).padRight(60);


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
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float dt) {
        timeCount += dt;
        Constant_timeCount += dt;
        //Gdx.app.log("dt", String.valueOf(dt));
        //Gdx.app.log("time", String.valueOf(Constant_timeCount));
        coinLabel.setText(String.format("%03d", coins));
        if(timeCount >= 1) {
            //Regen health every second
            if(health != this.screen.difficulty.getHP()) {
                health += 1;
                healthLabel.setText(String.format("%02d", health));
            }
            //Gain point every second
            score += 5;
            scoreLabel.setText(String.format("%03d", score));
            timeCount = 0;


            //Check if a points boundary is met
            SkillTree.pointsCheck(score, coins);
            //Gdx.app.log("dt", String.valueOf(dt));
            //Gdx.app.log("time", String.valueOf(timeCount));
            // PowerUp
        }

        // TODO Fix error of powerups not lasting the full 15 seconds, after picking up a new powerup
        // TODO maybe make it not possible to pick up a new powerup... basicaly like a cool down timer
        if (PowerupTimerBool == Boolean.TRUE){
            if (PowerupTimer == 0f){
                PowerupTimer = Constant_timeCount;
            }
            Integer PowerupTime = 15;
            if (Constant_timeCount > PowerupTimer + PowerupTime){
                // Remove powerup abilities
                // Respawn that powerup
                screen.difficulty.PreviousPowerupStats();
                PowerupPic = new Texture("blank.png");
                powerUp = new Image(PowerupPic);
                powerUp.setFillParent(false);
                powerUp.setScale(0.3f,0.3f );
                stage.addActor(powerUp);
                PowerupTimer = 0f;
                PowerupTimerBool = Boolean.FALSE;
                Gdx.app.log("dt", "Back to normal");
            }
        }
    }





    /**
     * Changes health by value increase
     *
     * @param value Increase to health
     */
    public static void changeHealth(int value) {

        if (health + value > GameScreen.difficulty.getHP()){
            health = GameScreen.difficulty.getHP();
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
        //Check if a points boundary is met
        SkillTree.pointsCheck(score, coins);
    }

    /**
     *
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
        PowerupTimerBool = Boolean.TRUE;

    }

    public static void SubtractCoin(Integer value){
        coins -= value;
    }




    /**
     * Changes health by value factor
     *
     * @param value Factor of coin increase
     */
    public static void changeCoinsMulti(int value) {
        coinMulti = coinMulti * value;
    }

    /**
     * Changes the camera size, Scales the hud to match the camera
     *
     * @param width the width of the viewable area
     * @param height the height of the viewable area
     */
    public static void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    /**
     * Returns health value
     *
     * @return health : returns health value
     */
    public static Integer getHealth(){
        return health;
    }

    /**
     * (Not Used)
     * Returns coins value
     *
     * @return health : returns coins value
     */
    public static Integer getCoins(){
        return coins;
    }

    /**
     * Disposes game data
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
