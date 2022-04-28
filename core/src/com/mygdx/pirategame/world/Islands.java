package com.mygdx.pirategame.world;

import static com.mygdx.pirategame.configs.Constants.DEFAULT_BIT;
import static com.mygdx.pirategame.configs.Constants.NOSPAWNAREA_BIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

/**
 * Sets up the class for all the Islands. Deals with what happens on collision and its properties
 * @author Ethan Alabaster
 * @version 1.0
 */
public class Islands extends InteractiveTileObject {
    /**
     * Instantiates a new Islands.
     *
     * @param screen visual data
     * @param bounds Rectangle boundary (world boundary)
     * @param bounding It's a check to know if the Island instance is used as no spawn areas
     */
    public Islands(GameScreen screen, Rectangle bounds, boolean bounding) {
        super(screen, bounds);
        fixture.setUserData(this);
        //Set the category bit
        if(bounding) {
            setCategoryFilter(NOSPAWNAREA_BIT);
        }
        else{
            setCategoryFilter(DEFAULT_BIT);
        }
    }

    /**
     * When contact occurs between the ship and island. deal damage to the ship
     */
    @Override
    public void onContact() {
        Gdx.app.log("island", "collision");
        //Deal damage to the boat
        Hud.changeHealth(-5);
    }
}