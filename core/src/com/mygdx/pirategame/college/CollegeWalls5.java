package com.mygdx.pirategame.college;

import static com.mygdx.pirategame.configs.Constants.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.world.InteractiveTileObject;

/**
 * College Walls (Halifax)
 * Checks interaction with walls from map
 *
 *@author Ethan Alabaster, Sam Pearson
 *@version 1.0
 */
public class CollegeWalls5 extends InteractiveTileObject {
    /**
     * Sets bounds of college walls
     *
     * @param screen Visual data
     * @param bounds Wall bounds
     */
    public CollegeWalls5(GameScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        //Set the category bit
        setCategoryFilter(COLLEGE_BIT);
    }

    /**
     * Checks for contact with cannonball
     */
    @Override
    public void onContact() {
        Gdx.app.log("wall", "collision");
        //Deal damage to the assigned college
        GameScreen.getCollege("Halifax").onContact();
    }
}
