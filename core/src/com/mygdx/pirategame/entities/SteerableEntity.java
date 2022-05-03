package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.PPM;
import com.mygdx.pirategame.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Enemy
 * Class to generate entities that are steerable
 *
 * @author Ethan Alabaster
 * @author Alex Davis
 * @version 2.0
 */
public abstract class SteerableEntity extends Entity implements Steerable<Vector2> {

    protected String college;
    
    protected Sound destroy;
    protected Sound hit;
    
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
        
        // Set audio effects
        destroy = Gdx.audio.newSound(Gdx.files.internal("ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("ship-hit.wav"));
        
	    steerOutput = new SteeringAcceleration<>(new Vector2());
	    
        cannonBalls = new Array<>();

    }
    
    public abstract void update(float delta);

	/**
	 * This method is used by the ships to fire cannonballs
	 */
	public void fire() {

		if(!getCollege().equals("unaligned")) { // This checks if the ships are unaligned (because these shouldn't be able to shoot)
			cannonBalls.add(new CannonFire(screen, getBody(), getPosition().x + (30 / PPM) * (float) Math.sin(getOrientation()),
					getPosition().y - (30 / PPM) * (float) Math.cos(getOrientation()), getOrientation() - (float) Math.PI / 2, 5, false));
			cannonBalls.add(new CannonFire(screen, getBody(), getPosition().x - (30 / PPM) * (float) Math.sin(getOrientation()),
					getPosition().y + (30 / PPM) * (float) Math.cos(getOrientation()), getOrientation() + (float) Math.PI / 2, 5, false));
			}
		else if(!getCollege().equals("alcuin")){ // This checks if the ships are not from alcuin
			cannonBalls.add(new CannonFire(screen, getBody(), getPosition().x + (30 / PPM) * (float) Math.sin(getOrientation()),
					getPosition().y - (30 / PPM) * (float) Math.cos(getOrientation()), getOrientation() - (float) Math.PI / 2, 5, false));
			cannonBalls.add(new CannonFire(screen, getBody(), getPosition().x - (30 / PPM) * (float) Math.sin(getOrientation()),
					getPosition().y + (30 / PPM) * (float) Math.cos(getOrientation()), getOrientation() + (float) Math.PI / 2, 5, false));
		}

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

	/**
	 * Disposes everything related to steerable entity
	 */
	@Override
	public void dispose() {

		super.dispose();
		bar.dispose();
		destroy.dispose();
		hit.dispose();

	}
	
	public Vector2 getPosition() { return getBody().getPosition(); }

	public String getCollege() { return college; }

	public SteeringBehavior<Vector2> getBehavior() { return behavior; }

	public int getHealth() { return health; }

	public float getTimeFired() { return timeFired; }

	public boolean isSetToDestroy() { return setToDestroy; }

	public boolean isDestroyed() { return destroyed; }

	@Override
	public float getZeroLinearSpeedThreshold() { return zeroLinearSpeedThreshold; }

	@Override
	public float getMaxLinearSpeed() { return maxLinearSpeed; }

	@Override
	public float getMaxLinearAcceleration() { return maxLinearAcceleration; }

	@Override
	public float getMaxAngularSpeed() { return maxAngularSpeed; }

	@Override
	public float getMaxAngularAcceleration() { return maxAngularAcceleration; }

	@Override
	public Vector2 getLinearVelocity() { return getBody().getLinearVelocity(); }

	@Override
	public float getAngularVelocity() { return getBody().getAngularVelocity(); }

	@Override
	public float getBoundingRadius() { return boundingRadius; }

	@Override
	public boolean isTagged() { return tagged; }
	
	public void setBehavior(SteeringBehavior<Vector2> behavior) { this.behavior = behavior; }

	public void setHealth(int health) { this.health = health; }

	public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }

	public void setSetToDestroy(boolean setToDestroy) { this.setToDestroy = setToDestroy; }

	public void setTimeFired(float timeFired) { this.timeFired = timeFired; }

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) { SteerableEntity.maxLinearSpeed = maxLinearSpeed; }

	@Override
	public void setZeroLinearSpeedThreshold(float value) { zeroLinearSpeedThreshold = value; }

	@Override
	public void setTagged(boolean tagged) { SteerableEntity.tagged = tagged; }

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) { SteerableEntity.maxAngularAcceleration = maxAngularAcceleration; }

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) { SteerableEntity.maxAngularSpeed = maxAngularSpeed; }

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) { SteerableEntity.maxLinearAcceleration = maxLinearAcceleration; }

}