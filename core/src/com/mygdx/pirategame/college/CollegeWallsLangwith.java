package com.mygdx.pirategame.college;

import static com.mygdx.pirategame.configs.Constants.COLLEGE_BIT;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.world.InteractiveTileObject;

import com.badlogic.gdx.math.Rectangle;

/**
 * College Walls (Langwith)
 * Checks interaction with walls from map
 *
 * @author Davide Bressani, Harry Swift
 * @version 1.0
 */
public class CollegeWallsLangwith extends InteractiveTileObject {

    /**
     * Sets bounds of college walls
     *
     * @param screen Visual data
     * @param bounds Wall bounds, taken from the Tiled Map
     */
    public CollegeWallsLangwith(GameScreen screen, Rectangle bounds) {

        super(screen, bounds);
        fixture.setUserData(this);

        //Set the category bit
        setCollisionFilter(COLLEGE_BIT);

    }

    /**
     * Checks for contact with cannonball
     */
    @Override
    public void onContact() {

        //Deal damage to the assigned college
        GameScreen.getCollege("Langwith").onContact();

    }

}
