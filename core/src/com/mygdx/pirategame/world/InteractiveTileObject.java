package com.mygdx.pirategame.world;

import static com.mygdx.pirategame.configs.Constants.PPM;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.pirategame.screens.GameScreen;

/**
 * Interactive Tile Object
 * Generates the world with interactive tiles
 *
 *@author Ethan Alabaster
 *@version 1.0
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    /**
     * Instantiates world data
     *
     * @param screen Visual data
     * @param bounds Rectangle boundary (world boundary)
     */
    public InteractiveTileObject(GameScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.bounds = bounds;

        //Create objects used for dimensions
        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        //Set position
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set((bounds.getX() + bounds.getWidth() / 2) / PPM, (bounds.getY() + bounds.getHeight() / 2) / PPM);

        body = world.createBody(bDef);

        shape.setAsBox(bounds.getWidth() / 2 / PPM, bounds.getHeight() / 2 / PPM);
        fDef.shape = shape;
        fDef.restitution = 0f;
        fixture = body.createFixture(fDef);
    }

    /**
     * Check contact
     */
    public abstract void onContact();

    /**
     * Set filter
     */
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
