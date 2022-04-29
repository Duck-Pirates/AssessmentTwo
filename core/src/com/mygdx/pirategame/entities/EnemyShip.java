package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.CANNON_BIT;
import static com.mygdx.pirategame.configs.Constants.DEFAULT_BIT;
import static com.mygdx.pirategame.configs.Constants.ENEMY_BIT;
import static com.mygdx.pirategame.configs.Constants.PLAYER_BIT;
import static com.mygdx.pirategame.configs.Constants.PPM;

import com.badlogic.gdx.Gdx;
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
    
    public StateMachine<EnemyShip, EnemyStateMachine> stateMachine;
    
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
        texture = new Texture(path);
        college = assignment;
        
        stateMachine = new DefaultStateMachine<EnemyShip, EnemyStateMachine>(this, EnemyStateMachine.SLEEP);
        
        //Set audio
        destroy = Gdx.audio.newSound(Gdx.files.internal("ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("ship-hit.wav"));
        //Set the position and size of the college
        setBounds(0, 0, 64 / PPM, 110 / PPM);
        setRegion(texture);
        setOrigin(32 /PPM, 55 / PPM);

        damage = GameScreen.difficulty.getDamageDealt();
    }

    /**
     * Updates the state of each object with delta time
     * Checks for ship destruction
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void update(float delta) {
        //If ship is set to destroy and isn't, destroy it
        if(setToDestroy && !destroyed) {
            //Play death noise
            if (GameScreen.game.getPreferences().isEffectsEnabled()) {
                destroy.play(GameScreen.game.getPreferences().getEffectsVolume());
            }
            world.destroyBody(body);
            destroyed = true;
            
            //Change player coins and points
            Hud.changePoints(30);
            Hud.changeCoins(10);
            
            texture.dispose();
            destroy.dispose();
            hit.dispose();
        } else if(!destroyed) {
            
        	stateMachine.update();
        	
        	if (behavior != null) {
    			behavior.calculateSteering(steerOutput);
    			applySteering(steerOutput, delta);
    		}
        	
            setRotation((float) Math.toDegrees(body.getAngle()) - 90);
            setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight()/2f);
            
            bar.update();
        }
        if(health <= 0) {
            setToDestroy = true;
        }
    }
    
    private void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
		
    	Vector2 la = steering.linear;
        float aa = steering.angular;
        
        body.setTransform(body.getPosition(), body.getAngle() + aa * delta);
        body.setLinearVelocity((body.getLinearVelocity().len() + la.len() * delta) * (float) Math.cos(body.getAngle()),
        					   (body.getLinearVelocity().len() + la.len() * delta) * (float) Math.sin(body.getAngle()));
        
        if(body.getLinearVelocity().len2() > maxLinearSpeed * maxLinearSpeed) {
    		// Int x and y are used to preserve direction of travel
    		int x = 1;
    		int y = 1;
    		if(body.getLinearVelocity().x < 0) {
    			x = -1;
    		}
    		if(body.getLinearVelocity().y < 0) {
    			y = -1;
    		}
    		body.setLinearVelocity(maxLinearSpeed * (float) Math.cos(body.getAngle()) * x, 
    							   maxLinearSpeed * (float) Math.sin(body.getAngle()) * y);
    	}
        if(body.getAngularVelocity() > maxAngularSpeed) {
    		body.setAngularVelocity(maxAngularSpeed);
    	} else if (body.getAngularVelocity() < -maxAngularSpeed) {
    		body.setAngularVelocity(-maxAngularSpeed);
    	}
	}
    
    public void fire() {
        cannonBalls.add(new CannonFire(screen, body, body.getPosition().x, body.getPosition().y,
        		body.getAngle() - (float) Math.PI / 2, 5));
        cannonBalls.add(new CannonFire(screen, body, body.getPosition().x, body.getPosition().y,
        		body.getAngle() - (float) Math.PI / 2, -5));
    	
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
        shape.dispose();
        
        // setting BIT identifier
        fdef.filter.categoryBits = ENEMY_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | PLAYER_BIT | ENEMY_BIT | CANNON_BIT;
        
        body = world.createBody(bdef);
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
        if (GameScreen.game.getPreferences().isEffectsEnabled()) {
            hit.play(GameScreen.game.getPreferences().getEffectsVolume());
        }
        //Deal with the damage
        health -= GameScreen.difficulty.getDamageDealt();
        bar.changeHealth(GameScreen.difficulty.getDamageDealt());
        Hud.changePoints(5);
    }
}
