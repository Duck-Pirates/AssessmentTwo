package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.PirateGame.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.screens.GameScreen;

/**
 * Creates the class of the player. Everything that involves actions coming from the player boat
 * @author Ethan Alabaster, Edward Poulter
 * @version 1.0
 */
public class Player extends Sprite implements Steerable<Vector2> {
    private final GameScreen screen;
    private Texture ship;
    public World world;
    private Sound breakSound;
    private Array<CannonFire> cannonBalls;
    private float velocity = 0;
    protected float maxVelocity = 50;
    protected float maxAngularVelocity = 2;
    
    private Body body;
    private float zeroLinearSpeedThreshold = 0.01f;
    private float maxLinearSpeed, maxLinearAcceleration;
    private float maxAngularSpeed, maxAngularAcceleration;
    private float boundingRadius;
    private boolean tagged;
    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steerOutput;
    
    /**
     * Instantiates a new Player. Constructor only called once per game
     *
     * @param screen visual data
     */
    public Player(GameScreen screen) {
        // Retrieves world data and creates ship texture
        this.screen = screen;
        ship = new Texture("player_ship.png");
        this.world = screen.getWorld();

        // Defines a player, and the players position on screen and world
        definePlayer();
        setBounds(0,0,64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(ship);
        setOrigin(32 / PirateGame.PPM,55 / PirateGame.PPM);
        
		zeroLinearSpeedThreshold = 0.1f;
	    maxLinearSpeed = 50f;
	    maxLinearAcceleration = 10f;
	    maxAngularSpeed = 50f;
	    maxAngularAcceleration = 10f;
	    boundingRadius = 55 / PPM;
	    tagged = false;
	    
	    steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
        // Sound effect for damage
        breakSound = Gdx.audio.newSound(Gdx.files.internal("wood-bump.mp3"));

        // Sets cannonball array
        cannonBalls = new Array<CannonFire>();
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
        if (screen.game.getPreferences().isEffectsEnabled()) {
            breakSound.play(screen.game.getPreferences().getEffectsVolume());
        }
    }

    /**
     * Defines all the parts of the player's physical model. Sets it up for collisons
     */
    private void definePlayer() {
        // Defines a players position
        BodyDef bdef = new BodyDef();
        bdef.position.set(1200  / PirateGame.PPM, 2500 / PirateGame.PPM); // Default Pos: 1800,2500
        bdef.type = BodyDef.BodyType.DynamicBody;

        // Defines a player's shape and contact borders
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50 / PPM);
        fdef.shape = shape;
        shape.dispose();

        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.PLAYER_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.COIN_BIT | PirateGame.ENEMY_BIT 
        		| PirateGame.COLLEGE_BIT | PirateGame.COLLEGESENSOR_BIT | PirateGame.COLLEGEFIRE_BIT 
        		| PirateGame.POWERUP_BIT;

        body = world.createBody(bdef);
        body.createFixture(fdef).setUserData(this);
    }

    public void updateVelocity(int linearAcceleration, float delta){

        setVelocity((getVelocity() +  (linearAcceleration * delta) * (1 - getVelocity() / maxVelocity)) * screen.difficulty.getSpeedReduction());
        //Gdx.app.log("powerup1", Float.toString(velocity));
        //Gdx.app.log("powerup2", Float.toString(screen.difficulty.getMaxSpeed()));
        if (getVelocity() < -1.5f) {
            setVelocity(-1.5f);
        }
        else if (getVelocity() > screen.difficulty.getMaxSpeed()){
            setVelocity(screen.difficulty.getMaxSpeed());
        }
        setLinearVelocity(getVelocity());
    }

    public void slowDown(float delta){
        setVelocity((float) (getVelocity() * Math.pow(0.95f, delta * 20.0f)));
        //Gdx.app.log("Slowing down velocity:", String.valueOf(velocity));
        setLinearVelocity(getVelocity());
        //TODO slow down reverse
    }


    public void updateRotation(int angularAcceleration, float delta) {

        float angularVelocity = getAngularVelocity() + (angularAcceleration * delta) * (getVelocity() / maxAngularVelocity);
        if (angularVelocity < -5f) {
            angularVelocity = -5f;
        }
        // Increase rotation
        if (angularVelocity > 5f) {
            angularVelocity = 5;
        }

        if (angularVelocity > 0) {
            angularVelocity -= (angularVelocity / screen.difficulty.getTraverseSpeed()); // change to update rotation
        } else if (angularVelocity < 0) {
            angularVelocity -= (angularVelocity / screen.difficulty.getTraverseSpeed()); // change to update rotation
        }


        body.setAngularVelocity(angularVelocity);
    }

    public void setLinearVelocity(float newVelocity){
        float horizontalVelocity = -newVelocity * MathUtils.sin(getOrientation());
        float verticalVelocity = newVelocity * MathUtils.cos(getOrientation());
        body.setLinearVelocity(horizontalVelocity, verticalVelocity);
    }

    /**
     * Called when E is pushed. Causes 1 cannon ball to spawn on both sides of the ships with their relative velocity
     */
    public void fire() {
        // Fires cannons
        cannonBalls.add(new CannonFire(screen, getPosition().x, getPosition().y, body, 5));
        cannonBalls.add(new CannonFire(screen, getPosition().x, getPosition().y, body, -5));

        // Cone fire below
        /*cannonBalls.add(new CannonFire(screen, b2body.getPosition().x, b2body.getPosition().y, (float) (b2body.getAngle() - Math.PI / 6), -5, b2body.getLinearVelocity()));
        cannonBalls.add(new CannonFire(screen, b2body.getPosition().x, b2body.getPosition().y, (float) (b2body.getAngle() - Math.PI / 6), 5, b2body.getLinearVelocity()));
        cannonBalls.add(new CannonFire(screen, b2body.getPosition().x, b2body.getPosition().y, (float) (b2body.getAngle() + Math.PI / 6), -5, b2body.getLinearVelocity()));
        cannonBalls.add(new CannonFire(screen, b2body.getPosition().x, b2body.getPosition().y, (float) (b2body.getAngle() + Math.PI / 6), 5, b2body.getLinearVelocity()));
        }
         */
    }

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

	public SteeringBehavior<Vector2> getBehavior() {
		return behavior;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
}
