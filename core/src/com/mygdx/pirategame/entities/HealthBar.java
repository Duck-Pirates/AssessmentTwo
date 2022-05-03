package com.mygdx.pirategame.entities;

import static com.mygdx.pirategame.configs.Constants.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Health Bar
 * Displays the health of players
 * Displays the health of colleges
 * Displays the health of enemy ships
 * Creates and displays a health bar for entities with health
 *
 *@author Sam Pearson
 *@version 1.0
 */
public class HealthBar {

    private final Sprite healthBar;
    private final Texture image;

    private final SteerableEntity owner;

    /**
     * Instantiates health bar
     * Sets health bar
     *
     * @param owner Parent entity of health bar
     */
    public HealthBar(SteerableEntity owner){

        this.owner = owner;
        image = new Texture("HealthBar.png");
        healthBar = new Sprite(image);

        //Sets size of the health bar
        healthBar.setScale(0.0155f);
        healthBar.setSize(healthBar.getWidth(), healthBar.getHeight() - 2f);

        //Sets location of bar
        healthBar.setX (this.owner.getPosition().x - 0.68f);
        healthBar.setY(this.owner.getPosition().y + this.owner.getHeight() / 2);
        healthBar.setOrigin(0,0);

    }

    /**
     * Updates health bar
     */
    public void update(){

        if (owner != null) {
            //Update location
            healthBar.setX(owner.getBody().getPosition().x - 0.68f);
            healthBar.setY(owner.getBody().getPosition().y + 60 / PPM);
        }

    }

    /**
     * Renders health bar
     */
    public void render(Batch batch){
        healthBar.draw(batch);
    }

    /**
     * Updates healthbar size with regard to damage
     *
     * @param damage Damage received
     */
    public void changeHealth(float damage){

        //Changes bar size when damaged
        healthBar.setSize(healthBar.getWidth() - damage, healthBar.getHeight());

    }

    /**
     * Disposes the healthbar's texture
     */
    public void dispose() { image.dispose(); }
}