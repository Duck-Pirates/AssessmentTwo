package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Enemy Ship
 * Generates enemy ship data
 * Instantiates an enemy ship
 *
 *@author Ethan Alabaster, Sam Pearson, Edward Poulter
 *@version 1.0
 */
public class EnemyShip extends Enemy {
	private SteeringAgent steeringAgent;
    private Texture enemyShip;
    public String college;
    private String objective = "WANDER";
    private boolean objectiveChanged = true;
    private Sound destroy;
    private Sound hit;
    
    /**
     * Instantiates enemy ship
     *
     * @param screen Visual data
     * @param x x coordinates of entity
     * @param y y coordinates of entity
     * @param path path of texture file
     * @param assignment College ship is assigned to
     */
    public EnemyShip(GameScreen screen, float x, float y, String path, String assignment) {
        super(screen, x, y);
        enemyShip = new Texture(path);
        //Assign college
        college = assignment;
        //Set audio
        destroy = Gdx.audio.newSound(Gdx.files.internal("ship-explosion-2.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("ship-hit.wav"));
        //Set the position and size of the college
        setBounds(0,0,64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(enemyShip);
        setOrigin(32 / PirateGame.PPM,55 / PirateGame.PPM);

        damage = screen.difficulty.getDamageDealt();
    }

    /**
     * Updates the state of each object with delta time
     * Checks for ship destruction
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float dt) {
        //If ship is set to destroy and isn't, destroy it
        if(setToDestroy && !destroyed) {
            //Play death noise
            if (screen.game.getPreferences().isEffectsEnabled()) {
                destroy.play(screen.game.getPreferences().getEffectsVolume());
            }
            world.destroyBody(steeringAgent.getBody());
            destroyed = true;
            //Change player coins and points
            Hud.changePoints(30);
            Hud.changeCoins(10);
        }
        else if(!destroyed) {
            
        	if(objectiveChanged) {
        		if(objective == "DEFENCE") {
        			
        		} else if(objective == "WANDER") {
        			Wander<Vector2> wanderSB = new Wander<Vector2>(steeringAgent)
        					.setFaceEnabled(true)
        					.setAlignTolerance(0.1f)
        					.setWanderRadius(10 * PirateGame.PPM)
        					.setWanderRate(0.1f)
        					.setWanderOffset(10 * PirateGame.PPM);
        			steeringAgent.setBehavior(wanderSB);
        		}
        		objectiveChanged = false;
        	}
        	
        	steeringAgent.update(dt);
        	Body body = steeringAgent.getBody();
        	setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
            float angle = (float) Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x);
            body.setTransform(body.getWorldCenter(), angle - ((float) Math.PI) / 2.0f);
            setRotation((float) (body.getAngle() * 180 / Math.PI));
            //Update health bar
            bar.update();

        }
        if(health <= 0) {
            setToDestroy = true;
        }

        // below code is to move the ship to a coordinate (target)
        //Vector2 target = new Vector2(960 / PirateGame.PPM, 2432 / PirateGame.PPM);
        //target.sub(body.getPosition());
        //target.nor();
        //float speed = 1.5f;
        //body.setLinearVelocity(target.scl(speed));
    }

    /**
     * Constructs the ship batch
     *
     * @param batch The batch of visual data of the ship
     */
    public void draw(Batch batch) {
        if(!destroyed) {
            super.draw(batch);
            //Render health bar
            bar.render(batch);
        }
    }

    /**
     * Defines the ship as an enemy
     * Sets data to act as an enemy
     */
    @Override
    protected void defineEnemy() {
    	
    	Body body;
    	
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(55 / PirateGame.PPM);
        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.ENEMY_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT | PirateGame.CANNON_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.7f;
        body.createFixture(fdef).setUserData(this);
        
        steeringAgent = new SteeringAgent(body, 55 / PirateGame.PPM);
    }

    /**
     * Checks contact
     * Changes health in accordance with contact and damage
     */
    @Override
    public void onContact() {
        Gdx.app.log("enemy", "collision");
        //Play collision sound
        if (screen.game.getPreferences().isEffectsEnabled()) {
            hit.play(screen.game.getPreferences().getEffectsVolume());
        }
        //Deal with the damage
        health -= screen.difficulty.getDamageDealt();
        bar.changeHealth(screen.difficulty.getDamageDealt());
        Hud.changePoints(5);
    }

    /**
     * Updates the ship image. Particularly change texture on college destruction
     *
     * @param alignment Associated college
     * @param path Path of new texture
     */
    public void updateTexture(String alignment, String path){
        college = alignment;
        enemyShip = new Texture(path);
        setRegion(enemyShip);
    }
    
    public String getObjective() {
    	return objective;
    }
    
    public void setObjective(String objective) {
    	this.objective = objective;
    }
    
    public Vector2 getPosition() {
    	return steeringAgent.getBody().getPosition();
    }
}
