package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.COIN_BIT;
import static com.mygdx.pirategame.configs.Constants.DEFAULT_BIT;
import static com.mygdx.pirategame.configs.Constants.ENEMY_BIT;
import static com.mygdx.pirategame.configs.Constants.NOSPAWNAREA_BIT;
import static com.mygdx.pirategame.configs.Constants.PLAYER_BIT;
import static com.mygdx.pirategame.configs.Constants.PPM;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

/**
 * Coin
 * Creates an object for each coin
 * Extends the entity class to define coin as an entity
 *
 *@author Joe Dickinson
 *@version 1.0
 */
public class Coin extends Entity {
    private Texture coin;
    public boolean setToDestroyed;
    public boolean destroyed;
    private Sound coinPickup;
    public Random rand = new Random();

    /**
     * Instantiates a new Coin.
     *
     * @param screen the screen its going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     */
    public Coin(GameScreen screen, float x, float y) {
        super(screen, x, y);
        //Set coin image
        coin = new Texture("coin.png");
        //Set the position and size of the coin
        setBounds(0,0,48 / PPM, 48 / PPM);
        //Set the texture
        setRegion(coin);
        //Sets origin of the coin
        setOrigin(24 / PPM,24 / PPM);
        coinPickup = Gdx.audio.newSound(Gdx.files.internal("coin-pickup.mp3"));
    }

    /**
     * Updates the coins state. If needed, deletes the coin if picked up.
     */
    public void update() {
        //If coin is set to destroy and isnt, destroy it
        if(setToDestroyed && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
        //Update position of coin
        else if(!destroyed) {
            setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
        }
    }

    /**
     * Defines all the parts of the coins physical model. Sets it up for collisons
     */
    @Override
    protected void defineEntity(float x, float y) {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(24 / PPM);
        // setting BIT identifier
        fdef.filter.categoryBits = COIN_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | PLAYER_BIT | ENEMY_BIT | NOSPAWNAREA_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * What happens when an entity collides with the coin. The only entity that is able to do so is the player ship
     */
    @Override
    public void onContact() {
        //Add a coin
        Hud.changeCoins((rand.nextInt(GameScreen.difficulty.getMaxGoldXCoin()) + 1) * GameScreen.difficulty.getGoldCoinMulti());
        //Set to destroy
        setToDestroyed = true;
        //Play pickup sound
        if (GameScreen.game.getPreferences().isEffectsEnabled()) {
            coinPickup.play(GameScreen.game.getPreferences().getEffectsVolume());
        }

    }

    /**
     * Draws the coin using batch
     *
     * @param batch The batch of the program
     */
    public void draw(Batch batch) {
        if(!destroyed) {
            super.draw(batch);
        }
    }
}
