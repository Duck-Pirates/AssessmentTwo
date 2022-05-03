package com.mygdx.pirategame.world;

import static com.mygdx.pirategame.configs.Constants.PPM;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Available Spawn
 * Class to determine invalid spawn areas
 * Instantiates invalid spawn map
 *
 * @author Edward Poulter
 * @author Davide Bressani, Harry Swift
 * @version 2.0
 */
public class AvailableSpawn {

    public static ArrayList<Polygon> bounds = WorldCreator.getBounds();
    public static ObjectMap blockedTiles = new ObjectMap(){{put(Math.floor(1200/PPM), new ArrayList<>((int)Math.floor(2500/PPM)));}};

    /**
     * Tries to add a new coordinate to the blockedTiles ObjectMap, that stores all the coordinates where entities have been spawned
     * To do so it checks if the entity is trying to spawn on an island or on top of another entity
     *
     * @param x The x coordinate of the new entity
     * @param y The y coordinate of the new entity
     * @return Returns if the entity can successfully be spawned at this location and, if so, it adds it to the blockedTiles HashMap
     */
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
        AvailableSpawn.blockedTiles = blockedTiles;
    }

}
