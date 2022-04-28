package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.DEFAULT_BIT;
import static com.mygdx.pirategame.configs.Constants.ENEMY_BIT;
import static com.mygdx.pirategame.configs.Constants.PLAYER_BIT;
import static com.mygdx.pirategame.configs.Constants.PPM;
import static com.mygdx.pirategame.configs.Constants.TORNADO_BIT;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

/**
 * Tornado
 * Creates an object for each tornado
 * Extends the entity class to define coin as an entity
 *
 * @author Davide Bressani
 * @version 1.0
 */
public class Tornado extends Entity {

    public float state = 0;
    private Animation swirl;
    public int dimension;

    private Texture tornado;
    private Sound windSound;
    public SpriteBatch batch;
    private Random rand = new Random();

    public boolean inContact;
    public float damage = 1;
    public float timeElapsed = 0f;

    /**
     * Instantiates a new Tornado.
     *
     * @param screen the screen its going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     */
    public Tornado(GameScreen screen, float x, float y, int dimension) {
        super(screen, x, y);
        // Set posistion and size of the tornado
        // The int number used for the height is to keep the ration of the texture image intact
        setBounds(0,0, dimension / PPM, dimension * 1.473333333f / PPM);
        //Sets origin of the tornado
        setOrigin(24 / PPM,24 / PPM);
        tornado =new Texture(Gdx.files.internal("TornadoSwirls.png"));
        TextureRegion[][] tmp = new TextureRegion(tornado).split(150, 221);
        setRegion(tmp[0][0]);
        swirl = new Animation(0.25f, tmp[0]);
    }

    /**
     * Defines an entity
     */
    @Override
    protected void defineEntity(float x, float y) {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        // Defines a player's shape and contact borders
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(150 / PPM);

        // setting BIT identifier
        fdef.filter.categoryBits = TORNADO_BIT;

        // determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | PLAYER_BIT | ENEMY_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Defines contact
     */
    @Override
    public void onContact() {
        inContact = true;
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
        setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
        setRegion(getFrame(dt));
        tornadoDamage(dt);
    }

    /**
     * Creates a vector force applied on the player if it's close to a tornado. This method also makes the boat turn
     * @param player The player's instance
     */

    public void tornadoImpulse(Player player, float dt){
        Vector2 pos = player.body.getWorldCenter();
        Vector2 moverPos = body.getWorldCenter();
        Vector2 fr = pos.sub(moverPos);
        float d = fr.len();
        float strength = 50f / (d*d);
        if(strength > 10){
            strength = Math.min(5*strength, 30);
            player.updateRotation(-strength/12, dt);
        }
        fr = fr.nor();
        fr = fr.scl(-strength);
        player.body.applyForce(fr, player.body.getPosition(), true);
    }

    public void tornadoDamage(float dt){
        if(inContact){
            timeElapsed += dt;
            if(timeElapsed > 1){
                timeElapsed -= 1;
                damage*=1.1f;
                Hud.changeHealth((int) - Math.floor(damage));
            }
        }
    }

    public void reset(){
        damage = 1;
        inContact = false;
        timeElapsed = 0;
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
}
