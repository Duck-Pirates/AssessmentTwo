package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.*;
import com.mygdx.pirategame.fsm.EntityProximity;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;
import com.mygdx.pirategame.world.AvailableSpawn;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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

	private final Random rand = new Random();
    private ArrayList<EnemyShip> fleet = new ArrayList<>();

    /**
     * Instantiates the college object
     *
     * @param screen Visual data
     * @param college College name i.e. "Alcuin" used for fleet assignment
     * @param x College position on x-axis
     * @param y College position on y-axis
     * @param ship_no Number of college ships to produce
     */
    public College(GameScreen screen, String college, float x, float y, int ship_no) {
        super(screen, x, y);

        this.college = college;
        String flag = college + "_flag.png";

        texture = new Texture(flag);

        //Set the position and size of the college
        setBounds(0,0,64 / PPM, 110 / PPM);
        setRegion(texture);
        setOrigin(32 / PPM, 55 / PPM);

        damage = 10;

        // Generates college fleet
        int ranX = 0;
        int ranY = 0;
        boolean spawnIsValid;
        for (int i = 0; i < ship_no; i++){
            spawnIsValid = false;
            while (!spawnIsValid){
                ranX = rand.nextInt(2000) - 1000;
                ranY = rand.nextInt(2000) - 1000;
                ranX = (int) MathUtils.floor(x + (ranX / PPM));
                ranY = (int) MathUtils.floor(y + (ranY / PPM));
                spawnIsValid = AvailableSpawn.add(ranX, ranY);
            }
            getFleet().add(new EnemyShip(screen, ranX, ranY, college));
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

        // If college is set to destroy and isn't, destroy it
        if(isSetToDestroy()) {
            world.destroyBody(getBody());
            setDestroyed(true);

            // If it is the player allay college, end the game for the player
            if (college.equals("alcuin")){
                screen.gameOverCheck();
            }
            else {
                Hud.changePoints(100);
                Hud.changeCoins(rand.nextInt(10 - 1) + 1);
            }
        }
        else {
            setPosition(getBody().getPosition().x - getWidth() / 2f, getBody().getPosition().y - getHeight() / 2f);
        }

        if(getHealth() <= 0) {
            setSetToDestroy(true);
        }
        bar.update();
        
        // Update cannon balls
        for(CannonFire ball : cannonBalls) {
            ball.update(dt);
            if(ball.isDestroyed()) {
                cannonBalls.removeValue(ball, true);
                ball.dispose();
            }
        }

    }

    /**
     * Draws the batch of cannonballs
     */
    @Override
    public void draw(Batch batch) {

        if(!isDestroyed()) {
            super.draw(batch);
            //Render health bar
            bar.render(batch);
        }

    }

    /**
     * Sets the data to define a college as an enemy
     */
    @Override
    protected void defineEntity(float x, float y) {

        // Sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.StaticBody;
        setBody(world.createBody(bdef));
        
        // Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(55 / PPM);
        fdef.shape = shape;
        
        // Setting BIT identifier
        fdef.filter.categoryBits = COLLEGESENSOR_BIT;
        fdef.filter.maskBits = DEFAULT_BIT;
        fdef.isSensor = true;

        getBody().createFixture(fdef).setUserData(this);

    }

    /**
     * Contact detection
     * Allows for the college to take damage
     */
    @Override
    public void onContact() {

        // Damage to college by cannon ball
        setHealth(getHealth() - GameScreen.getDifficulty().getDamageDealt());
        bar.changeHealth(GameScreen.getDifficulty().getDamageDealt());

    }
    
    /**
     * Fires cannonballs
     */
    @Override
    public void fire() {

    	ArrayList<Entity> ships = EntityProximity.findAgents(this, GameScreen.getShips().subList(0, 1), 6000 / PPM);
    	if(ships != null && GdxAI.getTimepiece().getTime() - getTimeFired() > 0.5f) {
    		Vector2 A = getBody().getPosition();
    		Vector2 B = ships.get(0).getPosition();
    		float angle = B.sub(A).angleRad();
    		cannonBalls.add(new CannonFire(this, screen, getBody(), getBody().getPosition().x, getBody().getPosition().y, angle));
    		setTimeFired(GdxAI.getTimepiece().getTime());
    	}

    }

	public ArrayList<EnemyShip> getFleet() {
		return fleet;
	}


	public void setFleet(ArrayList<EnemyShip> fleet) {
		this.fleet = fleet;
	}
}

