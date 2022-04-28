package com.mygdx.pirategame.screens;

import static com.mygdx.pirategame.configs.Constants.GAME;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.configs.Difficulty;

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

    private Texture hp;
    private Texture boxBackground;
    private Texture coinPic;
    private static Integer score;
    public static Integer health;
    private static Label scoreLabel;
    private static Label healthLabel;
    private static Label coinLabel;
    private static Label pointsText;
    private static Label powerupLabel;
    private static Integer coins;
    private static Integer coinMulti;
    private Image hpImg;
    private com.badlogic.gdx.scenes.scene2d.ui.Image box;
    private Image coin;


    //To store whether buttons are enabled or disabled
    public static ArrayList<Integer> states = new ArrayList<Integer>();

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






    private static Integer Speed1Cost = 5;
    private static Integer Speed1Points = 100;
    private static Integer Speed2Cost = 15;
    private static Integer Speed2Points = 400;
    private static Integer Speed3Cost = 30;
    private static Integer Speed3Points = 800;

    private static Integer Traverse1Cost = 10;
    private static Integer Traverse1Points = 400;
    private static Integer Traverse2Cost = 25;
    private static Integer Traverse2Points = 800;

    private static Integer Damage1Cost = 5;
    private static Integer Damage1Points = 100;
    private static Integer Damage2Cost = 15;
    private static Integer Damage2Points = 400;
    private static Integer Damage3Cost = 30;
    private static Integer Damage3Points = 800;

    private static Integer Gold1Cost = 10;
    private static Integer Gold1Points = 500;
    private static Integer Gold2Cost = 25;
    private static Integer Gold2Points = 1000;

    private static Integer Armour1Cost = 5;
    private static Integer Armour1Points = 200;
    private static Integer Armour2Cost = 15;
    private static Integer Armour2Points = 500;
    private static Integer Armour3Cost = 30;
    private static Integer Armour3Points = 1000;

    private static Integer Cone1Cost = 20;
    private static Integer Cone2Points = 1000;





    private final TextureRegion background = new TextureRegion(new Texture("map2.png"));;


    /**
     * Instantiates a new Skill tree.
     *
     * @param pirateGame the main starting body of the game. Where screen swapping is carried out.
     */
