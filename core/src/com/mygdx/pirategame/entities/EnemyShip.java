package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.PirateGame.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.fsm.EnemyStateMachine;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;

/**
 * Enemy Ship
 * Generates enemy ship data
 * Instantiates an enemy ship
 *
 *@author Ethan Alabaster, Sam Pearson, Edward Poulter
 *@version 1.0
 */
public class EnemyShip extends SteerableEntity {
    private Texture enemyShip;
    public String college;
    private Sound destroy;
    private Sound hit;
    
    public StateMachine<EnemyShip, EnemyStateMachine> stateMachine;
    
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
        college = assignment;
        
        stateMachine = new DefaultStateMachine<EnemyShip, EnemyStateMachine>(this, EnemyStateMachine.SLEEP);
        
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
    public void update(float delta) {
        //If ship is set to destroy and isn't, destroy it
        if(setToDestroy && !destroyed) {
            //Play death noise
            if (screen.game.getPreferences().isEffectsEnabled()) {
                destroy.play(screen.game.getPreferences().getEffectsVolume());
            }
            world.destroyBody(body);
            destroyed = true;
            //Change player coins and points
            Hud.changePoints(30);
            Hud.changeCoins(10);
            
            enemyShip.dispose();
            destroy.dispose();
            hit.dispose();
        } else if(!destroyed) {
            
        	stateMachine.update();
        	
        	GdxAI.getTimepiece().update(delta);
        	if (behavior != null) {
    			behavior.calculateSteering(steerOutput);
    			applySteering(steerOutput, delta);
    		}
        	
        	setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
            setRotation((float) (Math.toDegrees(body.getAngle())));
            
            bar.update();
        }
        if(health <= 0) {
            setToDestroy = true;
        }
    }
    
    private void applySteering(SteeringAcceleration<Vector2> steering, float delta) {
		
		body.setAngularVelocity(steering.angular * delta);
		body.setLinearVelocity(steering.linear.scl(delta));
	}
    
    public void fire() {}

    /**
     * Defines the ship as an enemy
     * Sets data to act as an enemy
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
        fdef.density = 1;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20 / PPM, 50 / PPM);
        fdef.shape = shape;
        shape.dispose();
        
        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.ENEMY_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT 
        		| PirateGame.CANNON_BIT;
        
        body.createFixture(fdef).setUserData(this);
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
}
