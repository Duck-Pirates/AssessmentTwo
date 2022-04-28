package com.mygdx.pirategame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.pirategame.college.CollegeFire;
import com.mygdx.pirategame.entities.*;
import com.mygdx.pirategame.screens.*;

import static com.mygdx.pirategame.configs.Constants.*;

/**
 * Tells the game what to do when certain entities come into contact with each other
 *
 * @author Ethan Alabaster
 * @version 1.0
 */
public class WorldContactListener implements ContactListener {

    public static GameScreen screen;

    public WorldContactListener(GameScreen screen){
        WorldContactListener.screen = screen;
    }

    /**
     * The start of the collision. Tells the game what should happen when the contact begins
     * @param contact The object that contains information about the collision
     */
    @Override
    public void beginContact(Contact contact) {
        // Finds contact
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        //Hud.changeHealth(-5);

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        // Fixes contact to an entity
        switch (cDef){
            case COIN_BIT | PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == COIN_BIT) {
                    ((Entity) fixA.getUserData()).onContact();
                }
                else {
                    ((Entity) fixB.getUserData()).onContact();
                }
                break;
            case POWERUP_BIT | PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == POWERUP_BIT) {
                    ((Entity) fixA.getUserData()).onContact();
                }
                else {
                    ((Entity) fixB.getUserData()).onContact();
                }
                break;
            case DEFAULT_BIT | PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == DEFAULT_BIT) {
                    if (fixA.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(fixA.getUserData().getClass())) {
                        ((InteractiveTileObject) fixA.getUserData()).onContact();
                        ((Player) fixB.getUserData()).playBreakSound();
                    }
                }
                else {
                    if (fixB.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(fixB.getUserData().getClass())) {
                        ((InteractiveTileObject) fixB.getUserData()).onContact();
                    }
                }
                break;
            case ENEMY_BIT | PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == ENEMY_BIT) {
                    ((SteerableEntity) fixA.getUserData()).onContact();
                }
                else {
                    ((SteerableEntity) fixB.getUserData()).onContact();
                }
                break;
            case COLLEGE_BIT | CANNON_BIT:
                if(fixA.getFilterData().categoryBits == COLLEGE_BIT) {
                    if (fixA.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(fixA.getUserData().getClass())) {
                        ((InteractiveTileObject) fixA.getUserData()).onContact();
                        ((CannonFire) fixB.getUserData()).setToDestroy();
                    }
                }
                else {
                    if (fixB.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(fixB.getUserData().getClass())) {
                        ((InteractiveTileObject) fixB.getUserData()).onContact();
                        ((CannonFire) fixA.getUserData()).setToDestroy();
                    }
                }
                break;
            case ENEMY_BIT | CANNON_BIT:
                if(fixA.getFilterData().categoryBits == ENEMY_BIT) {
                    ((SteerableEntity) fixA.getUserData()).onContact();
                    ((CannonFire) fixB.getUserData()).setToDestroy();
                }
                else {
                    ((SteerableEntity) fixB.getUserData()).onContact();
                    ((CannonFire) fixA.getUserData()).setToDestroy();
                }
                break;
            case COLLEGEFIRE_BIT | PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == COLLEGEFIRE_BIT) {
                    Hud.changeHealth(-GameScreen.difficulty.getDamageReceived());
                    ((CollegeFire) fixA.getUserData()).setToDestroy();
                }
                else {
                    Hud.changeHealth(-GameScreen.difficulty.getDamageReceived());
                    ((CollegeFire) fixB.getUserData()).setToDestroy();
                }
                break;
            case TORNADO_BIT | PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == TORNADO_BIT) {
                    ((Tornado) fixA.getUserData()).inContact = true;
                }
                else {
                    ((Tornado) fixB.getUserData()).inContact = true;
                }
                break;
            case CLOUDS_BIT | PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == CLOUDS_BIT) {
                    ((Entity) fixA.getUserData()).onContact();
                }
                else {
                    ((Entity) fixB.getUserData()).onContact();
                }
                break;
        }
    }

    /**
     * Run when contact is ended. Nearly empty since nothing special needs to happen when a contact is ended
     * @param contact The object that contains information about the collision
     */
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(cDef == (TORNADO_BIT | PLAYER_BIT)){
            if(fixA.getFilterData().categoryBits == TORNADO_BIT) {
                ((Tornado) fixA.getUserData()).reset();
            }
            else {
                ((Tornado) fixB.getUserData()).reset();
            }
        }

        if(cDef == (CLOUDS_BIT | PLAYER_BIT)){
            if(fixA.getFilterData().categoryBits == CLOUDS_BIT) {
                ((Cloud) fixA.getUserData()).resetAlpha();
            }
            else {
                ((Cloud) fixB.getUserData()).resetAlpha();
            }
        }
    }

    /**
     * (Not Used)
     * Can be called before beginContact to pre emptively solve it
     * @param contact The object that contains information about the collision
     * @param oldManifold Predicted impulse based on old data
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    /**
     * (Not Used)
     * Can be called before beginContact to post emptively solve it
     * @param contact The object that contains information about the collision
     * @param impulse The signal recieved
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
