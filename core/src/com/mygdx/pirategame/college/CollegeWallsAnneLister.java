package com.mygdx.pirategame.college;

import static com.mygdx.pirategame.configs.Constants.COLLEGE_BIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.world.InteractiveTileObject;

/**
 * College Walls (Anne Lister)
 * Checks interaction with walls from map
 *
 *@author Ethan Alabaster, Sam Pearson
 *@version 1.0
 */
public class CollegeWallsAnneLister extends InteractiveTileObject {
    /**
     * Sets bounds of college walls
     *
     * @param screen Visual data
     * @param bounds Wall bounds
     */
    public CollegeWallsAnneLister(GameScreen screen, Rectangle bounds) {
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
        Gdx.app.log("wall", "Anne Lister");
        //Deal damage to the assigned college
        GameScreen.getCollege("Anne Lister").onContact();
    }
}