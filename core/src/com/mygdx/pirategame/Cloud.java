package com.mygdx.pirategame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Cloud
 * Creates an object for each cloud
 * Extends the entity class to define cloud as an entity
 *
 * @author Davide Bressani
 * @version 1.0
 */

public class Cloud extends Entity{

    private float state;
    private Texture cloud;
    private Animation cloudAnimation;
    private float alpha;
    Random rand = new Random();

    /**
     * Instantiates a new Cloud.
     *
     * @param screen the screen its going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     */

    public Cloud(GameScreen screen, float x, float y) {
        super(screen, x, y);
        //Set cloud image and animation
        cloud = new Texture("clouds.png");
        TextureRegion[][] tmp = new TextureRegion(cloud).split(2048, 1256);
        List<TextureRegion> list = new ArrayList<TextureRegion>(Arrays.asList(tmp[0]));
        list.addAll(Arrays.asList(tmp[1]));
        int startIndex = rand.nextInt(4); // This is to randomize the cloud animation
        setRegion(list.get(startIndex));
        state = (startIndex * 0.25f) + 0.1f;
        cloudAnimation = new Animation(0.25f,  list.toArray());
        //Set the position and size of the cloud
        int dimension = 0;
        dimension = rand.nextInt(301-200)+200;
        setBounds(0,0,dimension / PirateGame.PPM, dimension * (0.61328125f) / PirateGame.PPM);
        //Sets origin of the cloud
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);
        alpha = 0.7f;
    }

    /**
     * Defines an entity for the cloud
     */

    @Override
    protected void defineEntity() {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
    }

    /**
     * (Not Used)
     *
     * Defines contact
     */

    @Override
    public void entityContact() {}

    /**
     * Updates the position and the animation frame of the cloud
     * @param dt time elapsed from last game frame
     */

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
        if(state % 0.15f < 0.10f){
            this.b2body.applyForce(new Vector2(-0.03f,-0.03f), this.b2body.getWorldCenter(), false);
        }
        setRegion(getFrame(dt));
    }

    /**
     * Changes the alpha value (transparency) of the cloud if the player is getting closer
     */

    public void changeAlpha() {
        this.alpha = this.alpha*0.99f;
        if (this.alpha < 0.5f){
            this.alpha = 0.5f;
        }
    }

    /**
     * Slowly resets the alpha value of the cloud when the player is leaving the cloud behind
     */

    public void resetAlpha(){
        this.alpha = this.alpha*1.005f;
        if (this.alpha > 0.9f){
            this.alpha = 0.9f;
        }
    }

    /**
     * Draws the cloud onto the screen
     * @param batch The game's sprite batch
     */
    public void draw(Batch batch) {
        super.setAlpha(alpha);
        super.draw(batch);
    }

    /**
     * Getter for the Cloud's animation frame
     * @param dt time elapsed from last game frame
     */
    public TextureRegion getFrame(float dt){
        TextureRegion region;
        state += dt;
        region = (TextureRegion) cloudAnimation.getKeyFrame(state, true);
        return region;
    }
}
