package com.mygdx.pirategame;

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
    public Powerup(GameScreen screen, float x, float y, Integer type) {
        super(screen, x, y);

        //TODO We need to add some texture and sound for the powerups

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
            world.destroyBody(b2body);
            destroyed = true;
        }
        //Update position of powerup
        else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
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
        b2body = world.createBody(bdef);

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
        b2body.createFixture(fdef).setUserData(this);
    }
    @Override
    public void entityContact() {
        //Add powerup ability

        //Set to destroy
        setToDestroyed = true;
        Gdx.app.log("powerup", "collision");
        //Play pickup sound
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


        // Remove previous powerup
        // Reset previous powerup
        screen.difficulty.PreviousPowerupStats();

        if (powerupType == 0) {
            Hud.ChangePowerUpImage(0);
            Ammo();
        } else if (powerupType == 1){
            Hud.ChangePowerUpImage(1);
            Lightning();
        } else if (powerupType == 2){
            Hud.ChangePowerUpImage(2);
            Money();
        } else if (powerupType == 3){
            Hud.ChangePowerUpImage(3);
            Repair();
        } else if (powerupType == 4){
            Hud.ChangePowerUpImage(4);
            Star();
        }

    }


    private void HideCurrentPower(){

    }



    public void Ammo(){
        // Increase damage or shots per second


        screen.difficulty.SetDamageDealt(2);
        screen.difficulty.SavePowerupStats();
    }
    public void Lightning(){
        // Increase Speed

        screen.difficulty.SetMaxSpeed(6f);
        screen.difficulty.SavePowerupStats();
    }
    public void Money(){
        // Increase money earnt

        screen.difficulty.SetGoldCoinMulti(2); // double current multi
        screen.difficulty.SavePowerupStats();
    }
    public void Repair(){
        // Recovers ship
        screen.difficulty.IncreaseHP();
    }
    public void Star(){
        // TODO Imunity?? could change for cone
        screen.difficulty.SetDamageReceived(0);
        screen.difficulty.SavePowerupStats();
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