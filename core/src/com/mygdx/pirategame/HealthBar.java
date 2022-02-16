package com.mygdx.pirategame;


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
class HealthBar {
    private Sprite healthBar;
    private Texture image;

    private Enemy owner;

    /**
     * Instantiates health bar
     * Sets health bar
     *
     * @param owner Parent entity of health bar
     */
    public HealthBar(Enemy owner){
        this.owner = owner;
        image = new Texture("HealthBar.png");
        healthBar = new Sprite(image);
        //Sets size of the health bar
        healthBar.setScale(0.0155f);
        healthBar.setSize(healthBar.getWidth(), healthBar.getHeight() - 2f);

        //Sets location of bar
        healthBar.setX (this.owner.b2body.getPosition().x - 0.68f);
        healthBar.setY(this.owner.b2body.getPosition().y + this.owner.getHeight() / 2);
        healthBar.setOrigin(0,0);
    }

    /**
     * Updates health bar
     */
    public void update(){
        if (owner != null) {
            //Update location
            healthBar.setX( (owner.b2body.getPosition().x - 0.68f));
            healthBar.setY(owner.b2body.getPosition().y + owner.getHeight() / 2);
        }
    }

    /**
     * Renders health bar
     */
    public void render(Batch batch){
        healthBar.draw(batch);
    }

    /**
     * Updates healthbar with regards to damage
     *
     * @param value Damage recieved
     */
    public void changeHealth(float value){
        //Changes bar size when damaged
        healthBar.setSize(healthBar.getWidth() - value, healthBar.getHeight());
    }
}