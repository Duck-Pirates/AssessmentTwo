package com.mygdx.pirategame.world;

import static com.mygdx.pirategame.configs.Constants.COLLEGE_BIT;

import com.mygdx.pirategame.entities.College;
import com.mygdx.pirategame.screens.GameScreen;

import com.badlogic.gdx.math.Rectangle;

/**
 * College Walls (Alcuin)
 * Checks interaction with walls from map
 *
 * @author Ethan Alabaster, Sam Pearson
 * @author Davide Bressani, Harry Swift, Alex Davis
 * @version 2.0
 */
public class CollegeWalls extends InteractiveTileObject {

    public String college;

    /**
     * Sets bounds of college walls
     *
     * @param screen Visual data
     * @param bounds Wall bounds, taken from the Tiled Map
     */
    public CollegeWalls(GameScreen screen, Rectangle bounds, String college) {
        super(screen, bounds);
        fixture.setUserData(this);

        // Set the category bit
        setCollisionFilter(COLLEGE_BIT);
        this.college = college;
    }

    /**
     * Checks for contact with a cannonball
     */
    @Override
    public void onContact() {

        //Deal damage to the assigned college
        GameScreen.getCollege(college).onContact();

    }

    public College getCollege() { return GameScreen.getCollege(college); }

}
