package com.mygdx.pirategame.college;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.screens.GameScreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * College Fire
 * Defines college attack method
 * Defines college cannonball projectiles
 *
 * @author Ethan Alabaster
 * @version 1.0
 */

public class CollegeFire extends Sprite {

    private final World world;
    private Body b2body;
    private final Vector2 playerPos;

    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;

    /**
     * College Fire constructor
     *
     * @param screen Visual data
     * @param x x position of player
     * @param y y position of player
     */
    public CollegeFire(GameScreen screen, float x, float y) {

        this.world = screen.getWorld();
        playerPos = screen.getPlayerPosition();

        //Set the texture, the position and the size of the ball
        setRegion( new Texture("cannonBall.png"));
        setBounds(x, y, 10 / PPM, 10 / PPM);

        defineCannonBall();

    }

    /**
     * Defines cannonball data, shape and collision logic
     */
    public void defineCannonBall() {

        // Sets the body definitions
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        // Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / PPM);
        fdef.shape = shape;

        // Setting BIT identifier
        fdef.filter.categoryBits = COLLEGEFIRE_BIT;
        // Determining what this BIT can collide with
        fdef.filter.maskBits = PLAYER_BIT;

        // Creates the cannonballs fixture
        b2body.createFixture(fdef).setUserData(this);

        // Math for firing the cannonball at the player
        playerPos.sub(b2body.getPosition());
        playerPos.nor();
        float speed = 5f;
        b2body.setLinearVelocity(playerPos.scl(speed));

    }

    /**
     * Updates state with delta time and also defines when the cannon fire should be destroyed by the game
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void update(float delta){

        stateTime += delta;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // If College Fire is set to destroy and isn't, destroy it
        if((setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }

        // Check to see if the cannonball has been in the game for more than 2 seconds (the limit)
        if(stateTime > 2f) {
            setToDestroy();
        }

    }

    /**
     * Changes destruction state
     */
    public void setToDestroy(){
        setToDestroy = true;
    }

}
