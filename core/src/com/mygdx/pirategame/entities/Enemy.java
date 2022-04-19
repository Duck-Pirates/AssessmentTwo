package com.mygdx.pirategame.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.screens.GameScreen;

/**
 * Enemy
 * Class to generate enemies
 * Instantiates enemies
 *
 *@author Ethan Alabaster
 *@version 1.0
 */
public abstract class Enemy extends Entity {
    public boolean setToDestroy;
    public boolean destroyed;
    public int health;
    public int damage;
    protected HealthBar bar;


    /**
     * Instantiates an enemy
     *
     * @param screen Visual data
     * @param x x position of entity
     * @param y y position of entity
     */
    public Enemy(GameScreen screen, float x, float y) {
    	super(screen, x, y);
        this.setToDestroy = false;
        this.destroyed = false;
        this.health = 100;
        bar = new HealthBar(this);
    }
    
    public abstract void update(float delta);

    /**
     * Checks recieved damage
     * Increments total damage by damage received
     * @param value Damage received
     */
    public void changeDamageReceived(int value){
        damage += value;
    }
    
    public abstract Vector2 getPosition();
}
