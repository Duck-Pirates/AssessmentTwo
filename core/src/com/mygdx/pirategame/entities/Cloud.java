package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.screens.GameScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;



/**
 * Cloud
 * Creates an object for each cloud
 * Extends the entity class to define cloud as an entity
 *
 * @author Davide Bressani
 * @version 1.0
 */
public class Cloud extends Entity {

    private float state;
    private final Animation<?> cloudAnimation;
    private float alpha;
    private int dimension;
    private boolean inContact = false, endReset = true;

    /**
     * Instantiates a new Cloud.
     *
     * @param screen the screen is going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     */
    public Cloud(GameScreen screen, float x, float y) {

        super(screen, x, y);

        // Set cloud image and animation
        TextureRegion[][] slicedAsset = new TextureRegion(new Texture("clouds.png")).split(2048, 1256);
        List<TextureRegion> list = new ArrayList<>(Arrays.asList(slicedAsset[0]));
        list.addAll(Arrays.asList(slicedAsset[1]));
        int startIndex = new Random().nextInt(4); // This is to randomize the cloud animation
        setRegion(list.get(startIndex));
        state = (startIndex * 0.25f) + 0.1f;
        cloudAnimation = new Animation<>(0.25f,  list.toArray());

        // Set the position and size of the cloud
        setBounds(0,0,dimension / PPM, dimension * (0.61328125f) / PPM);

        // Sets origin of the cloud
        setOrigin(24 / PPM,24 / PPM);

        alpha = 0.7f;
    }

    /**
     * Defines an entity for the cloud
     *
     * @param x value of origin
     * @param y value of origin
     */
    @Override
    protected void defineEntity(float x, float y) {

        // Sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        // Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Random rand = new Random();
        dimension = rand.nextInt(301-200)+200;
        shape.setAsBox(dimension/2/PPM, dimension * (0.61328125f) /2/PPM);

        // Setting BIT identifier
        fdef.filter.categoryBits = CLOUDS_BIT;

        // Determining what this BIT can collide with
        fdef.filter.maskBits = PLAYER_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;

        body.createFixture(fdef).setUserData(this);

    }

    /**
     * (Not Used)
     *
     * Defines contact
     */
    @Override
    public void onContact() {
        inContact = true;
    }

    /**
     * Updates the position and the animation frame of the cloud
     *
     * @param delta time elapsed from last game frame
     */
    public void update(float delta){

        setPosition(getBody().getPosition().x - getWidth() / 2f, getBody().getPosition().y - getHeight() / 2f);

        if(state % 0.15f < 0.10f){ // Checks if the cloud should be moving in a direction
            this.getBody().applyForce(new Vector2(-0.03f,-0.03f), this.getBody().getWorldCenter(), false);
        }
        setRegion(getFrame(delta)); // Sets the new frame for the cloud's animation

        if(inContact){ // Checks if the alpha level should be changed (this is for the transparency effect)
            changeAlpha();
        }
        else if(!endReset){
            resetAlpha();
        }

    }

    /**
     * Changes the alpha value (transparency) of the cloud if the player is getting closer
     */
    public void changeAlpha() {

        this.alpha = this.alpha*0.99f;
        if (this.alpha < 0.5f){
            inContact = false;
            this.alpha = 0.5f;
        }

    }

    /**
     * Slowly resets the alpha value of the cloud when the player is leaving the cloud behind
     */
    public void resetAlpha(){

        inContact = false;
        endReset = false;
        this.alpha = this.alpha*1.005f;
        if (this.alpha > 0.9f){
            endReset = true;
            this.alpha = 0.9f;
        }

    }

    /**
     * Draws the cloud onto the screen
     *
     * @param batch The game's sprite batch
     */
    public void draw(Batch batch) {

        super.setAlpha(alpha);
        super.draw(batch);

    }

    /**
     * Getter for the Cloud's animation frame
     *
     * @param dt time elapsed from last game frame
     */
    public TextureRegion getFrame(float dt){

        TextureRegion region;
        state += dt;
        region = (TextureRegion) cloudAnimation.getKeyFrame(state, true);
        return region;

    }

}
