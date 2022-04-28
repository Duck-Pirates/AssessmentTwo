package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.COLLEGESENSOR_BIT;
import static com.mygdx.pirategame.configs.Constants.PLAYER_BIT;
import static com.mygdx.pirategame.configs.Constants.PPM;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.college.CollegeFire;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;
import com.mygdx.pirategame.world.AvailableSpawn;

/**
 * College
 * Class to generate the enemy entity college
 * Instantiates colleges
 * Instantiates college fleets
 *
 *@author Ethan Alabaster, Edward Poulter
 *@version 1.0
 */

public class College extends SteerableEntity {
    public Random rand = new Random();
    public String currentCollege, currentCollegePath;
    private Array<CollegeFire> cannonBalls;
    private AvailableSpawn noSpawn;
    public ArrayList<EnemyShip> fleet = new ArrayList<>();
    private float timeLastAttacked;
    private float timer = 0;

    /**
     *
     * @param screen Visual data
     * @param college College name i.e. "Alcuin" used for fleet assignment
     * @param x College position on x-axis
     * @param y College position on y-axis
     * @param flag College flag sprite (image name)
     * @param ship College ship sprite (image name)
     * @param ship_no Number of college ships to produce
     * @param invalidSpawn Spawn data to check spawn validity when generating ships
     */
    public College(GameScreen screen, String college, float x, float y, int ship_no, AvailableSpawn invalidSpawn) {
        super(screen, x, y);
        noSpawn = invalidSpawn;
        currentCollege = college + "_flag.png";
        texture = new Texture(currentCollege);
        //Set the position and size of the college
        setBounds(0,0,64 / PPM, 110 / PPM);
        setRegion(texture);
        setOrigin(32 / PPM, 55 / PPM);
        damage = 10;
        cannonBalls = new Array<>();
        int ranX = 0;
        int ranY = 0;
        boolean spawnIsValid;

        //Generates college fleet
        for (int i = 0; i < ship_no; i++){
            spawnIsValid = false;
            while (!spawnIsValid){
                ranX = rand.nextInt(2000) - 1000;
                ranY = rand.nextInt(2000) - 1000;
                ranX = (int)Math.floor(x + (ranX / PPM));
                ranY = (int)Math.floor(y + (ranY / PPM));
                spawnIsValid = AvailableSpawn.add(ranX, ranY);
            }
            fleet.add(new EnemyShip(screen, ranX, ranY, college + "_ship.png", college));
        }
    }


    /**
     * Updates the state of each object with delta time
     * Checks for college destruction
     * Checks for cannon fire
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float dt) {
    	
    	timer += dt;
    	
        //If college is set to destroy and isnt, destroy it
        if(setToDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;

            //If it is the player ally college, end the game for the player
            if (currentCollege.equals("alcuin_flag.png")){
                screen.gameOverCheck();
            }
            //Award the player coins and points for destroying a college
            if (!currentCollege.equals("alcuin_flag.png")){
                Hud.changePoints(100);
                Hud.changeCoins(rand.nextInt(10 - 1) + 1);
            }
        }
        //If not destroyed, update the college position
        else if(!destroyed) {
            setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);

        }
        if(health <= 0) {
            setToDestroy = true;
        }
        bar.update();
        
        //Update cannon balls
        for(CollegeFire ball : cannonBalls) {
            ball.update(dt);
            if(ball.isDestroyed())
                cannonBalls.removeValue(ball, true);
        }
    }

    /**
     * Draws the batch of cannonballs
     */
    public void draw(Batch batch) {
        if(!destroyed) {
            super.draw(batch);
            //Render health bar
            bar.render(batch);

            //Render balls
            for(CollegeFire ball : cannonBalls)
                ball.draw(batch);
        }
    }

    /**
     * Sets the data to define a college as an enemy
     */
    @Override
    protected void defineEntity(float x, float y) {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(55 / PPM);
        // setting BIT identifier
        fdef.filter.categoryBits = COLLEGESENSOR_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = PLAYER_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Contact detection
     * Allows for the college to take damage
     */
    @Override
    public void onContact() {
        //Damage the college and lower health bar
        Gdx.app.log("enemy", "collision");

        // Damage to college by cannon ball?
        health -= GameScreen.difficulty.getDamageDealt();
        bar.changeHealth(GameScreen.difficulty.getDamageDealt());
    }
    
    /**
     * Fires cannonballs
     */
    public void fire() {
        cannonBalls.add(new CollegeFire(screen, body.getPosition().x, body.getPosition().y));
    }

    /**
     * Getter for the college's path
     */
    public String getCurrentCollegePath(){
        return currentCollegePath;
    }

    /**
     * Getter for the college name
     */
    public String getCurrentCollegeName(){
        return currentCollege;
    }
}

