package com.mygdx.pirategame.world;

import static com.mygdx.pirategame.configs.Constants.DEFAULT_BIT;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

import com.badlogic.gdx.math.Rectangle;

/**
 * Sets up the class for all the Islands. Deals with what happens on collision and its properties
 *
 * @author Ethan Alabaster
 * @version 1.0
 */
public class Islands extends InteractiveTileObject {

    /**
     * Instantiates a new Islands.
     *
     * @param screen visual data
     * @param bounds Rectangle boundary (world boundary)
     */
    public Islands(GameScreen screen, Rectangle bounds) {

        super(screen, bounds);

        fixture.setUserData(this);
        //Set the category bit
        setCollisionFilter(DEFAULT_BIT);

    }

    /**
     * Defines collisions between the player and the islands
     */
    @Override
    public void onContact() { Hud.changeHealth(-GameScreen.getDifficulty().getDamageReceived()); }

}
