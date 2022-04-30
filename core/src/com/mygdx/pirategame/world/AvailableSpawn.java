package com.mygdx.pirategame.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.math.Polygon;
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
    public static ArrayList<Polygon> bounds = WorldCreator.getBounds();
    public static HashMap<Integer, ArrayList<Integer>> blockedTiles = new HashMap(){{put(12, new ArrayList<Integer>(Arrays.asList(25)));}};

    public static boolean add(int x, final int y){
        for(Polygon polygon: bounds){
            if(polygon.contains(x, y)){
                return false;
            }
        }
        if(blockedTiles.containsKey(x) && blockedTiles.get(x).contains(y)){
            return false;
        }
        if(blockedTiles.containsKey(x)){
            blockedTiles.get(x).add(y);
        }
        else{
            blockedTiles.put(x, new ArrayList<Integer>(){{add(y);}});
        }
        return true;
    }
}
