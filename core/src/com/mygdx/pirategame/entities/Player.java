package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.COIN_BIT;
import static com.mygdx.pirategame.configs.Constants.COLLEGEFIRE_BIT;
import static com.mygdx.pirategame.configs.Constants.COLLEGESENSOR_BIT;
import static com.mygdx.pirategame.configs.Constants.COLLEGE_BIT;
import static com.mygdx.pirategame.configs.Constants.DEFAULT_BIT;
import static com.mygdx.pirategame.configs.Constants.ENEMY_BIT;
import static com.mygdx.pirategame.configs.Constants.PLAYER_BIT;
import static com.mygdx.pirategame.configs.Constants.POWERUP_BIT;
import static com.mygdx.pirategame.configs.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.pirategame.screens.GameScreen;

/**
 * Creates the class of the player. Everything that involves actions coming from the player boat
 * @author Ethan Alabaster, Edward Poulter
 * @version 1.0
 */
public class Player extends SteerableEntity {
    private Sound breakSound;
    
    /**
     * Instantiates a new Player. Constructor only called once per game
     *
     * @param screen visual data
     */
    public Player(GameScreen screen) {
    	super(screen, 1200  / PPM, 2500 / PPM);
        // Creates ship texture
        texture = new Texture("player_ship.png");
        
        setBounds(0,0,64 / PPM, 110 / PPM);
        setRegion(texture);
        setOrigin(32 / PPM,55 / PPM);
	    
        // Sound effect for damage
        breakSound = Gdx.audio.newSound(Gdx.files.internal("wood-bump.mp3"));

        college = "Alcuin";
    }

    /**
     * Update the position of the player. Also updates any cannon balls the player generates
     *
     * @param delta Delta Time
     */
    public void update(float delta) {
    	updateMovement();
    	
        setRotation((float) Math.toDegrees(body.getAngle()) - 90);
        setPosition(getPosition().x - getWidth() / 2f, getPosition().y - getHeight()/2f);
        
        // Cannon fire on 'Space'
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fire();
        }
        
        // Updates cannonball data
        for(CannonFire ball : cannonBalls) {
            ball.update(delta);
            if(ball.isDestroyed()) {
                cannonBalls.removeValue(ball, true);
            }
        }
    }
    
    /**
     * updates the force applied to the player using the equation f = (a - cu)
     * f - Force
     * a - Maximum acceleration
     * c - Constant
     * u - Current velocity of the player
     * 
     * NOTE: this equation is using f = ma as its base assuming m = 1
     * 
     * @param delta
     */
    public void updateMovement(){

    	int linearDirection = 0;
    	int angularDirection = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        	linearDirection++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        	linearDirection--;
        }
    	if (Gdx.input.isKeyPressed(Input.Keys.A)) {
    		angularDirection++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        	angularDirection--;
        }
        
    	float lf;
        float af;
        
        if(linearDirection != 0) {
        	lf  = maxLinearAcceleration * linearDirection;
        } else {
        	lf = -1f * maxLinearAcceleration * body.getLinearVelocity().len() / maxLinearSpeed;
        }
        
        if(angularDirection != 0) {
        	af = maxAngularAcceleration * angularDirection;
        } else {
        	af = -1f * maxAngularAcceleration * body.getAngularVelocity() / maxAngularSpeed;
        }
        
    	body.applyForceToCenter(lf * (float) Math.cos(body.getAngle()), lf * (float) Math.sin(body.getAngle()), true);
        body.applyTorque(af, true);
        
        body.setLinearVelocity(body.getLinearVelocity().len() * (float) Math.cos(body.getAngle()),
        					   body.getLinearVelocity().len() * (float) Math.sin(body.getAngle()));
    	
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

    /**
     * Plays the break sound when a boat takes damage
     */
    public void playBreakSound() {
        // Plays damage sound effect
        if (GameScreen.game.getPreferences().isEffectsEnabled()) {
            breakSound.play(GameScreen.game.getPreferences().getEffectsVolume());
        }
    }

    /**
     * Defines all the parts of the player's physical model. Sets it up for collisons
     */
    @Override
    protected void defineEntity(float x, float y) {
        // Defines a players position
        BodyDef bdef = new BodyDef();
        bdef.position.set(new Vector2(x, y)); // Default Pos: 1800,2500
        bdef.type = BodyDef.BodyType.DynamicBody;
        
        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 20 / PPM);
        fdef.shape = shape;
        shape.dispose();
        
        // setting BIT identifier
        fdef.filter.categoryBits = PLAYER_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | COIN_BIT | ENEMY_BIT 
        		| COLLEGE_BIT | COLLEGESENSOR_BIT | COLLEGEFIRE_BIT 
        		| POWERUP_BIT;

        body = world.createBody(bdef);
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Called when E is pushed. Causes 1 cannon ball to spawn on both sides of the ships with their relative velocity
     */
    public void fire() {
        // Fires cannons
        cannonBalls.add(new CannonFire(screen, body, body.getPosition().x, body.getPosition().y,
        		body.getAngle() - (float) Math.PI / 2, 5));
        cannonBalls.add(new CannonFire(screen, body, body.getPosition().x, body.getPosition().y,
        		body.getAngle() - (float) Math.PI / 2, -5));
    }
    
    @Override
    public void onContact() {}

    /**
     * Draws the player using batch
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
    
	public Body getBody() {
		return body;
	}

	public float getVelocity() {
		return this.getLinearVelocity().len();
	}
}
