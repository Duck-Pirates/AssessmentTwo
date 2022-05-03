package com.mygdx.pirategame.entities;

import com.mygdx.pirategame.screens.GameScreen;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Entity
 * Defines an entity
 * Instantiates an entity
 *
 * @author Ethan Alabaster
 * @author Alex Davis
 * @version 2.0
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
        super.setPosition(x, y);
        defineEntity(x, y);
        destroyed = false;
        toDestroy = false;

    }

	/**
	 * Defines an entity
	 *
	 * @param x value of origin
	 * @param y value of origin
	 */
	protected abstract void defineEntity(float x, float y);

    /**
     * Defines contact
     */
    public abstract void onContact();

	public void dispose() {
		texture.dispose();
	}

	public Vector2 getPosition() {
		return getBody().getPosition();
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public boolean isSetToDestroy() {
		return toDestroy;
	}

	public Body getBody() {
		return body;
	}
	
	@Override
	public float getOrientation() {
		return getBody().getAngle();
	}

	/**
	 * Converts a vector into an angle in degrees
	 *
	 * @param vector The vector to be converted
	 * @return The angle
	 */
	@Override
	public float vectorToAngle(Vector2 vector) { return (float)Math.atan2(-vector.x, vector.y); }

	/**
	 * Converts from an angle to a vector, and it stores the result in the parse vector
	 *
 	 * @param outVector The vector where the method stores the new coordinates
	 * @param angle The angle
	 * @return The new vector
	 */
	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {

		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;

	}

	@Override
	public Location<Vector2> newLocation() { return null; }

	@Override
	public void setOrientation(float orientation) {
		getBody().setTransform(getPosition(), orientation);
	}

	public void setPosition(Vector2 position, float angle){

		body.setTransform(position, (float) Math.toRadians(angle));
		super.setPosition(position.x, position.y);
		super.setRotation(angle);

	}

	public void setPosition(float x, float y, float angle){ setPosition(new Vector2(x, y), angle);}

	public void setBody(Body body) { this.body = body; }
	
	public void setDestroyed(Boolean destroyed) { this.destroyed = destroyed; }

	public void setToDestroy(Boolean toDestroy) { this.toDestroy = toDestroy; }

}
