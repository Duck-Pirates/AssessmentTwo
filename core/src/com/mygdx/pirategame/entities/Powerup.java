package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;


/**
 * This class implements methods and variables for every powerup in game, that will have their own class and methods too
 *
 * @author Davide Bressani, Benjamin Withnell
 * @version 2.0
 */
public class Powerup extends Entity {
    private boolean setToDestroyed;
    private boolean destroyed;
    private final Sound powerupPickup;
    private Integer powerupType;


    /**
     * Instantiates a new Powerup.
     *
     * @param screen the screen it's going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     * @param type   the powerup's type, that changes the texture and sound of it
     */
    public Powerup(GameScreen screen, float x, float y, Integer type) {

        super(screen, x, y);

        //Set powerup image
        setPowerupType(type);
        if (getPowerupType() == 0) {
            texture = new Texture("Ammo.png");
        } else if (getPowerupType() == 1){
        	texture = new Texture("Lightning.png");
        } else if (getPowerupType() == 2){
        	texture = new Texture("Money.png");
        } else if (getPowerupType() == 3){
        	texture = new Texture("Repair.png");
        } else if (getPowerupType() == 4){
        	texture = new Texture("Star.png");
        }

        //Set the position, size, origin and texture of the powerup
        setBounds(0,0,100 / PPM, 100 / PPM);
        setRegion(texture);
        setOrigin(24 / PPM,24 / PPM);

        powerupPickup = Gdx.audio.newSound(Gdx.files.internal("powerup-pickup.mp3"));

    }

    /**
     * Updates the powerup's state. If needed, deletes the powerup if picked up.
     */
    public void update() {

        //If powerup is set to destroy and isn't, destroy it
        if(isSetToDestroyed() && !isDestroyed()) {
            world.destroyBody(getBody());
            setDestroyed(true);
        }
        //Update position of powerup
        else if(!isDestroyed()) {
            setPosition(getBody().getPosition().x - getWidth() / 2f, getBody().getPosition().y - getHeight() / 2f);
        }

    }

    /**
     * Defines the entity
     *
     * @param x value of origin
     * @param y value of origin
     */
    @Override
    protected void defineEntity(float x, float y) {

        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        setBody(world.createBody(bdef));

        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(24 / PPM);
        // setting BIT identifier
        fdef.filter.categoryBits = POWERUP_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = DEFAULT_BIT | PLAYER_BIT | ENEMY_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;

        getBody().createFixture(fdef).setUserData(this);

    }

    /**
     * Defines contact with every type of Powerup
     */
    @Override
    public void onContact() {

        setSetToDestroyed(true);

        if (GameScreen.getGame().getPreferences().isEffectsEnabled()) {
            powerupPickup.play(GameScreen.getGame().getPreferences().getEffectsVolume());
        }

        /*

        Select case

        Timer... Each powerup lasts 30 seconds

        Ammo - Increase
        Lightning - Increase Speed by 10% + Increase rotation 10%
        Money - Increase earnings by 50%
        Repair - Increase regain HP speed
        Star - Take no damage

         */
        if (!Hud.PowerupTimerBool) {

            // Remove previous powerup
            // Reset previous powerup
            GameScreen.getDifficulty().previousPowerupStats();

            if (getPowerupType() == 0) {
                Hud.ChangePowerUpImage(0);
                Ammo();
            } else if (getPowerupType() == 1) {
                Hud.ChangePowerUpImage(1);
                Lightning();
            } else if (getPowerupType() == 2) {
                Hud.ChangePowerUpImage(2);
                Money();
            } else if (getPowerupType() == 3) {
                Hud.ChangePowerUpImage(3);
                Repair();
            } else if (getPowerupType() == 4) {
                Hud.ChangePowerUpImage(4);
                Star();
            }

        }

    }

    /**
     * Method called if the Ammo Powerup is picked up
     */
    public void Ammo(){

        // Increase damage or shots per second
        GameScreen.getDifficulty().savePowerupStats();
        GameScreen.getDifficulty().setDamageDealt(5);

    }

    /**
     * Method called if the Lightning Powerup is picked up
     */
    public void Lightning(){

        // Increase Speed
        GameScreen.getDifficulty().savePowerupStats();
        GameScreen.getDifficulty().setMaxSpeed(1.5f);// Doubles maxSpeed and increases the speed reduction by 1%

    }

    /**
     * Method called if the Money Powerup is picked up
     */
    public void Money(){

        // Increase money earned
        GameScreen.getDifficulty().savePowerupStats();
        GameScreen.getDifficulty().setGoldCoinMulti(1); // Adds 1 to the current Gold Coin Multiplier

    }

    /**
     * Method called if the Repair Powerup is picked up
     */
    public void Repair(){

        GameScreen.getDifficulty().savePowerupStats();
        Hud.changeHealth(50); // Increases HP by 50 as long as it's less than upper limit

    }

    /**
     * Method called if the Immunity Powerup is picked up
     */
    public void Star(){

        GameScreen.getDifficulty().savePowerupStats();
        GameScreen.getDifficulty().setDamageReceived(0);

    }

    /**
     * Draws the powerup using batch
     *
     * @param batch The batch of the program
     */
    public void draw(Batch batch) {
        if(!isDestroyed()) {
            super.draw(batch);
        }
    }

    /**
     * Dispose the entity and the powerup's texture
     */
    @Override
    public void dispose() {
        super.dispose();
        powerupPickup.dispose();
    }

	public Integer getPowerupType() {
		return powerupType;
	}

	public boolean isSetToDestroyed() {
		return setToDestroyed;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

    public void setSetToDestroyed(boolean setToDestroyed) {
        this.setToDestroyed = setToDestroyed;
    }

    public void setPowerupType(Integer powerupType) {
        this.powerupType = powerupType;
    }

}