package com.mygdx.pirategame.fsm;

import static com.mygdx.pirategame.configs.Constants.PPM;
import com.mygdx.pirategame.entities.*;
import com.mygdx.pirategame.screens.GameScreen;

import java.util.ArrayList;
import java.util.Objects;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;

/**
 * EnemyStateMachine enum class
 * This keeps track of what the different ships should be doing at each stage. The states change based on various
 * conditions and give back different steering outputs
 *
 * @author Alex Davis
 * @version 1.0
 */
public enum EnemyStateMachine implements State<EnemyShip> {
	
	SLEEP() {

		/**
		 * Updates the ships that are in the Sleep State
		 * @param entity The entity in the Sleep State
		 */
		public void update(EnemyShip entity) { entity.getStateMachine().changeState(WANDER); }

	},
	
	WANDER() {

		/**
		 * Creates a Wander behaviour's steering vector
		 * @param entity The entity in the Wander State
		 */
		public void enter(EnemyShip entity) {

			Wander<Vector2> wander = new Wander<>(entity)
					.setAlignTolerance((float) Math.PI / 32)
					.setEnabled(true)
					.setFaceEnabled(true)
					.setWanderOffset(100f / PPM)
					.setWanderRadius(50f / PPM)
					.setWanderRate((float) Math.PI / 2);
			entity.setBehavior(wander);

		}

		/**
		 * Updates the ships that are in the Wander State
		 * @param entity The entity in the Wander state
		 */
		public void update(EnemyShip entity) {

			// Check to see if the entity is close to the Player
			if (EntityProximity.findAgents(entity, GameScreen.getShips().subList(0, 1), 1000 / PPM) != null) {
				entity.getStateMachine().changeState(PURSUE);
			}
			// Check to see if the entity is close to a coin
			else if (EntityProximity.findAgents(entity, GameScreen.getCoins(), 1000 / PPM) != null) {
				entity.getStateMachine().changeState(SEEK);
			}

		}
	},

	SEEK() {

		/**
		 * Creates a Seek behaviour's steering vector
		 * @param entity The entity in the Seek State
		 */
		public void enter(EnemyShip entity) {

			ArrayList<Entity> coins = EntityProximity.findAgents(entity, GameScreen.getCoins(), 1000 / PPM);
			Seek<Vector2> seek = new Seek<>(entity)
					.setEnabled(true)
					.setTarget(Objects.requireNonNull(coins).get(0));
			entity.setBehavior(seek);

		}

		/**
		 * Updates the ships that are in the Seek State
		 * @param entity The entity in the Seek state
		 */
		public void update(EnemyShip entity) {

			// Check to see if the entity is close to the Player
			if (EntityProximity.findAgents(entity, GameScreen.getShips().subList(0, 1), 1000 / PPM) != null) {
				entity.getStateMachine().changeState(PURSUE);
			}
			// Check to see if the entity is close to the target
			else if (entity.getPosition() == ((Seek<Vector2>) entity.getBehavior()).getTarget()) {
				entity.getStateMachine().changeState(WANDER);
			}

		}
	},
	
	PURSUE() {

		/**
		 * Creates a Pursue behaviour's steering vector
		 * @param entity The entity in the Pursue State
		 */
		public void enter(EnemyShip entity) {

			ArrayList<Entity> ships = EntityProximity.findAgents(entity, GameScreen.getShips().subList(0, 1), 1000 / PPM);
			Pursue<Vector2> pursue = new Pursue<>(entity, (SteerableEntity) Objects.requireNonNull(ships).get(0))
					.setEnabled(true)
					.setMaxPredictionTime(0.01f);
			entity.setBehavior(pursue);

		}

		/**
		 * Updates the ships that are in the Pursue State
		 * @param entity The entity in the Pursue state
		 */
		public void update(EnemyShip entity) {

			if(entity.getHealth() <= 15) {
				entity.getStateMachine().changeState(FLEE); // This is retreat mode
			}
			// Check to see if the target is too far away now
			else if(entity.getPosition().dst(((Pursue<Vector2>) entity.getBehavior()).getTarget().getPosition()) >= 1000 / PPM) {
				entity.getStateMachine().changeState(WANDER);
			}

			// Logic to see if the ship should be shooting or not
			if(GdxAI.getTimepiece().getTime() - entity.getTimeFired() > 1f) {
				entity.fire();
				entity.setTimeFired(GdxAI.getTimepiece().getTime());
			}

		}

	},
	
	FLEE() {

		/**
		 * Creates a Flee behaviour's steering vector
		 * @param entity The entity in the Flee State
		 */
		public void enter(EnemyShip entity) {

			Flee<Vector2> flee = new Flee<>(entity)
					.setTarget(GameScreen.getCoins().get(0));
			entity.setBehavior(flee);

		}

		/**
		 * Updates the ships that are in the Flee State
		 * @param entity The entity in the Flee state
		 */
		public void update(EnemyShip entity) {

			if(entity.getHealth() >= 70) { // Check to see if the ship has enough health to go back to search for the player
				entity.getStateMachine().changeState(WANDER);
			}

		}
	};

	@Override
	public void enter(EnemyShip entity) {}

	@Override
	public void update(EnemyShip entity) {}

	/**
	 * (Not Used)
	 *
	 * Eliminate the AI behaviour
	 *
	 * @param entity The entity that needs to stop to have the entity behavior
	 */
	@Override
	public void exit(EnemyShip entity) { entity.setBehavior(null); }

	/**
	 * (Not Used)
	 */
	@Override
	public boolean onMessage(EnemyShip entity, Telegram telegram) { return false; }

}
