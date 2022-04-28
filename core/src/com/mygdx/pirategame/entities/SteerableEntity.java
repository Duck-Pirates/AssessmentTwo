package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.PPM;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.screens.GameScreen;

/**
 * Enemy
 * Class to generate enemies
 * Instantiates enemies
 *
 *@author Ethan Alabaster
 *@version 1.0
 */
public abstract class SteerableEntity extends Entity implements Steerable<Vector2> {
    public String college;
    
	public boolean setToDestroy;
    public boolean destroyed;
    public int health;
    public int damage;
    protected HealthBar bar;

    protected float zeroLinearSpeedThreshold = 0.01f;
    protected float maxLinearSpeed, maxLinearAcceleration;
    protected float maxAngularSpeed, maxAngularAcceleration;
    protected float boundingRadius;
    protected boolean tagged;
    protected SteeringBehavior<Vector2> behavior;
    protected SteeringAcceleration<Vector2> steerOutput;


    /**
     * Instantiates an enemy
     *
     * @param screen Visual data
     * @param x x position of entity
     * @param y y position of entity
     */
    public SteerableEntity(GameScreen screen, float x, float y) {
    	super(screen, x, y);
        this.setToDestroy = false;
        this.destroyed = false;
        this.health = 100;
        bar = new HealthBar(this);
        
        zeroLinearSpeedThreshold = 0.1f;
	    maxLinearSpeed = 302.5f / PPM;
	    maxLinearAcceleration = 55f / PPM;
	    maxAngularSpeed = (float) Math.PI / 4;
	    maxAngularAcceleration = (float) Math.PI / 16;
	    boundingRadius = 55f / PPM;
	    tagged = false;
	    steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
    }
    
    public abstract void update(float delta);
    
    public abstract void fire();

    /**
     * Checks recieved damage
     * Increments total damage by damage received
     * @param value Damage received
     */
    public void changeDamageReceived(int value){
        damage += value;
    }

    /**
     * Updates the ship image. Particularly change texture on college destruction
     *
     * @param alignment Associated college
     * @param path Path of new texture
     */
    public void updateTexture(String alignment, String path){
        college = alignment;
        texture = new Texture(path);
        setRegion(texture);
    }
	
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return zeroLinearSpeedThreshold;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		zeroLinearSpeedThreshold = value;
	}

	@Override
	public float getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}
	
	public void setBehavior(SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}
	
	public SteeringBehavior<Vector2> getBehavior() {
		return behavior;
	}
}