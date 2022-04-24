package com.mygdx.pirategame;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

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
    public static final int xCap = 250;
    public static final int yCap = 250;
    public static final int xBase = 14;
    public static final int yBase = 14;

    /**
     * Checks if given value pair is already contained in the map
     * If not contained in the map, adds the data pair to the map
     *
     * @param x the x coord value
     * @param y the y coord value
     */
    static boolean add(int x, int y){
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
