package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Creates the class of the player. Everything that involves actions coming from the player boat
 * @author Ethan Alabaster, Edward Poulter
 * @version 1.0
 */
public class Player extends Sprite {
    private GameScreen screen;
    private Texture ship;
    public World world;
    public Body b2body;
    private Sound breakSound;
    private Array<CannonFire> cannonBalls;
    protected float velocity = 0;
    protected float maxVelocity = 50;
    protected float maxAngularVelocity = 2;

    /**
     * Instantiates a new Player. Constructor only called once per game
     *
     * @param screen visual data
     */

    public Player(GameScreen screen, Vector2 position, Float angle){

        // Retrieves world data and creates ship texture
        this.screen = screen;
        ship = new Texture("player_ship.png");
        this.world = screen.getWorld();

        // Defines a player, and the players position on screen and world
        definePlayer(position, angle);
        setBounds(0,0,64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(ship);
        setOrigin(32 / PirateGame.PPM,55 / PirateGame.PPM);

        // Sound effect for damage
        breakSound = Gdx.audio.newSound(Gdx.files.internal("wood-bump.mp3"));

        // Sets cannonball array
        cannonBalls = new Array<CannonFire>();

    }

    /**
     * Update the position of the player. Also updates any cannon balls the player generates
     *
     * @param dt Delta Time
     */
    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight()/2f);
        setRotation((float) (b2body.getAngle() * 180 / Math.PI));

        // Updates cannonball data
        for(CannonFire ball : cannonBalls) {
            ball.update(dt);
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
    private void definePlayer(Vector2 position, float angle) {
        // Defines a players position
        BodyDef bdef = new BodyDef();
        bdef.position.set(position); // Default Pos: 1800,2500
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.angle = (float) (angle * Math.PI/180);
        b2body = world.createBody(bdef);

        // Defines a player's shape and contact borders
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(55 / PirateGame.PPM);

        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.PLAYER_BIT;

        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.COIN_BIT | PirateGame.ENEMY_BIT | PirateGame.COLLEGE_BIT | PirateGame.COLLEGESENSOR_BIT | PirateGame.COLLEGEFIRE_BIT | PirateGame.POWERUP_BIT | PirateGame.TORNADO_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }



    public void updateVelocity(int linearAcceleration, float delta){

        velocity = (velocity +  (linearAcceleration * delta) * (1 - velocity / maxVelocity)) * screen.difficulty.getSpeedReduction();
        //Gdx.app.log("powerup1", Float.toString(velocity));
        //Gdx.app.log("powerup2", Float.toString(screen.difficulty.getMaxSpeed()));
        if (velocity < -1.5f) {
            velocity = -1.5f;
        }
        else if (velocity > screen.difficulty.getMaxSpeed()){
            velocity = screen.difficulty.getMaxSpeed();
        }
        setLinearVelocity(velocity);
    }

    public void slowDown(float delta){
        velocity *= Math.pow(0.95f, delta * 20.0f);
        //Gdx.app.log("Slowing down velocity:", String.valueOf(velocity));
        setLinearVelocity(velocity);
        //TODO slow down reverse
    }


    public void updateRotation(float angularAcceleration, float delta) {

        float angularVelocity = b2body.getAngularVelocity() + (angularAcceleration * delta) * (velocity / maxAngularVelocity);
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


        b2body.setAngularVelocity(angularVelocity);
    }

    public void updateRotation(int angularAcceleration, float delta) {
        updateRotation((float) angularAcceleration, delta);
    }

    public void setLinearVelocity(float newVelocity){
        float horizontalVelocity = -newVelocity * MathUtils.sin(b2body.getAngle());
        float verticalVelocity = newVelocity * MathUtils.cos(b2body.getAngle());
        b2body.setLinearVelocity(horizontalVelocity, verticalVelocity);
    }








    /**
     * Called when E is pushed. Causes 1 cannon ball to spawn on both sides of the ships with their relative velocity
     */
    public void fire() {
        // Fires cannons
        cannonBalls.add(new CannonFire(screen, b2body.getPosition().x, b2body.getPosition().y, b2body, 5));
        cannonBalls.add(new CannonFire(screen, b2body.getPosition().x, b2body.getPosition().y, b2body, -5));

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
}
