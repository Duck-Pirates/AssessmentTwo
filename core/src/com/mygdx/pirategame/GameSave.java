package com.mygdx.pirategame;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * GameSave
 * The class manages the saving and loading phases of the game. To do so, it only get the important information
 * about the main classes in the project and it serializes them (convert into bytes) and writes them onto a json
 * file.
 *
 * @author Davide Bressani
 * @version 1.0
 */

public class GameSave {

    private static Difficulty difficultySave;
    private static AvailableSpawn invalidSpawnSave;

    private static PlayerSave playerSave;
    private static ArrayList<CollegeSave> collegesSaves;
    private static ArrayList<ShipSave> fleetsSaves;
    private static ArrayList<CoinSave> coinSaves;
    private static HudSave hudSave;
    private static ArrayList<PowerUpSave> powerUpSaves;

    private static float tempTimeSave;


    private static ArrayList<Integer> statesSave;

    /**
     * Saves the game state and writes it to the json file
     * @param game The GameScreen class
     */

    private void save(GameScreen game, SkillTree shop) {
        difficultySave = game.difficulty;
        invalidSpawnSave = game.invalidSpawn;

        playerSave = new PlayerSave(game.player);
        for (College college: game.colleges) {
            CollegeSave collegeSave = new CollegeSave(college);
            collegesSaves.add(collegeSave);
            fleetsSaves.addAll(collegeSave.fleet);
        }
        for (Coin coin: game.coins) {
            coinSaves.add(new CoinSave(coin));
        }
        hudSave = new HudSave(game.hud);
        for (Powerup powerup: game.powerups){
            powerUpSaves.add(new PowerUpSave(powerup));
        }

        tempTimeSave = game.TempTime;

        statesSave = shop.states;
    }




    /**
     * PlayerSave
     * Auxiliary class to save data about the player
     */
    public static class PlayerSave{

        private float velocity, rotation, maxVelocity, maxAngularVelocity;
        private Vector2 position;

        public PlayerSave(Player player) {
            velocity = player.velocity;
            rotation = player.getRotation();
            maxVelocity = player.maxVelocity;
            maxAngularVelocity = player.maxAngularVelocity;
            position = player.b2body.getPosition();
        }
    }


    /**
     * CollegeSave
     * Auxiliary class to save data about the college
     */
    public static class CollegeSave{

        private final String collegeName;
        private ArrayList<ShipSave> fleet;
        private final float x, y;
        private int health;

        public CollegeSave(College college) {
            collegeName = college.getCurrentCollege();
            for(EnemyShip ship: college.fleet){
                fleet.add(new ShipSave(ship));
            }
            x = college.getX();
            y = college.getY();
            health = college.health;
        }
    }

    /**
     * ShipSave
     * Auxiliary class to save data about the college's fleets
     */
    public static class ShipSave{

        private String college;
        private float x, y, rotation;
        private int health;


        public ShipSave(EnemyShip ship){
            college = ship.college;
            x = ship.getX();
            y = ship.getY();
            rotation = ship.getRotation();
            health = ship.health;
        }
    }

    /**
     * CoinSave
     * Auxiliary class to save data about the coins
     */
    public static class CoinSave {

        private static float x, y;

        public CoinSave(Coin coin){
            x = coin.getX();
            y = coin.getY();
        }
    }

    /**
     * HudSave
     * Auxiliary class to save data about the HUD
     */
    public static class HudSave{

        private static float timeCount, constantTimeCount, powerUpTimer;
        private static Integer score, health, coins, coinMulti, powerUpType;
        private static boolean powerUpTimerBool;

        public HudSave(Hud hud){
            timeCount = hud.timeCount;
            constantTimeCount = hud.Constant_timeCount;
            powerUpTimer = hud.PowerupTimer;
            score = hud.score;
            health = hud.health;
            coins = hud.coins;
            coinMulti = difficultySave.getGoldCoinMulti();
            powerUpType = hud.PowerUpType;
            powerUpTimerBool = hud.PowerupTimerBool;
        }
    }

    /**
     * PowerUpSave
     * Auxiliary class to save data about the PowerUps
     */
    public static class PowerUpSave{

        private static float x, y;
        private static Integer type;

        public PowerUpSave(Powerup powerup){
            x = powerup.getX();
            y = powerup.getY();
            type = powerup.powerupType;
        }
    }
}
