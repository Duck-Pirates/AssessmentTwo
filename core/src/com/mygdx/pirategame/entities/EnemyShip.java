package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.PirateGame.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.audio.Sound; 
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

/**
 * Enemy Ship
 * Generates enemy ship data
 * Instantiates an enemy ship
 *
 *@author Ethan Alabaster, Sam Pearson, Edward Poulter
 *@version 1.0
 */
public class EnemyShip extends Enemy implements Steerable<Vector2> {
    private Texture enemyShip;
    public String college;
    private String objective = "WANDER";
    private boolean objectiveChanged = true;
    private Sound destroy;
    private Sound hit;
    
    private Body body;
    private float zeroLinearSpeedThreshold = 0.01f;
    private float maxLinearSpeed, maxLinearAcceleration;
    private float maxAngularSpeed, maxAngularAcceleration;
    private float boundingRadius;
    private boolean tagged;
    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steerOutput;
    
    /**
     * Instantiates enemy ship
     *
     * @param screen Visual data
     * @param x x coordinates of entity
     * @param y y coordinates of entity
     * @param path path of texture file
     * @param assignment College ship is assigned to
     */
    public EnemyShip(GameScreen screen, float x, float y, String path, String assignment) {
        super(screen, x, y);
        enemyShip = new Texture(path);
        //Assign college
        college = assignment;
        //AI variables
        zeroLinearSpeedThreshold = 0.1f;
	    maxLinearSpeed = 50f;
	    maxLinearAcceleration = 10f;
	    maxAngularSpeed = 50f;
	    maxAngularAcceleration = 10f;
	    boundingRadius = 50/PPM;
	    tagged = false;
	    steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
        //Set audio
        destroy = Gdx.audio.newSound(Gdx.files.internal("ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("ship-hit.wav"));
        //Set the position and size of the college
        setBounds(0,0,64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(enemyShip);
        setOrigin(32 / PirateGame.PPM,55 / PirateGame.PPM);

        damage = screen.difficulty.getDamageDealt();
    }

    /**
     * Updates the state of each object with delta time
     * Checks for ship destruction
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float delta) {
        //If ship is set to destroy and isn't, destroy it
        if(setToDestroy && !destroyed) {
            //Play death noise
            if (screen.game.getPreferences().isEffectsEnabled()) {
                destroy.play(screen.game.getPreferences().getEffectsVolume());
            }
            world.destroyBody(body);
            destroyed = true;
            //Change player coins and points
            Hud.changePoints(30);
            Hud.changeCoins(10);
        }
        else if(!destroyed) {
            
//        	if(false) {
//        		
//        	} else {
//        		objective = "WANDER";
//        	}
        	
        	if(objectiveChanged) {
        		if(objective == "DEFENCE") {
        			
        		} else if(objective == "WANDER") {
        			Wander<Vector2> wanderSB = new Wander<Vector2>(this)
        					.setAlignTolerance(0.0174533f)
        					.setEnabled(true)
        					.setFaceEnabled(true)
        					.setWanderOffset(0f)
        					.setWanderRadius(50f / PPM)
        					.setWanderRate((float) Math.PI / 2);
        			behavior = wanderSB;
        		} else if(objective == "PURSUE") {
//        			Pursue<Vector2> persueSB = new Pursue<Vector2>(this);
        		}
        		objectiveChanged = false;
        	}
        	
        	GdxAI.getTimepiece().update(delta);
        	if (behavior != null) {
    			behavior.calculateSteering(steerOutput);
    			applySteering(steerOutput, delta);
    		}
        	
        	setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
            setRotation((float) (Math.toDegrees(body.getAngle())));
            
            bar.update();
        }
        if(health <= 0) {
            setToDestroy = true;
        }
    }
    
    private void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
		
		body.setAngularVelocity(steering.angular * delta);
		body.setLinearVelocity(steering.linear.scl(delta));
	}

    /**
     * Defines the ship as an enemy
     * Sets data to act as an enemy
     */
    @Override
    protected void defineEnemy() {
    	
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20 / PPM, 50 / PPM);
        fdef.shape = shape;
        shape.dispose();
        
        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.ENEMY_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT 
        		| PirateGame.CANNON_BIT;
        
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Constructs the ship batch
     *
     * @param batch The batch of visual data of the ship
     */
    public void draw(Batch batch) {
        if(!destroyed) {
            super.draw(batch);
            //Render health bar
            bar.render(batch);
        }
    }

    /**
     * Checks contact
     * Changes health in accordance with contact and damage
     */
    @Override
    public void onContact() {
        Gdx.app.log("enemy", "collision");
        //Play collision sound
        if (screen.game.getPreferences().isEffectsEnabled()) {
            hit.play(screen.game.getPreferences().getEffectsVolume());
        }
        //Deal with the damage
        health -= screen.difficulty.getDamageDealt();
        bar.changeHealth(screen.difficulty.getDamageDealt());
        Hud.changePoints(5);
    }

    /**
     * Updates the ship image. Particularly change texture on college destruction
     *
     * @param alignment Associated college
     * @param path Path of new texture
     */
    public void updateTexture(String alignment, String path){
        college = alignment;
        enemyShip = new Texture(path);
        setRegion(enemyShip);
    }
    
    public String getObjective() {
    	return objective;
    }
    
    public void setObjective(String objective) {
    	this.objective = objective;
    }
    
	public Body getBody() {
		return body;
	}
	
    @Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

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

	@SuppressWarnings("unchecked")
	@Override
	public Location<Vector2> newLocation() {
		return (Location<Vector2>) new Vector2();
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
}
