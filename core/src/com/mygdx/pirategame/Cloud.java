package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;


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

    public Cloud(GameScreen screen, float x, float y) {
        super(screen, x, y);
        //Set cloud image
        cloud = new Texture("clouds.png");
        //Set the position and size of the cloud
        setBounds(0,0,200 / PirateGame.PPM, 150 / PirateGame.PPM);
        //Set the texture
        setRegion(cloud);
        //Sets origin of the cloud
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);
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



    public void draw(Batch batch) {
        super.setAlpha(0.7f);
        super.draw(batch);
    }
}
