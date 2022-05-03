package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Coin
 * Creates an object for each coin
 * Extends the entity class to define coin as an entity
 *
 * @author Joe Dickinson
 * @version 1.0
 */
public class Coin extends Entity {

    private final Sound coinPickup;
    private final Random rand = new Random();

    /**
     * Instantiates a new Coin.
     *
     * @param screen the screen it's going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     */
    public Coin(GameScreen screen, float x, float y) {

        super(screen, x, y);

        // Set the position and size of the coin
        setBounds(0,0,48 / PPM, 48 / PPM);

        // Set the texture
        setRegion(new Texture("coin.png"));

        // Sets origin of the coin
        setOrigin(24 / PPM,24 / PPM);
        coinPickup = Gdx.audio.newSound(Gdx.files.internal("coin-pickup.mp3"));

    }

    /**
     * Updates the coins state. If needed, deletes the coin if picked up.
     */
    public void update() {

        // If coin is set to destroy and isn't, destroy it
        if(isSetToDestroy() && !isDestroyed()) {
            world.destroyBody(getBody());
            setDestroyed(true);
        }
        // Update position of coin
        else if(!isDestroyed()) {
            setPosition(getBody().getPosition().x - getWidth() / 2f, getBody().getPosition().y - getHeight() / 2f);
        }

    }

    /**
     * Defines all the parts of the coins physical model. Sets it up for collisions
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
        setBody(world.createBody(bdef));

        // Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(24 / PPM);
        fdef.shape = shape;

        // Setting BIT identifier
        fdef.filter.categoryBits = COIN_BIT;
        // Determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | PLAYER_BIT | ENEMY_BIT;
        fdef.isSensor = true;

        getBody().createFixture(fdef).setUserData(this);

    }

    /**
     * Defines contact for coins. The only entity that is able to do so is the player ship
     */
    @Override
    public void onContact() {

        // Add gold to the player
        Hud.changeCoins((rand.nextInt(GameScreen.getDifficulty().getMaxGoldXCoin()) + 1) * GameScreen.getDifficulty().getGoldCoinMulti());

        // Set toDestroy
        setToDestroy(true);

        // Play pickup sound
        if (GameScreen.getGame().getPreferences().isEffectsEnabled()) {
            coinPickup.play(GameScreen.getGame().getPreferences().getEffectsVolume());
        }

    }

    /**
     * Draws the coin using batch
     *
     * @param batch The batch of the program
     */
    public void draw(Batch batch) {

        if(!isDestroyed()) {
            super.draw(batch);
        }

    }
	
	@Override
	public void dispose() {

		super.dispose();
		coinPickup.dispose();

	}
}
