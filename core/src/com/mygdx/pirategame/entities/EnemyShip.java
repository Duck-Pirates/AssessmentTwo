package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.fsm.EnemyStateMachine;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Enemy Ship
 * Generates enemy ship data
 * Instantiates an enemy ship
 *
 *@author Ethan Alabaster, Sam Pearson, Edward Poulter, Alexander Davis
 *@version like a million
 */
public class EnemyShip extends SteerableEntity {
    
    private StateMachine<EnemyShip, EnemyStateMachine> stateMachine;

    /**
     * Instantiates enemy ship
     *
     * @param screen Visual data
     * @param x x coordinates of entity
     * @param y y coordinates of entity
     * @param assignment College ship is assigned to
     */
    public EnemyShip(GameScreen screen, float x, float y, String assignment) {

        super(screen, x, y);

        texture = new Texture(assignment + "_ship.png");
        college = assignment;

        setTimeFired(GdxAI.getTimepiece().getTime());
        setStateMachine(new DefaultStateMachine<>(this, EnemyStateMachine.SLEEP));
        
        //Set the position and size of the college
        setBounds(0, 0, 64 / PPM, 110 / PPM);
        setRegion(texture);
        setOrigin(32 /PPM, 55 / PPM);

        damage = GameScreen.getDifficulty().getDamageDealt();

    }

    /**
     * Updates the state of each object with delta time
     * Checks for ship destruction
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void update(float delta) {

        //If ship is set to destroy and isn't, destroy it
        if(isSetToDestroy()) {
            //Play death noise
            if (GameScreen.getGame().getPreferences().isEffectsEnabled()) {
                destroy.play(GameScreen.getGame().getPreferences().getEffectsVolume());
            }
            world.destroyBody(getBody());
            setDestroyed(true);
            
            //Change player coins and points
            Hud.changePoints(30);
            Hud.changeCoins(10);
        }
        else {
        	getStateMachine().update();
        	if (behavior != null) {
    			behavior.calculateSteering(steerOutput);
    			applySteering(delta);
    		}
        	
            setRotation((float) Math.toDegrees(getOrientation()) - 90);
            setPosition(getPosition().x - getWidth() / 2f, getPosition().y - getHeight()/2f);
            
            // Updates cannonball data
            for(CannonFire ball : cannonBalls) {
                ball.update(delta);
                if(ball.isDestroyed()) {
                    cannonBalls.removeValue(ball, true);
                	ball.dispose();
                }
            }
            
            bar.update();
        }

        if(getHealth() <= 0) {
            setSetToDestroy(true);
        }

    }

    /**
     * Applies the steering to the ships, based on their targets
     *
     * @param delta Time elapsed
     */
	private void applySteering(float delta) {

    	Vector2 la = steerOutput.linear;
        float aa = steerOutput.angular;
        
        if(stateMachine.isInState(EnemyStateMachine.WANDER))
        	setOrientation(getOrientation() + aa * delta);
        else
        	setOrientation(la.angleRad());
        
        if(stateMachine.isInState(EnemyStateMachine.PERSUE)) {
        	float distanceToTargetSquare = ((Pursue<Vector2>) behavior).getTarget().getPosition().dst2(getPosition());
        	if(distanceToTargetSquare >= (400 / PPM) * (400 / PPM)) {
        		velocity = velocity + (maxLinearAcceleration * 0.85f * delta) * (1 - velocity / (maxLinearSpeed * 0.85f));
            	if (velocity < -0.5f)
            		velocity = -0.5f;

            	getBody().setLinearVelocity(velocity * MathUtils.cos(getOrientation()), velocity * MathUtils.sin(getOrientation()));
        	} else {
        		velocity = velocity * 0.8f;
        		if(velocity < 10) {
        			velocity = 0;
        		}
        		getBody().setLinearVelocity(velocity * MathUtils.cos(getOrientation()), velocity * MathUtils.sin(getOrientation()));

        		if(velocity == 0) {
	        		Vector2 A = getBody().getPosition();
	        		Vector2 B = ((Pursue<Vector2>) behavior).getTarget().getPosition();
	        		float newOrientation = B.sub(A).angleRad() + MathUtils.HALF_PI;
	        		setOrientation(newOrientation);
        		}
        	}
        } else {

        	velocity = velocity + (maxLinearAcceleration * 0.85f * delta) * (1 - velocity / (maxLinearSpeed * 0.85f));
        	if (velocity < -0.5f)
        		velocity = -0.5f;

        	getBody().setLinearVelocity(velocity * MathUtils.cos(getOrientation()), velocity * MathUtils.sin(getOrientation()));
        }
        
        if(getAngularVelocity() > maxAngularSpeed + 10) {
    		getBody().setAngularVelocity(maxAngularSpeed);
    	} else if (getBody().getAngularVelocity() < -maxAngularSpeed - 10) {
    		getBody().setAngularVelocity(-maxAngularSpeed);
    	}
	}

    /**
     * Defines the entity as an enemy
     *
     * @param x value of origin
     * @param y value of origin
     */
    @Override
    protected void defineEntity(float x, float y) {

        // Sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;

        // Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 20 / PPM);
        fdef.shape = shape;
        
        // Setting BIT identifier
        fdef.filter.categoryBits = ENEMY_BIT;
        // Determining what this BIT can collide with
        fdef.filter.maskBits = PLAYER_BIT | DEFAULT_BIT | COIN_BIT | COLLEGE_BIT | POWERUP_BIT | ENEMY_BIT
        					 | COLLEGESENSOR_BIT | COLLEGEFIRE_BIT | CLOUDS_BIT | CANNON_BIT;
        					 
        
        setBody(world.createBody(bdef));

        getBody().createFixture(fdef).setUserData(this);

    }

    /**
     * Constructs the ship batch
     *
     * @param batch The batch of visual data of the ship
     */
    @Override
    public void draw(Batch batch) {

        if(!isDestroyed()) {
            super.draw(batch);
            bar.render(batch);
        }

    }

    /**
     * Checks contact
     * Changes health in accordance with contact and damage
     */
    @Override
    public void onContact() {

        //Play collision sound
        if (GameScreen.getGame().getPreferences().isEffectsEnabled()) {
            hit.play(GameScreen.getGame().getPreferences().getEffectsVolume());
        }

        //Deal with the damage
        setHealth(getHealth() - GameScreen.getDifficulty().getDamageDealt());
        bar.changeHealth(GameScreen.getDifficulty().getDamageDealt());
        Hud.changePoints(5);

    }

	public StateMachine<EnemyShip, EnemyStateMachine> getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(StateMachine<EnemyShip, EnemyStateMachine> stateMachine) { this.stateMachine = stateMachine; }
}
