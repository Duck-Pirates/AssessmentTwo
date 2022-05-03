package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.DEFAULT_BIT;
import static com.mygdx.pirategame.configs.Constants.ENEMY_BIT;
import static com.mygdx.pirategame.configs.Constants.PLAYER_BIT;
import static com.mygdx.pirategame.configs.Constants.PPM;
import static com.mygdx.pirategame.configs.Constants.TORNADO_BIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private float state = 0;
    private Animation<Object> swirl;
	private SpriteBatch batch;
    private boolean inContact;
    private float damage = 1, timeElapsed = 0f;

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
        texture = new Texture(Gdx.files.internal("TornadoSwirls.png"));
        TextureRegion[][] tmp = new TextureRegion(texture).split(150, 221);
        setRegion(tmp[0][0]);
        swirl = new Animation<Object>(0.25f, tmp[0]);
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
        setBody(world.createBody(bdef));

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
        getBody().createFixture(fdef).setUserData(this);
    }

    /**
     * Defines contact
     */
    @Override
    public void onContact() {
        setInContact(true);
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
        setPosition(getBody().getPosition().x - getWidth() / 2f, getBody().getPosition().y - getHeight() / 2f);
        setRegion(getFrame(dt));
        tornadoDamage(dt);
    }

    /**
     * Creates a vector force applied on the player if it's close to a tornado. This method also makes the boat turn
     * @param player The player's instance
     */
    public void tornadoImpulse(Player player, float dt){
        // Checks if the player is within a certain distance of the tornado
        if(player.getPosition().dst2(getBody().getPosition()) <= (160 * 160) / (PPM * PPM)) {
        	player.body.applyTorque((float) Math.PI / 4, true);
        }
    }

    public void tornadoDamage(float dt){
        if(isInContact()){
            setTimeElapsed(getTimeElapsed() + dt);
            if(getTimeElapsed() > 1){
                setTimeElapsed(getTimeElapsed() - 1);
                setDamage(getDamage() * 1.1f);
                Hud.changeHealth((int) - Math.floor(getDamage()));
            }
        }
    }

    public void reset(){
        setDamage(1);
        setInContact(false);
        setTimeElapsed(0);
    }
    /**
     * Checks which frame the animation should be in and returns it
     * @param dt time elapsed
     * @return The current animation frame
     */

    public TextureRegion getFrame(float dt){
        TextureRegion region;
        setState(getState() + dt);
        region = (TextureRegion) swirl.getKeyFrame(getState(), true);
        return region;
    }

	public boolean isInContact() {
		return inContact;
	}

	public void setInContact(boolean inContact) {
		this.inContact = inContact;
	}

	public float getState() {
		return state;
	}

	public void setState(float state) {
		this.state = state;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(float timeElapsed) {
		this.timeElapsed = timeElapsed;
	}
}
