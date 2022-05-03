package com.mygdx.pirategame.world;

import static com.mygdx.pirategame.configs.Constants.COLLEGE_BIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.pirategame.entities.College;
import com.mygdx.pirategame.screens.GameScreen;

/**
 * College Walls (Alcuin)
 * Checks interaction with walls from map
 *
 *@author Ethan Alabaster, Sam Pearson, Alex Davis
 *@version 1.0
 */
public class CollegeWalls extends InteractiveTileObject {
	public String college;
    /**
     * Sets bounds of college walls
     *
     * @param screen Visual data
     * @param bounds Wall bounds
     */
    public CollegeWalls(GameScreen screen, Rectangle bounds, String college) {
        super(screen, bounds);
        fixture.setUserData(this);
        //Set the category bit
        setCategoryFilter(COLLEGE_BIT);
        this.college = college;
    }

    /**
     * Checks for contact with cannonball
     */
    @Override
    public void onContact() {
        Gdx.app.log("wall", college);
        
        //Deal damage to the assigned college
        GameScreen.getCollege(college).onContact();
    }
    
    public College getCollege() {
    	return GameScreen.getCollege(college);
    }
}
