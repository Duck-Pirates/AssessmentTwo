package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;


/**
 * Cannon Fire
 * Combat related cannon fire
 * Used by player and colleges,
 * Use should extend to enemy ships when implementing ship combat
 *
 * @author Ethan Alabaster
 * @version 1.0
 */
public class CannonFire extends Entity {
    private SteerableEntity shooter;
	private float stateTime;
    private final float angle;
    private final float velocity;
    private final Sound fireNoise;
    private final Vector2 bodyVel;
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
     * @param velocity velocity of the cannonball
     */
    public CannonFire(SteerableEntity shooter, GameScreen screen, Body body, float x, float y, float angle) {
    	super(screen, x, y);

        this.shooter = shooter;
        this.velocity = 5;
        this.world = screen.getWorld();

        // Sets the angle and velocity of the body
        this.angle = angle;
        bodyVel = body.getLinearVelocity();

        // Sets cannonBall's texture dimensions and position
        texture = new Texture("cannonBall.png");
        setRegion(texture);
        setBounds(x, y, 10 / PPM, 10 / PPM);

        // Sets sound for fire and play if on
        fireNoise = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        if (GameScreen.getGame().getPreferences().isEffectsEnabled()) {
            fireNoise.play(GameScreen.getGame().getPreferences().getEffectsVolume());
        }

    }

    /**
     * Defines the existence, direction, shape, size and collision logic of a cannonball
     *
     * @param x value of origin
     * @param y value of origin
     */
    public void defineEntity(float x, float y) {

        //sets the body definitions
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyDef.BodyType.DynamicBody;

        // Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / PPM);
        fdef.shape = shape;

        // Setting BIT identifier
        fdef.filter.categoryBits = CANNON_BIT;
        // Determining what this BIT can collide with
        fdef.filter.maskBits = ENEMY_BIT | PLAYER_BIT | COLLEGE_BIT;
        fdef.isSensor = true;

        getBody().createFixture(fdef).setUserData(this);

    }

    /**
     * Updates state with delta time
     * Defines range of cannon fire
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void update(float delta){
    	
    	if(!fired) {
    		// Velocity maths
            float velX = MathUtils.cos(angle) * velocity + bodyVel.x;
            float velY = MathUtils.sin(angle) * velocity + bodyVel.y;
            getBody().applyLinearImpulse(new Vector2(velX, velY), getBody().getWorldCenter(), true);
            fired = true;
    	}
    	
        stateTime += delta;
        // Update position of ball
        setPosition(getBody().getPosition().x - getWidth() / 2, getBody().getPosition().y - getHeight() / 2);

        // If ball is set to destroy and isn't, destroy it
        if(stateTime > 1.4f || isSetToDestroy()) {
            world.destroyBody(getBody());
            destroyed = true;
        }

    }

    /**
     * (Not Used)
     *
     * Defines contact
     */
	@Override
	public void onContact() {}

    /**
     * Dispose the CannonFire data
     */
	@Override
	public void dispose() {

		super.dispose();
		fireNoise.dispose();

    }

	public SteerableEntity getShooter() {
		return shooter;
	}
}
