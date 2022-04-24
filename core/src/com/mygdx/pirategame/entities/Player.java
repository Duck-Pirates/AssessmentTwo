package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.PirateGame.PPM;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.screens.GameScreen;

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
    	super(screen, 1200  / PirateGame.PPM, 2500 / PirateGame.PPM);
        // Creates ship texture
        texture = new Texture("player_ship.png");
        
        setBounds(0,0,64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(texture);
        setOrigin(32 / PirateGame.PPM,55 / PirateGame.PPM);
	    
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
        if (screen.game.getPreferences().isEffectsEnabled()) {
            breakSound.play(screen.game.getPreferences().getEffectsVolume());
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
            angularVelocity -= (angularVelocity / screen.difficulty.getTraverseSpeed()); // change to update rotation
        } else if (angularVelocity < 0) {
            angularVelocity -= (angularVelocity / screen.difficulty.getTraverseSpeed()); // change to update rotation
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
		this.velocity = velocity;
	}
}
