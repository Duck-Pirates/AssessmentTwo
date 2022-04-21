package com.mygdx.pirategame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;


/**
 * This class implements methods and variables for every powerup in game, that will have their own class and methods too
 *
 * @author Davide Bressani
 * @author Benjamin Withnell
 * @version 2.0
 */

public class Powerup extends Entity{
    private Texture powerup;
    private boolean setToDestroyed;
    private boolean destroyed;
    private Sound powerupPickup;
    private Integer powerupType;


    /**
     * Instantiates a new Powerup.
     *
     * @param screen the screen its going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     * @param type   the powerup's type, that changes the texture and sound of it
     */

    //TODO powerups dissapear just after being picked up
    public Powerup(GameScreen screen, float x, float y, Integer type) {
        super(screen, x, y);

        //Set powerup image

        powerupType = type;
        if (powerupType == 0) {
            powerup = new Texture("Ammo.png");
        } else if (powerupType == 1){
            powerup = new Texture("Lightning.png");
        } else if (powerupType == 2){
            powerup = new Texture("Money.png");
        } else if (powerupType == 3){
            powerup = new Texture("Repair.png");
        } else if (powerupType == 4){
            powerup = new Texture("Star.png");
        }


        Gdx.app.log("x", String.valueOf(x));
        Gdx.app.log("y", String.valueOf(y));
        //Set the position and size of the powerup
        setBounds(0,0,100 / PirateGame.PPM, 100 / PirateGame.PPM);
        //Set the texture
        setRegion(powerup);
        //Sets origin of the powerup
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);
        powerupPickup = Gdx.audio.newSound(Gdx.files.internal("powerup-pickup.mp3"));
    }

    /**
     * Updates the powerup's state. If needed, deletes the powerup if picked up.
     */
    public void update() {
        //If powerup is set to destroy and isnt, destroy it
        if(setToDestroyed && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
        //Update position of powerup
        else if(!destroyed) {
            setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
        }
    }

    /**
     * Defines all the parts of the powerup physical model. Sets it up for collisions
     */
    @Override
    protected void defineEntity() {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(24 / PirateGame.PPM);
        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.POWERUP_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }
    @Override
    public void onContact() {

        setToDestroyed = true;

        if (screen.game.getPreferences().isEffectsEnabled()) {
            powerupPickup.play(screen.game.getPreferences().getEffectsVolume());
        }



        //Select case

        // Timer... Each powerup lasts 30 seconds

        //Ammo - Increase
        //Lightning - Increase Speed by 10% + Increase rotation 10%
        //Money - Increase earnings by 50%
        //Repair - Increase regain HP speed
        //Star - Take no damage
        if (Hud.PowerupTimerBool == Boolean.FALSE) {

            // Remove previous powerup
            // Reset previous powerup
            screen.difficulty.PreviousPowerupStats();

            if (powerupType == 0) {
                Hud.ChangePowerUpImage(0);
                Ammo();
            } else if (powerupType == 1) {
                Hud.ChangePowerUpImage(1);
                Lightning();
            } else if (powerupType == 2) {
                Hud.ChangePowerUpImage(2);
                Money();
            } else if (powerupType == 3) {
                Hud.ChangePowerUpImage(3);
                Repair();
            } else if (powerupType == 4) {
                Hud.ChangePowerUpImage(4);
                Star();
            }
        } else{
            Gdx.app.log("pu", "powerup already picked up");
        }
    }


    private void HideCurrentPower(){

    }



    public void Ammo(){
        // Increase damage or shots per second
        screen.difficulty.SavePowerupStats();
        screen.difficulty.SetDamageDealt(5); // increases damage dealt by 5... could be doubled??
    }
    public void Lightning(){
        // Increase Speed
        screen.difficulty.SavePowerupStats();
        screen.difficulty.SetMaxSpeed(1.5f);// doubles mad speed + imcreases speed reduction by 1%
    }
    public void Money(){
        // Increase money earnt
        screen.difficulty.SavePowerupStats();
        screen.difficulty.SetGoldCoinMulti(1); // +1 to current multi (from *1 to *2)
    }
    public void Repair(){
        // Recovers ship
        screen.difficulty.SavePowerupStats();
        //screen.difficulty.IncreaseHP();
        Hud.changeHealth(50); // increases HP by 50 aslong as its less than max,
        // TODO getting health back even if ur not getting damaged
        // TODO heart powerup
    }
    public void Star(){
        // TODO Imunity?? could change for cone
        screen.difficulty.SavePowerupStats();
        screen.difficulty.SetDamageReceived(0);
    }

    /**
     * Draws the powerup using batch
     *
     * @param batch The batch of the program
     */
    public void draw(Batch batch) {
        if(!destroyed) {
            super.draw(batch);
        }
    }
}