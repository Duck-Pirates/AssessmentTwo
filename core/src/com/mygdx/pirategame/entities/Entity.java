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
        super.setPosition(x, y);
        defineEntity(x, y);
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
		return body.getPosition();
	}

	public void setPosition(Vector2 position, float angle){
		body.setTransform(position, (float) Math.toRadians(angle));
		super.setPosition(position.x, position.y);
		super.setRotation(angle);
	}

	public void setPosition(float x, float y, float angle){ setPosition(new Vector2(x, y), angle);}
	
	@Override
	public float getOrientation() {
		return body.getAngle();
	}

	@Override
	public void setOrientation(float orientation) {
		body.setTransform(getPosition(), orientation);
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
}
