package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.COIN_BIT;
import static com.mygdx.pirategame.configs.Constants.COLLEGEFIRE_BIT;
import static com.mygdx.pirategame.configs.Constants.COLLEGESENSOR_BIT;
import static com.mygdx.pirategame.configs.Constants.COLLEGE_BIT;
import static com.mygdx.pirategame.configs.Constants.DEFAULT_BIT;
import static com.mygdx.pirategame.configs.Constants.ENEMY_BIT;
import static com.mygdx.pirategame.configs.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.pirategame.fsm.EnemyStateMachine;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

/**
 * Enemy Ship
 * Generates enemy ship data
 * Instantiates an enemy ship
 *
 *@author Ethan Alabaster, Sam Pearson, Edward Poulter, Alexander Davis
 *@version 3.1
 */
public class EnemyShip extends SteerableEntity {
    private Sound destroy;
    private Sound hit;
    
    private StateMachine<EnemyShip, EnemyStateMachine> stateMachine;
    
    /**
     * Instantiates enemy ship
     *
     * @param screen Visual data
     * @param x x coordinates of entity
     * @param y y coordinates of entity
     * @param path path of texture file
     * @param assignment College ship is assigned to
     */
    public EnemyShip(GameScreen screen, float x, float y, String assignment) {
        super(screen, x, y);
        texture = new Texture(assignment + "_ship.png");
        college = assignment;
        setTimeFired(GdxAI.getTimepiece().getTime());
        setStateMachine(new DefaultStateMachine<EnemyShip, EnemyStateMachine>(this, EnemyStateMachine.SLEEP));
        
        //Set audio
        destroy = Gdx.audio.newSound(Gdx.files.internal("ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("ship-hit.wav"));
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
        } else {
        	getStateMachine().update();
        	setHealth(1000);
        	if (behavior != null) {
    			behavior.calculateSteering(steerOutput);
    			applySteering(steerOutput, delta);
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
    
    private void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
		
    	Vector2 la = steering.linear;
        float aa = steering.angular;
        
        getBody().setTransform(getPosition(), getOrientation() + aa * delta);
        getBody().setLinearVelocity((getBody().getLinearVelocity().len() + la.len() * delta) * (float) Math.cos(getOrientation()),
        					   		(getBody().getLinearVelocity().len() + la.len() * delta) * (float) Math.sin(getOrientation()));
        
        if(getBody().getLinearVelocity().len2() > maxLinearSpeed * maxLinearSpeed) {
    		getBody().setLinearVelocity(maxLinearSpeed * (float) Math.cos(getBody().getAngle()), 
    									maxLinearSpeed * (float) Math.sin(getBody().getAngle()));
    	}
        
        if(getBody().getAngularVelocity() > maxAngularSpeed) {
    		getBody().setAngularVelocity(maxAngularSpeed);
    	} else if (getBody().getAngularVelocity() < -maxAngularSpeed) {
    		getBody().setAngularVelocity(-maxAngularSpeed);
    	}
	}
    
    public void fire() {
        cannonBalls.add(new CannonFire(screen, getBody(), getPosition().x, getPosition().y, getOrientation() - (float) Math.PI / 2, 5));
        cannonBalls.add(new CannonFire(screen, getBody(), getPosition().x, getPosition().y, getOrientation() + (float) Math.PI / 2, 5));
   	
    }

    /**
     * Defines the ship as an enemy
     * Sets data to act as an enemy
     */
    @Override
    protected void defineEntity(float x, float y) {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;

        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 20 / PPM);
        fdef.shape = shape;
        //shape.dispose();
        
        // setting BIT identifier
        fdef.filter.categoryBits = ENEMY_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | COIN_BIT | ENEMY_BIT 
        		| COLLEGE_BIT | COLLEGESENSOR_BIT | COLLEGEFIRE_BIT;
        
        setBody(world.createBody(bdef));
        getBody().createFixture(fdef).setUserData(this);
    }

    /**
     * Constructs the ship batch
     *
     * @param batch The batch of visual data of the ship
     */
    public void draw(Batch batch) {
        if(!isDestroyed()) {
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

	public void setStateMachine(StateMachine<EnemyShip, EnemyStateMachine> stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		destroy.dispose();
		hit.dispose();
	}
}
