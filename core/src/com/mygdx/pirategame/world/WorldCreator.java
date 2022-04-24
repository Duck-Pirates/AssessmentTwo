package com.mygdx.pirategame.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.pirategame.college.CollegeWalls;
import com.mygdx.pirategame.college.CollegeWalls2;
import com.mygdx.pirategame.college.CollegeWalls3;
import com.mygdx.pirategame.college.CollegeWalls4;
import com.mygdx.pirategame.college.CollegeWalls5;
import com.mygdx.pirategame.college.CollegeWalls6;
import com.mygdx.pirategame.college.CollegeWalls7;
import com.mygdx.pirategame.screens.GameScreen;

import java.util.ArrayList;

/**
 * This is the class where all boundaries and collisions are created for the map.
 * @author Ethan Alabaster
 * @version 1.0
 */
public class WorldCreator {

    protected static ArrayList<Rectangle> rectangleBounds = new ArrayList<>();
    /**
     * Starts the creation of the boundaries
     *
     * @param screen the screen that the boundaries are relevant for
     */
    public WorldCreator(GameScreen screen) {
        TiledMap map = screen.getMap();

        // Object class is islands, stuff for boat to collide with
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Islands(screen, rect);
        }
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWalls(screen, rect);
        }
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWalls2(screen, rect);
        }
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWalls3(screen, rect);
        }
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWalls4(screen, rect);
        }
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWalls5(screen, rect);
        }
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWalls6(screen, rect);
        }
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWalls7(screen, rect);
        }
    }
}
