package com.mygdx.pirategame.college;

import static com.mygdx.pirategame.configs.Constants.COLLEGE_BIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.world.InteractiveTileObject;

/**
 * College Walls (Langwith)
 * Checks interaction with walls from map
 *
 *@author Harry Swift
 *@version 2.0
 */
public class CollegeWallsLangwith extends InteractiveTileObject {
    /**
     * Sets bounds of college walls
     *
     * @param screen Visual data
     * @param bounds Wall bounds
     */
    public CollegeWallsLangwith(GameScreen screen, Rectangle bounds) {
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
        Gdx.app.log("wall", "Langwith");
        //Deal damage to the assigned college
        GameScreen.getCollege("Langwith").onContact();
    }
}
