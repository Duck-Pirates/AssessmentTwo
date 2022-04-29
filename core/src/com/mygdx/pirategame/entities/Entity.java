package com.mygdx.pirategame.entities;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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
public abstract class Entity extends Sprite implements Location<Vector2> {

    protected boolean toDestroy;
    protected boolean destroyed;
	protected GameScreen screen;
	protected Texture texture;
	protected World world;
    protected Body body;
    
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
        defineEntity(x, y);
        destroyed = false;
        toDestroy = false;
    }

    /**
     * Defines an entity
     */
    protected abstract void defineEntity(float x, float y);

    /**
     * Defines contact
     */
    public abstract void onContact();

	public Vector2 getPosition() {
		return getBody().getPosition();
	}
	
	@Override
	public float getOrientation() {
		return getBody().getAngle();
	}

	@Override
	public void setOrientation(float orientation) {
		getBody().setTransform(getPosition(), orientation);
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}

	@Override
	public Location<Vector2> newLocation() {
		return null;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void setDestroyed(Boolean destroyed) {
		this.destroyed = destroyed;
	}

	public boolean isSetToDestroy() {
		return toDestroy;
	}

	public void setToDestroy(Boolean toDestroy) {
		this.toDestroy = toDestroy;
	}
	
	public void dispose() {
		texture.dispose();
	}
}
