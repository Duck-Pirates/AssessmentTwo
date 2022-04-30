package com.mygdx.pirategame.world;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

/**
 * Available Spawn
 * Class to determine invalid spawn areas
 * Instantiates invalid spawn map
 *
 *@author Edward Poulter
 *@version 1.0
 */
public class AvailableSpawn {
    public static ArrayList<Rectangle> tileBlocked = new ArrayList<>();

    /**
     * Checks if given value pair is already contained in the map
     * If not contained in the map, adds the data pair to the map
     *
     * @param x the x coord value
     * @param y the y coord value
     */
    public static boolean add(int x, int y){
        boolean check = check(x, y);
        if (check){
            Rectangle rect = new Rectangle(x, y, 1, 1);
            tileBlocked.add(rect);
        }
        return check;
    }

    protected static boolean check(int x, int y) {
        for (Rectangle rect : tileBlocked) {
            if (rect.contains(x, y)) {
                return false;
            }
        }
        return true;
    }
}
