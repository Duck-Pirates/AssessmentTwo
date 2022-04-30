package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.PPM;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
    protected String college;
    
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected int health;
    protected int damage;
    protected HealthBar bar;

    protected static float zeroLinearSpeedThreshold = 0.01f;
    protected static float maxLinearSpeed = GameScreen.getDifficulty().getMaxSpeed() / PPM;
    protected static float maxLinearAcceleration = 55f / PPM;
    protected static float maxAngularSpeed = (float) Math.PI / GameScreen.getDifficulty().getTraverseSpeed();
    protected static float maxAngularAcceleration = (float) Math.PI / 16;
    protected static float boundingRadius = 55f / PPM;
    protected static boolean tagged = false;
    protected SteeringBehavior<Vector2> behavior;
    protected SteeringAcceleration<Vector2> steerOutput;
    
    protected Array<CannonFire> cannonBalls;
    protected float timeFired;

    /**
     * Instantiates an enemy
     *
     * @param screen Visual data
     * @param x x position of entity
     * @param y y position of entity
     */
    public SteerableEntity(GameScreen screen, float x, float y) {
    	super(screen, x, y);
        this.setSetToDestroy(false);
        this.setDestroyed(false);
        this.setHealth(100);
        bar = new HealthBar(this);
        
	    steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
	    
        cannonBalls = new Array<CannonFire>();
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
		return getBody().getPosition();
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
		SteerableEntity.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		SteerableEntity.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		SteerableEntity.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		SteerableEntity.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return getBody().getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return getBody().getAngularVelocity();
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
		SteerableEntity.tagged = tagged;
	}
	
	public void setBehavior(SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}
	
	public SteeringBehavior<Vector2> getBehavior() {
		return behavior;
	}
	
	public String getCollege() {
		return college;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public boolean isSetToDestroy() {
		return setToDestroy;
	}

	public void setSetToDestroy(boolean setToDestroy) {
		this.setToDestroy = setToDestroy;
	}
	
	/**
     * Draws the entity using batch
     * Draws cannonballs using batch
     *
     * @param batch The batch of the program
     */
    public void draw(Batch batch){
        // Draws player and cannonballs
        super.draw(batch);
        for(CannonFire ball : cannonBalls)
            ball.draw(batch);
    }
	
	@Override
	public void dispose() {
		super.dispose();
		bar.dispose();
	}

	public float getTimeFired() {
		return timeFired;
	}

	public void setTimeFired(float timeFired) {
		this.timeFired = timeFired;
	}
}