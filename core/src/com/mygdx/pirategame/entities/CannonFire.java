package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.CANNON_BIT;
import static com.mygdx.pirategame.configs.Constants.COLLEGE_BIT;
import static com.mygdx.pirategame.configs.Constants.ENEMY_BIT;
import static com.mygdx.pirategame.configs.Constants.PLAYER_BIT;
import static com.mygdx.pirategame.configs.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.pirategame.screens.GameScreen;

/**
 * Cannon Fire
 * Combat related cannon fire
 * Used by player and colleges,
 * Use should extend to enemy ships when implementing ship combat
 *
 *@author Ethan Alabaster
 *@version 1.0
 */
public class CannonFire extends Entity {
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private float angle;
    private float velocity;
    private Sound fireNoise;
    private Vector2 bodyVel;
    private boolean fired = false;

    /**
     * Instantiates cannon fire
     * Determines general cannonball data
     * Determines firing sound
     *
     * @param screen visual data
     * @param x x value of origin
     * @param y y value of origin
     * @param body body of origin
     * @param velocity velocity of the cannon ball
     */
    public CannonFire(GameScreen screen, Body body, float x, float y, float angle, float velocity) {
    	super(screen, x, y);
        this.velocity = velocity;
        this.world = screen.getWorld();
        //sets the angle and velocity
        this.angle = angle;
        bodyVel = body.getLinearVelocity();
        //set cannonBall dimensions for the texture
        texture = new Texture("cannonBall.png");
        setRegion(texture);
        setBounds(x, y, 10 / PPM, 10 / PPM);
        //set sound for fire and play if on
        fireNoise = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        if (GameScreen.game.getPreferences().isEffectsEnabled()) {
            fireNoise.play(GameScreen.game.getPreferences().getEffectsVolume());
        }
    }

    /**
     * Defines the existance, direction, shape and size of a cannonball
     */
    public void defineEntity(float x, float y) {
        //sets the body definitions
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        //Sets collision boundaries
        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / PPM);

        // setting BIT identifier
        fDef.filter.categoryBits = CANNON_BIT;
        // determining what this BIT can collide with
        fDef.filter.maskBits = ENEMY_BIT | PLAYER_BIT | COLLEGE_BIT;
        fDef.shape = shape;
        fDef.isSensor = true;
        body.createFixture(fDef).setUserData(this);
    }

    /**
     * Updates state with delta time
     * Defines range of cannon fire
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float delta){
    	
    	if(!fired) {
    		//Velocity maths
            float velX = MathUtils.cos(angle) * velocity + bodyVel.x;
            float velY = MathUtils.sin(angle) * velocity + bodyVel.y;
            body.applyLinearImpulse(new Vector2(velX, velY), body.getWorldCenter(), true);
            fired = true;
    	}
    	
        stateTime += delta;
        //Update position of ball
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

        //If ball is set to destroy and isnt, destroy it
        if((setToDestroy) && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
        // determines cannonball range
        if(stateTime > 0.98f) {
            setToDestroy();
        }
    }

    /**
     * Changes destruction state
     */
    public void setToDestroy(){
        setToDestroy = true;
    }

    /**
     * Returns destruction status
     */
    public boolean isDestroyed(){
        return destroyed;
    }

	@Override
	public void onContact() {
		// TODO Auto-generated method stub
		
	}
}
