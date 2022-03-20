package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Tornado
 * Creates an object for each tornado
 * Extends the entity class to define coin as an entity
 *
 * @author Davide Bressani
 * @version 1.0
 */
public class Tornado extends Entity {

    float state = 0;
    private Animation swirl;
    private Texture tornado;
    private Sound windSound;
    public SpriteBatch batch;
    private Random rand = new Random();

    /**
     * Instantiates a new Tornado.
     *
     * @param screen the screen its going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     */
    public Tornado(GameScreen screen, float x, float y) {
        super(screen, x, y);
        // Set posistion and size of the tornado
        int dimension = rand.nextInt(257-128)+128;
        // The float number used for the height is to keep the ration of the texture image intact
        setBounds(0,0, dimension / PirateGame.PPM, dimension * 1.473333333f / PirateGame.PPM);
        //Sets origin of the tornado
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);
        tornado =new Texture(Gdx.files.internal("TornadoSwirls.png"));
        TextureRegion[][] tmp = new TextureRegion(tornado).split(150, 221);
        setRegion(tmp[0][0]);
        swirl = new Animation(0.25f, tmp[0]);
    }

    /**
     * Defines an entity
     */
    @Override
    protected void defineEntity () {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
    }

    /**
     * Defines contact
     */
    @Override
    public void entityContact () {

    }

    /**
     * Draw the tornados on the gamescreen
     */

    public void draw () {
        super.draw(batch);
    }

    /**
     * Updates the tornado's position and animation
     * @param dt time elapsed
     */

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
        setRegion(getFrame(dt));
    }

    /**
     * Creates a vector force applied on the player if it's close to a tornado. This method also makes the boat turn
     * @param player The player's instance
     */

    public void tornadoImpulse(Player player, float dt){
        Vector2 pos = player.b2body.getWorldCenter();
        Vector2 moverPos = b2body.getWorldCenter();
        Vector2 fr = pos.sub(moverPos);
        float d = fr.len();
        float strength = 50f / (d*d);
        if(strength > 10){
            strength = Math.min(5*strength, 125);
            player.updateRotation(-strength/12, dt);
        }
        fr = fr.nor();
        fr = fr.scl(-strength);
        player.b2body.applyForce(fr, player.b2body.getPosition(), true);
    }

    /**
     * Checks which frame the animation should be in and returns it
     * @param dt time elapsed
     * @return The current animation frame
     */

    public TextureRegion getFrame(float dt){
        TextureRegion region;
        state += dt;
        region = (TextureRegion) swirl.getKeyFrame(state, true);
        return region;
    }

    /**
     * Checks if the player is close to the tornado
     * @param player
     */
    public boolean checkPosition(Player player){
        if ((player.getX() >= (this.getX() - 1) && player.getX() <= (this.getX() + 1)) && (player.getY() >= (this.getY() - 1) && player.getY() <= (this.getY() + 1))){
            return true;
        }
        return false;
    }
}
