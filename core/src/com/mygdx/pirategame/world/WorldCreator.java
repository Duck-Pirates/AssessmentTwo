package com.mygdx.pirategame.world;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.college.*;
import com.mygdx.pirategame.screens.GameScreen;

import java.util.ArrayList;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

/**
 * This is the class where all boundaries and collisions are created for the map.
 *
 * @author Ethan Alabaster
 * @author Davide Bressani
 * @version 2.0
 */
public class WorldCreator {

    protected static ArrayList<Polygon> bounds = new ArrayList<>();

    /**
     * Starts the creation of the boundaries
     *
     * @param screen the screen that the boundaries are relevant for
     */
    public WorldCreator(GameScreen screen) {

        TiledMap map = screen.getMap();

        // Get the polygons where the entities shouldn't be spawning
        for(PolygonMapObject object : map.getLayers().get(4).getObjects().getByType(PolygonMapObject.class)) {
            Polygon polygon = object.getPolygon();
            polygon.setPosition(polygon.getX()/PPM, polygon.getY()/PPM);
            polygon.scale(-(1-1/PPM));
            bounds.add(polygon);
        }

        // Object class is islands, stuff for boat to collide with
        for(RectangleMapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Islands(screen, rect);
        }

        for(RectangleMapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new CollegeWallsAlcuin(screen, rect);
        }

        for(RectangleMapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new CollegeWallsGoodricke(screen, rect);
        }

        for(RectangleMapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new CollegeWallsAnneLister(screen, rect);
        }

        for(RectangleMapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new CollegeWallsConstantine(screen, rect);
        }

        for(RectangleMapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new CollegeWallsHalifax(screen, rect);
        }

        for(RectangleMapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new CollegeWallsLangwith(screen, rect);
        }

        for(RectangleMapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new CollegeWallsVambrugh(screen, rect);
        }

    }

    public static ArrayList<Polygon> getBounds() {
        return bounds;
    }

}
