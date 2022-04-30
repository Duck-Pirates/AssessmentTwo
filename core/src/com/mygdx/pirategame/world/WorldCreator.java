package com.mygdx.pirategame.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.pirategame.college.*;
import com.mygdx.pirategame.screens.GameScreen;

import static com.mygdx.pirategame.configs.Constants.*;
import static com.mygdx.pirategame.configs.Constants.NOSPAWNAREA_BIT;

/**
 * This is the class where all boundaries and collisions are created for the map.
 * @author Ethan Alabaster
 * @version 1.0
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

        // Object class is islands, stuff for boat to collide with
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(PolygonMapObject.class)) {
            Polygon polygon = ((PolygonMapObject) object).getPolygon();
            polygon.setPosition(polygon.getX()/64, polygon.getY()/64);
            polygon.scale(-0.984375f);
            bounds.add(polygon);
        }
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Islands(screen, rect, false);
        }
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWallsAlcuin(screen, rect);
        }
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWallsGoodricke(screen, rect);
        }
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWallsAnneLister(screen, rect);
        }
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWallsConstantine(screen, rect);
        }
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWallsHalifax(screen, rect);
        }
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWallsLangwith(screen, rect);
        }
        for(MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new CollegeWallsVambrugh(screen, rect);
        }

    }

    public static ArrayList<Polygon> getBounds() {
        return bounds;
    }
}
