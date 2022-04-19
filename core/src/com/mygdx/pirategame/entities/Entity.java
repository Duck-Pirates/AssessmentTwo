package com.mygdx.pirategame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.pirategame.screens.GameScreen;

/**
 * Entity
 * Defines an entity
 * Instantiates an entity
 *
 *@author Ethan Alabaster
 *@version 1.0
 */
public abstract class Entity extends Sprite {
	protected GameScreen screen;
	protected Texture texture;
	protected World world;
    public Body body;
    
    
    /**
     * Instantiates an entity
     * Sets position in world
     *
     * @param screen Visual data
     * @param x x position of entity
     * @param y y position of entity
     */
    public Entity(GameScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEntity();
    }

    /**
     * Defines an entity
     */
    protected abstract void defineEntity();

    /**
     * Defines contact
     */
    public abstract void onContact();
}
