package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;

import java.util.Random;


/**
 * Coin
 * Creates an object for each coin
 * Extends the entity class to define coin as an entity
 *
 * @author Davide Bressani
 * @version 1.0
 */
public class Cloud extends Entity{

    private Texture cloud;
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
        //Set cloud image
        cloud = new Texture("clouds.png");
        //Set the position and size of the cloud
        int dimension = 0;
        dimension = rand.nextInt(301-200)+200;
        setBounds(0,0,dimension / PirateGame.PPM, dimension * (3/4f) / PirateGame.PPM);
        //Set the texture
        setRegion(cloud);
        //Sets origin of the cloud
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);
        alpha = 0.7f;
    }

    /**
     * Defines an entity
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
     * Defines contact
     */
    @Override
    public void entityContact() {}

    public void update(){
        setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
    }

    public void changeAlpha() {
        this.alpha = this.alpha*0.99f;
        if (this.alpha < 0.5f){
            this.alpha = 0.5f;
        }
    }

    public void resetAlpha(){
        this.alpha = this.alpha*1.005f;
        if (this.alpha > 0.9f){
            this.alpha = 0.9f;
        }
    }

    public void draw(Batch batch) {
        super.setAlpha(alpha);
        super.draw(batch);
    }
}
