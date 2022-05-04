package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Creates the class of the player. Everything that involves actions coming from the player boat
 * @author Ethan Alabaster, Edward Poulter
 * @version 1.0
 */
public class Player extends SteerableEntity {
    
    /**
     * Instantiates a new Player. Constructor only called once per game
     *
     * @param screen visual data
     */
    public Player(GameScreen screen) {

    	super(screen, 1200  / PPM, 2500 / PPM);
        
    	college = "alcuin";

    	// Creates ship texture
        texture = new Texture(college + "_ship.png");

        // Sets bounds and origin of the Player's texture
        setBounds(0,0,64 / PPM, 110 / PPM);
        setRegion(texture);
        setOrigin(32 / PPM,55 / PPM);

    }

    /**
     * Updates the position of the player. Also updates any cannonballs the player generates
     *
     * @param delta Delta Time
     */
    public void update(float delta) {

    	SteerableEntity.maxLinearSpeed = GameScreen.getDifficulty().getMaxSpeed() / PPM;

    	updateMovement(delta);

        setRotation((float) Math.toDegrees(getOrientation()) - 90);
        setPosition(getPosition().x - getWidth() / 2f, getPosition().y - getHeight()/2f);
        
        // Cannon fire on 'Space'
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && GdxAI.getTimepiece().getTime() - getTimeFired() > 1f) {
            fire();
            setTimeFired(GdxAI.getTimepiece().getTime());
        }
        
        // Updates cannonball data
        for(CannonFire ball : cannonBalls) {
            ball.update(delta);
            if(ball.isDestroyed()) {
                cannonBalls.removeValue(ball, true);
            	ball.dispose();
            }
        }

    }
    
    /**
     * Updates the force applied to the player using the equation f = (a - cu)
     * f - Force
     * a - Maximum acceleration
     * c - Constant
     * u - Current velocity of the player
     * 
     * NOTE: this equation is using f = ma as its base assuming m = 1
     */
    public void updateMovement(float delta){

    	int linearDirection = 0;
    	int angularDirection = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        	linearDirection++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        	linearDirection -= 5;
        }
    	if (Gdx.input.isKeyPressed(Input.Keys.A)) {
    		angularDirection++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        	angularDirection--;
        }
        
    	float la;
        float af;
        
        if(linearDirection != 0) {
        	la  = maxLinearAcceleration * linearDirection;
        } else {
        	la = -1f * maxLinearAcceleration * velocity / maxLinearSpeed;
        }
        
        if(angularDirection != 0) {
        	af = maxAngularAcceleration * angularDirection;
        } else {
        	af = -1f * maxAngularAcceleration * getBody().getAngularVelocity() / maxAngularSpeed;
        }

        // Applies forces to body
        getBody().applyTorque(af, true);
        velocity = velocity + (la * delta) * (1 - velocity / maxLinearSpeed);
    	if (velocity < -0.5f)
    		velocity = -0.5f;

    	body.setLinearVelocity(velocity * MathUtils.cos(getOrientation()), velocity * MathUtils.sin(getOrientation()));
    	
    	// limits angular speed
        if(getBody().getAngularVelocity() > maxAngularSpeed) {
    		getBody().setAngularVelocity(maxAngularSpeed);
    	} else if (getBody().getAngularVelocity() < -maxAngularSpeed) {
    		getBody().setAngularVelocity(-maxAngularSpeed);
    	}

    }

    /**
     * Plays the break sound when a boat takes damage
     */
    public void playBreakSound() {

        // Plays damage sound effect
        if (GameScreen.getGame().getPreferences().isEffectsEnabled()) {
            hit.play(GameScreen.getGame().getPreferences().getEffectsVolume());
        }

    }

    /**
     * Defines all the parts of the player's physical model, and it sets it up for collisions too
     *
     * @param x value of origin
     * @param y value of origin
     */
    @Override
    protected void defineEntity(float x, float y) {

        // Defines a players position
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y); // Default Pos: 1800,2500
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        // Defines a player's shape and contact borders
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 20 / PPM);
        fdef.shape = shape;
        fdef.restitution = 0f;
        
        // Setting BIT identifier
        fdef.filter.categoryBits = PLAYER_BIT;
        // Determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | COIN_BIT | ENEMY_BIT 
        		| COLLEGE_BIT | COLLEGESENSOR_BIT | COLLEGEFIRE_BIT 
        		| POWERUP_BIT | CLOUDS_BIT | CANNON_BIT;

        body.createFixture(fdef).setUserData(this);

    }

    /**
     * Called when E is pushed. Causes 1 cannonball to spawn on both sides of the ships with their relative velocity
     */
    @Override
    public void fire() {

        if (GameScreen.getDifficulty().getConeMec()){
            cannonBalls.add(new CannonFire(this, screen, getBody(), getPosition().x, getPosition().y, getOrientation() - (float) Math.PI / 4));
            cannonBalls.add(new CannonFire(this, screen, getBody(), getPosition().x, getPosition().y, getOrientation() + (float) Math.PI / 4));

            cannonBalls.add(new CannonFire(this, screen, getBody(), getPosition().x, getPosition().y, getOrientation() - MathUtils.PI * 3 / 4));
            cannonBalls.add(new CannonFire(this, screen, getBody(), getPosition().x, getPosition().y, getOrientation() + MathUtils.PI * 3 / 4));
        }
        else{
            super.fire();
        }

    }

    /**
     * Defines contact
     */
    @Override
    public void onContact() {

    	//Play collision sound
        if (GameScreen.getGame().getPreferences().isEffectsEnabled()) {
            hit.play(GameScreen.getGame().getPreferences().getEffectsVolume());
        }

        //Deal with the damage
        Hud.changeHealth(-GameScreen.getDifficulty().getDamageDealt());
    }
    
	public Body getBody() {
		return body;
	}

}
