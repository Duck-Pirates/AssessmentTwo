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

    private Difficulty difficultySave;

    private PlayerSave playerSave;
    private static ArrayList<CollegeSave> collegesSave;

    /**
     * Saves the game state and writes it to the json file
     * @param game The GameScreen class
     */

    private void save(GameScreen game) {
        this.difficultySave = game.difficulty;

        this.playerSave = new PlayerSave(game.player);
        for(int i = 0; i < game.colleges.size(); i++){
            collegesSave.add(new CollegeSave(game.colleges.get(i)));
        }
    }

    /**
     * PlayerSave
     * Auxiliary class to save data about the player
     */
    public static class PlayerSave{

        protected float velocity, rotation, maxVelocity, maxAngularVelocity;
        protected Vector2 position;

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

        protected final String collegeName;
        protected ArrayList<ShipSave> fleet;
        protected final float x, y;
        protected int health;

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
     * CollegeSave
     * Auxiliary class to save data about the college's fleets
     */
    public static class ShipSave{

        protected String college;
        protected final float x, y;
        protected int health;


        public ShipSave(EnemyShip ship){
            x = ship.getX();
            y = ship.getY();
            health = ship.health;
            college = ship.college;
        }
    }
}
