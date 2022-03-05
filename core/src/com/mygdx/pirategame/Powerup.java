package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * This class implements methods and variables for every powerup in game, that will have their own class and methods too
 *
 * @author Davide Bressani
 * @version 1.0
 */

public abstract class Powerup extends Entity{
    private Texture powerup;
    private boolean setToDestroyed;
    private boolean destroyed;
    private Sound powerupPickup;

    /**
     * Instantiates a new Powerup.
     *
     * @param screen the screen its going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     */
    public Powerup(GameScreen screen, float x, float y, String name) {
        super(screen, x, y);

        //TODO We need to add some texture and sound for the powerups

        //Set powerup image
        powerup = new Texture("coin.png");
        //Set the position and size of the powerup
        setBounds(0,0,48 / PirateGame.PPM, 48 / PirateGame.PPM);
        //Set the texture
        setRegion(powerup);
        //Sets origin of the powerup
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);
        powerupPickup = Gdx.audio.newSound(Gdx.files.internal("coin-pickup.mp3"));
    }

    /**
     * Updates the powerup's state. If needed, deletes the powerup if picked up.
     */
    public void update() {
        //If powerup is set to destroy and isnt, destroy it
        if(setToDestroyed && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        //Update position of powerup
        else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
        }
    }

}