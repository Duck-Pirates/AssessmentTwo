package com.mygdx.pirategame.world;

import static com.mygdx.pirategame.configs.Constants.PPM;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

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
    public static ObjectMap<Integer, Array<Integer>> blockedTiles = new ObjectMap(){{put(Math.floor(1200/PPM), new ArrayList<Integer>(Arrays.asList((int)Math.floor(2500/PPM))));}};

    public AvailableSpawn(){

    }
    public static boolean add(int x, final int y){
        for(Polygon polygon: bounds){
            if(polygon.contains(x, y)){
                return false;
            }
        }
        if(getBlockedTiles().containsKey(x) && getBlockedTiles().get(x).contains(y, true)){
            return false;
        }
        if(getBlockedTiles().containsKey(x)){
            getBlockedTiles().get(x).add(y);
        }
        else{
            getBlockedTiles().put(x, new Array<Integer>(){{add(y);}});
        }
        return true;
    }

    public static ObjectMap<Integer, Array<Integer>> getBlockedTiles() {
        return blockedTiles;
    }

    public void setBlockedTiles(ObjectMap<Integer, Array<Integer>> blockedTiles) {
        this.blockedTiles = blockedTiles;
    }
}
