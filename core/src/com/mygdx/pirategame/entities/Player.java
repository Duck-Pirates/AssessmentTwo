package com.mygdx.pirategame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.screens.GameScreen;

import static com.mygdx.pirategame.configs.Constants.*;

/**
 * Creates the class of the player. Everything that involves actions coming from the player boat
 * @author Ethan Alabaster, Edward Poulter
 * @version 1.0
 */
public class Player extends SteerableEntity {
    private Sound breakSound;
    private Array<CannonFire> cannonBalls;
    public float velocity = 0;
    public float maxVelocity = 50;
    public float maxAngularVelocity = 2;
    
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

        // Sets cannonball array
        cannonBalls = new Array<CannonFire>();
        college = "Alcuin";
    }

    /**
     * Update the position of the player. Also updates any cannon balls the player generates
     *
     * @param delta Delta Time
     */
    public void update(float delta) {
        setPosition(getPosition().x - getWidth() / 2f, getPosition().y - getHeight()/2f);
        setRotation((float) (getOrientation() * 180 / Math.PI));

        // Updates cannonball data
        for(CannonFire ball : cannonBalls) {
            ball.update(delta);
            if(ball.isDestroyed())
                cannonBalls.removeValue(ball, true);
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
        body = world.createBody(bdef);

        // Defines a player's shape and contact borders
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50 / PPM);
        fdef.shape = shape;
        fdef.restitution = 0f;

        // setting BIT identifier
        fdef.filter.categoryBits = PLAYER_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | COIN_BIT | ENEMY_BIT 
        		| COLLEGE_BIT | COLLEGESENSOR_BIT | COLLEGEFIRE_BIT 
        		| POWERUP_BIT | CLOUDS_BIT;

        body.createFixture(fdef).setUserData(this);
    }



    public void updateVelocity(int linearAcceleration, float delta){

        setVelocity((getVelocity() +  (linearAcceleration * delta) * (1 - getVelocity() / maxVelocity)) * GameScreen.difficulty.getSpeedReduction());
        //Gdx.app.log("powerup1", Float.toString(velocity));
        //Gdx.app.log("powerup2", Float.toString(screen.difficulty.getMaxSpeed()));
        if (getVelocity() < -1.5f) {
            setVelocity(-1.5f);
        }
        else if (getVelocity() > GameScreen.difficulty.getMaxSpeed()){
            setVelocity(GameScreen.difficulty.getMaxSpeed());
        }
        setLinearVelocity(getVelocity());
    }

    public void slowDown(float delta){
        setVelocity((float) (getVelocity() * Math.pow(0.95f, delta * 20.0f)));
        //Gdx.app.log("Slowing down velocity:", String.valueOf(velocity));
        setLinearVelocity(getVelocity());
        //TODO slow down reverse
    }


    public void updateRotation(float angularAcceleration, float delta) {

        float angularVelocity = getAngularVelocity() + (angularAcceleration * delta) * (getVelocity() / maxAngularVelocity);
        if (angularVelocity < -5f) {
            angularVelocity = -5f;
        }
        // Increase rotation
        if (angularVelocity > 5f) {
            angularVelocity = 5;
        }

        if (angularVelocity > 0) {
            angularVelocity -= (angularVelocity / GameScreen.difficulty.getTraverseSpeed()); // change to update rotation
        } else if (angularVelocity < 0) {
            angularVelocity -= (angularVelocity / GameScreen.difficulty.getTraverseSpeed()); // change to update rotation
        }


        body.setAngularVelocity(angularVelocity);
    }

    public void updateRotation(int angularAcceleration, float delta) {
        updateRotation((float) angularAcceleration, delta);
    }

    public void setLinearVelocity(float newVelocity){
        float horizontalVelocity = -newVelocity * MathUtils.sin(getOrientation());
        float verticalVelocity = newVelocity * MathUtils.cos(getOrientation());
        body.setLinearVelocity(horizontalVelocity, verticalVelocity);
        velocity = newVelocity;
    }

    /**
     * Called when E is pushed. Causes 1 cannon ball to spawn on both sides of the ships with their relative velocity
     */
    public void fire() {

        cannonBalls.add(new CannonFire(screen, getPosition().x, getPosition().y, body, 5F, body.getAngle() - (float)Math.toRadians(180)));
        cannonBalls.add(new CannonFire(screen, getPosition().x, getPosition().y, body, -5, body.getAngle() - (float)Math.toRadians(-180)));
        if (GameScreen.difficulty.GetConeMec() == true){
            cannonBalls.add(new CannonFire(screen, getPosition().x, getPosition().y, body, 5F, body.getAngle() - (float)Math.toRadians(45)));
            cannonBalls.add(new CannonFire(screen, getPosition().x, getPosition().y, body, -5, body.getAngle() - (float)Math.toRadians(-45)));

            cannonBalls.add(new CannonFire(screen, getPosition().x, getPosition().y, body, 5F,  body.getAngle() -(float)Math.toRadians(225)));
            cannonBalls.add(new CannonFire(screen, getPosition().x, getPosition().y, body, -5, body.getAngle() - (float)Math.toRadians(-225)));
        }

        // Fires cannons




        // Cone fire below

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
		return velocity;
	}

	public void setVelocity(float velocity) {
        setLinearVelocity(velocity);
	}
}