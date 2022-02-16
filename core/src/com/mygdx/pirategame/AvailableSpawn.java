package com.mygdx.pirategame;

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
    public HashMap<Integer, ArrayList<Integer>> tileBlocked = new HashMap<>();
    public static final int xCap = 75;
    public static final int yCap = 75;
    public static final int xBase = 7;
    public static final int yBase = 7;

    /**
     * Initialises the Available Spawn Map
     * Generates the validity data for a given area
     */
    public AvailableSpawn() {
        //Determines island coverage (where ships and coins can't spawn)
        for (int x = 0; x < xCap; x++) {
            for (int y = 0; y < yCap; y++) {
                //Goodricke islands
                if (x >= 50 && y <= 50) {
                    if (x >= 53 && x <= 59 && y >= 10 && y <= 12) {
                        add(x, y);
                    } else if (x >= 58 && x <= 60 && y >= 12 && y <= 13) {
                        add(x, y);
                    } else if (x >= 60 && x <= 65 && y >= 9 && y <= 14) {
                        add(x, y);
                    } else if (x >= 64 && x <= 67 && y >= 12 && y <= 16) {
                        add(x, y);
                    } else if (x >= 68 && x <= 73 && y >= 16 && y <= 24) {
                        add(x, y);
                    }
                    //Constantine islands
                } else if (x <= 50 && y >= 50) {
                    if (x >= 9 && x <= 13 && y >= 60 && y <= 62) {
                        add(x, y);
                    } else if (x >= 11 && x <= 26 && y >= 63 && y <= 69) {
                        add(x, y);
                    } else if (x >= 12 && x <= 25 && y >= 70 && y <= 71) {
                        add(x, y);
                    } else if (x >= 30 && x <= 32 && y >= 68 && y <= 69){
                        add(x, y);
                    }
                    //Anne Lister islands
                } else if (x >= 50) {
                    if (x >= 58 && x <= 71 && y >= 64 && y <= 70) {
                        add(x, y);
                    } else if (x >= 67 && x <= 71 && y >= 60 && y <= 64) {
                        add(x, y);
                    } else if (x >= 57 && x <= 59 && y >= 59 && y <= 62) {
                        add(x, y);
                    }
                    //Alcuin islands
                } else {
                    if (x >= 14 && x <= 23 && y >= 18 && y <= 24) {
                        add(x, y);
                    } else if (x >= 25 && x <= 29 && y >= 14 && y <= 17) {
                        add(x, y);
                    }
                }
            }
        }
    }

    /**
     * Checks if given value pair is already contained in the map
     * If not contained in the map, adds the data pair to the map
     *
     * @param x the x coord value
     * @param y the y coord value
     */
    private void add(int x, int y){
        //Adds to hashmap, prevents duplication of values
        ArrayList<Integer> checkY;
        if (tileBlocked.containsKey(x)) {
            checkY = tileBlocked.get(x);
            if (!checkY.contains(y)) {
                checkY.add(y);
            }
        }else{
            checkY = new ArrayList<>();
            checkY.add(y);
        }
        tileBlocked.put(x, checkY);
    }
}