//In the constructor, the parent and stage are set. Also the states list is set
    public SkillTree(PirateGame pirateGame){
        parent = pirateGame;
        stage = new Stage(new ScreenViewport());

        //0 = enabled, 1 = disabled, 2 = unlocked
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
        // Fills the parent
        table.setFillParent(true);



        // Table for the return button
        final Table Other = new Table();
        Other.setFillParent(true);
        stage.addActor(Other);


        //The skin for the actors
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //create skill tree buttons
        //Sets enabled or disabled

        //TODO Display total coins and points in the shop

        //Creates tables

        hp = new Texture("hp.png");
        boxBackground = new Texture("hudBG.png");
        coinPic = new Texture("coin.png");
        coin = new Image(coinPic);
        hpImg = new Image(hp);
        box = new Image(boxBackground);


        Table table1 = new Table(); //Counters
        Table table2 = new Table(); //Pictures or points label
        Table table3 = new Table(); //Background

        table1.top().right();
        table1.setFillParent(true);
        table2.top().right();
        table2.setFillParent(true);
        table3.top().right();
        table3.setFillParent(true);

        scoreLabel = new Label(String.format("%03d", Hud.score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        if (GameScreen.difficulty == Difficulty.EASY){
            healthLabel = new Label(String.format("%03d", Hud.health), new Label.LabelStyle(new BitmapFont(), Color.RED));
        }
        else{
            healthLabel = new Label(String.format("%02d", Hud.health), new Label.LabelStyle(new BitmapFont(), Color.RED));
        }
        coinLabel = new Label(String.format("%03d", Hud.coins), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
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

        //TODO If brought hide the button?? (kinda done)

        movement1 = new TextButton("Speed + 5%", skin);
        //Gdx.app.log("Movement1", String.valueOf(states.get(0)));
        if (states.get(0) == 1 || states.get(0) == 2){
            movement1.setDisabled(true);
        } else if (states.get(0) == 0){
            movement1.setDisabled(false);
            //Gdx.app.log("Movement1", "Brought");
            movement1.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseMaxSpeedPercent(5);
                    states.set(0, 2);
                    movement1.setDisabled(true);
                    Hud.SubtractCoin(Speed1Cost);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }


        movement2 = new TextButton("Speed + 10%", skin);
        if (states.get(1) == 1 || states.get(1) == 2){
            movement2.setDisabled(true);
        } else if (states.get(1) == 0){
            movement2.setDisabled(false);
            movement2.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseMaxSpeedPercent(10);
                    states.set(1, 2);
                    Hud.SubtractCoin(Speed2Cost);
                    movement2.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }


        movement3 = new TextButton("Speed + 15%", skin);
        if (states.get(2) == 1 || states.get(2) == 2){
            movement3.setDisabled(true);
        } else if (states.get(2) == 0){
            movement3.setDisabled(false);
            movement3.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseMaxSpeedPercent(15);
                    states.set(2, 2);
                    Hud.SubtractCoin(Speed3Cost);
                    movement3.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }


        traverse1 = new TextButton("Traverse + 10%", skin);
        if (states.get(3) == 1 || states.get(3) == 2){
            traverse1.setDisabled(true);
        } else if (states.get(3) == 0){
            traverse1.setDisabled(false);
            traverse1.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseTraversePercent(10);
                    states.set(3, 2);
                    Hud.SubtractCoin(Traverse1Cost);
                    traverse1.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }
        traverse2 = new TextButton("Traverse + 20%", skin);
        if (states.get(4) == 1 || states.get(4) == 2){
            traverse2.setDisabled(true);
        } else if (states.get(4) == 0){
            traverse2.setDisabled(false);
            traverse2.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseTraversePercent(20);
                    states.set(4, 2);
                    Hud.SubtractCoin(Traverse2Cost);
                    traverse2.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }

        damage1 = new TextButton("Damage + 5", skin);
        if (states.get(5) == 1 || states.get(5) == 2){
            damage1.setDisabled(true);
        } else if (states.get(5) == 0){
            damage1.setDisabled(false);
            damage1.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseDamageDealtPercent(5);
                    states.set(5, 2);
                    Hud.SubtractCoin(Damage1Cost);
                    damage1.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }
        damage2 = new TextButton("Damage + 10", skin);
        if (states.get(6) == 1 || states.get(6) == 2){
            damage2.setDisabled(true);
        } else if (states.get(6) == 0){
            damage2.setDisabled(false);
            damage2.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseDamageDealtPercent(10);
                    states.set(6, 2);
                    Hud.SubtractCoin(Damage2Cost);
                    damage2.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }
        damage3 = new TextButton("Damage + 20", skin);
        if (states.get(7) == 1 || states.get(7) == 2){
            damage3.setDisabled(true);
        } else if (states.get(7) == 0){
            damage3.setDisabled(false);
            damage3.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseDamageDealtPercent(20);
                    states.set(7, 2);
                    Hud.SubtractCoin(Damage3Cost);
                    damage3.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }

        armour1 = new TextButton("Armour + 5%", skin);
        if (states.get(8) == 1 || states.get(8) == 2){
            armour1.setDisabled(true);
        } else if (states.get(8) == 0){
            armour1.setDisabled(false);
            armour1.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.DecreaseDamageRecievedPercent(14);
                    states.set(8, 2);
                    Hud.SubtractCoin(Armour1Cost);
                    armour1.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }
        armour2 = new TextButton("Armour + 10%", skin);
        if (states.get(9) == 1 || states.get(9) == 2){
            armour2.setDisabled(true);
        } else if (states.get(9) == 0){
            armour2.setDisabled(false);
            armour2.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.DecreaseDamageRecievedPercent(12);
                    states.set(9, 2);
                    Hud.SubtractCoin(Armour2Cost);
                    armour2.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }
        armour3 = new TextButton("Armour + 20%", skin);
        if (states.get(10) == 1 || states.get(10) == 2){
            armour3.setDisabled(true);
        } else if (states.get(10) == 0){
            armour3.setDisabled(false);
            armour3.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.DecreaseDamageRecievedPercent(8);
                    states.set(10, 2);
                    Hud.SubtractCoin(Armour3Cost);
                    armour3.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }

        GoldMulti1 = new TextButton("Gold Multiplier x2", skin);
        if (states.get(11) == 1 || states.get(11) == 2){
            GoldMulti1.setDisabled(true);
        } else if (states.get(11) == 0){
            GoldMulti1.setDisabled(false);
            GoldMulti1.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseCoinMulti(1);
                    states.set(11, 2);
                    Hud.SubtractCoin(Gold1Cost);
                    GoldMulti1.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }
        GoldMulti2 = new TextButton("Gold Multiplier x3", skin);
        if (states.get(12) == 1 || states.get(12) == 2){
            GoldMulti2.setDisabled(true);
        } else if (states.get(12) == 0){
            GoldMulti2.setDisabled(false);
            GoldMulti2.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.difficulty.IncreaseCoinMulti(1);
                    states.set(12, 2);
                    Hud.SubtractCoin(Gold2Cost);
                    GoldMulti2.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }

        cone = new TextButton("Cone Shot", skin);
        if (states.get(13) == 1 || states.get(13) == 2){
            cone.setDisabled(true);
        } else if (states.get(13) == 0){
            cone.setDisabled(false);
            cone.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //TODO insert cone shoot function
                    GameScreen.difficulty.SetConeMec(true);
                    states.set(13, 2);
                    Hud.SubtractCoin(Cone1Cost);
                    GoldMulti1.setDisabled(true);
                    coinLabel.setText(String.format("%03d", Hud.coins));
                }
            });
        }



        //Point unlock labels



        final Label unlockmovement1 = new Label("Required: " + Integer.toString(Speed1Points) + " points \n Costs: " + Integer.toString(Speed1Cost) + " gold",skin);
        final Label unlockmovement2 = new Label("Required: " + Integer.toString(Speed2Points) + " points \n Costs: " + Integer.toString(Speed2Cost) + " gold",skin);
        final Label unlockmovement3 = new Label("Required: " + Integer.toString(Speed3Points) + " points \n Costs: " + Integer.toString(Speed3Cost) + " gold",skin);
        final Label unlocktraverse1 = new Label("Required: " + Integer.toString(Traverse1Points) + " points \n Costs: " + Integer.toString(Traverse1Cost) + " gold",skin);
        final Label unlocktraverse2 = new Label("Required: " + Integer.toString(Traverse2Points) + " points \n Costs: " + Integer.toString(Traverse2Cost) + " gold",skin);
        final Label unlockdamage1 = new Label("Required: " + Integer.toString(Damage1Points) + " points \n Costs: " + Integer.toString(Damage1Cost) + " gold",skin);
        final Label unlockdamage2 = new Label("Required: " + Integer.toString(Damage2Points) + " points \n Costs: " + Integer.toString(Damage2Cost) + " gold",skin);
        final Label unlockdamage3 = new Label("Required: " + Integer.toString(Damage3Points) + " points \n Costs: " + Integer.toString(Damage3Cost) + " gold",skin);
        final Label unlockgoldmulti1 = new Label("Required: " + Integer.toString(Gold1Points) + " points \n Costs: " + Integer.toString(Gold1Cost) + " gold",skin);
        final Label unlockgoldmulti2 = new Label("Required: " + Integer.toString(Gold2Points) + " points \n Costs: " + Integer.toString(Gold2Cost) + " gold",skin);
        final Label unlockarmour1 = new Label("Required: " + Integer.toString(Armour1Points) + " points \n Costs: " + Integer.toString(Armour1Cost) + " gold",skin);
        final Label unlockarmour2 = new Label("Required: " + Integer.toString(Armour2Points) + " points \n Costs: " + Integer.toString(Armour2Cost) + " gold",skin);
        final Label unlockarmour3 = new Label("Required: " + Integer.toString(Armour3Points) + " points \n Costs: " + Integer.toString(Armour3Cost) + " gold",skin);
        final Label unlockcone = new Label("Required: " + Integer.toString(Cone2Points) + " points \n Costs: " + Integer.toString(Cone1Cost) + " gold",skin);




        //Return Button
        TextButton backButton = new TextButton("Return", skin);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                parent.changeScreen(GAME, false); //Return to game
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

        table.toFront();
        stage.addActor(table);
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

        //TODO Requires certain amount of gold and then remove that amount of gold
        //TODO Link items in store... can only buy *3 after buying *2
        //TODO, if they no longer have enough money, they can't buy items?? or items are no longer visible
        //states.get(0) == 2 ==== purchased???


        //States.get() checks whether it has already been unlocked. 1 = not unlocked, 0 = unlocked, 2 = purchased
        if(states.get(0) == 1 && points >= Speed1Points && coins >= Speed1Cost ){ // Movement 1 (5% speed)
            //GameScreen.difficulty.IncreaseMaxSpeedPercent(5);
            states.set(0, 0);
        }
        if(states.get(1) == 1 && states.get(0) == 2 && points >= Speed2Points && coins >= Speed2Cost){ // movement 2 (10% speed)
            //GameScreen.difficulty.IncreaseMaxSpeedPercent(10);
            states.set(1, 0);
        }
        if(states.get(2) == 1 && states.get(0) == 2 && states.get(1) == 2 && points >= Speed3Points && coins >= Speed3Cost){ // movement 3 (15% speed)
            //GameScreen.difficulty.IncreaseMaxSpeedPercent(15);
            states.set(2, 0);
        }

        if(states.get(3) == 1 && points >= Traverse1Points && coins >= Traverse1Cost){ // traverse 1 (10% traverse)
            //GameScreen.difficulty.IncreaseTraversePercent(10);
            states.set(3, 0);
        }
        if(states.get(4) == 1 && states.get(3) == 2 && points >= Traverse2Points && coins >= Traverse2Cost){ // traverse 2 (20% traverse)
            //GameScreen.difficulty.IncreaseTraversePercent(20);
            states.set(4, 0);
        }

        if(states.get(5) == 1 && points >= Damage1Points && coins >= Damage1Cost){ // damage 1 (5 damage)
            //GameScreen.difficulty.IncreaseDamageDealtPercent(5);
            states.set(5, 0);
        }
        if(states.get(6) == 1 && states.get(5) == 2 && points >= Damage2Points && coins >= Damage2Cost){ // damage 2 (10 damage)
            //GameScreen.difficulty.IncreaseDamageDealtPercent(10);
            states.set(6, 0);
        }
        if(states.get(7) == 1 && states.get(5) == 2 && states.get(6) == 2 && points >= Damage3Points && coins >= Damage3Cost){ // damage 3 (20 damage)
            //GameScreen.difficulty.IncreaseDamageDealtPercent(20);
            states.set(7, 0);
        }

        if(states.get(8) == 1 && points >= Armour1Points && coins >= Armour1Cost){ // armour 1 (5% armour )
            //GameScreen.difficulty.DecreaseDamageRecievedPercent(14);
            states.set(8, 0);
        }
        if(states.get(9) == 1 && states.get(8) == 2 && points >= Armour2Points && coins >= Armour2Cost){ // armour 2 (10% armour)
            //GameScreen.difficulty.DecreaseDamageRecievedPercent(12);
            states.set(9, 0);
        }
        if(states.get(10) == 1 && states.get(8) == 2 && states.get(9) == 2 && points >= Armour3Points && coins >= Armour3Cost){ // armour 3 (20% armour)
            //GameScreen.difficulty.DecreaseDamageRecievedPercent(8);
            states.set(10, 0);
        }

        if(states.get(11) == 1 && points >= Gold1Points && coins >= Gold1Cost){ // gold multiplier 1 (x2 gold)
            //GameScreen.difficulty.IncreaseCoinMulti(1); // basically means prevGold * 2
            states.set(11, 0);
        }
        if(states.get(12) == 1 && states.get(11) == 2 && points >= Gold2Points && coins >= Gold2Cost){ // gold multiplier 2 (x3 gold)
            //GameScreen.difficulty.IncreaseCoinMulti(1); // basically means num * 2 * 3
            states.set(12, 0);
        }
        if(states.get(13) == 1 && points >= Cone2Points && coins >= Cone1Cost){ // coneshot
            // TODO cone shot ability in shop
            states.set(13, 0);
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
        Gdx.app.log("w", String.valueOf(width));
        Gdx.app.log("h", String.valueOf(height));
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




