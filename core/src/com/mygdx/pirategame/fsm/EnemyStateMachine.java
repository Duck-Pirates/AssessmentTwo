package com.mygdx.pirategame.fsm;

import static com.mygdx.pirategame.configs.Constants.PPM;

import java.util.ArrayList;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.entities.EnemyShip;
import com.mygdx.pirategame.entities.Entity;
import com.mygdx.pirategame.entities.SteerableEntity;
import com.mygdx.pirategame.screens.GameScreen;

public enum EnemyStateMachine implements State<EnemyShip> {
	
	SLEEP() {
		public void update(EnemyShip entity) {
			entity.getStateMachine().changeState(WANDER);
		}
	},
	
	WANDER() {
		public void enter(EnemyShip entity) {
			Wander<Vector2> wander = new Wander<Vector2>(entity)
					.setAlignTolerance((float) Math.PI / 32)
					.setEnabled(true)
					.setFaceEnabled(true)
					.setWanderOffset(100f / PPM)
					.setWanderRadius(50f / PPM)
					.setWanderRate((float) Math.PI / 2);
			entity.setBehavior(wander);
		}
		
		public void update(EnemyShip entity) {
			if (EntityProximity.findAgents(entity, GameScreen.getShips().subList(0, 1), 1000 / PPM) != null) {
				entity.getStateMachine().changeState(PERSUE); 
			} 
		}
	},
	
	PERSUE() { 
		// Finds the nearest ship to the entity and persues it (note the player is the 0th item in the array so will always be prioritized) 
		public void enter(EnemyShip entity) {
			ArrayList<Entity> ships = EntityProximity.findAgents(entity, GameScreen.getShips().subList(0, 1), 1000 / PPM);
			Pursue<Vector2> persue = new Pursue<Vector2>(entity, (SteerableEntity) ships.get(0))
					.setEnabled(true)
					.setMaxPredictionTime(0.01f);
			entity.setBehavior(persue);
		}
		
		public void update(EnemyShip entity) {
			if(entity.getPosition().dst(((Pursue<Vector2>) entity.getBehavior()).getTarget().getPosition()) >= 1000 / PPM) {
				entity.getStateMachine().changeState(WANDER);
			} else {
				// attack enemy
				float distanceToTargetSquare = ((Pursue<Vector2>) entity.getBehavior()).getTarget().getPosition().dst2(entity.getPosition());
				if(GdxAI.getTimepiece().getTime() - entity.getTimeFired() > 1f && distanceToTargetSquare <= (400 / PPM) * (400 / PPM)) {
					entity.fire();
					entity.setTimeFired(GdxAI.getTimepiece().getTime());
				}
			}
		}
	};

	@Override
	public void enter(EnemyShip entity) {

	}

	@Override
	public void update(EnemyShip entity) {
		if(entity.isAligenceChange()) {
			entity.getStateMachine().changeState(SLEEP);
		}
	}

	@Override
	public void exit(EnemyShip entity) {
		entity.setBehavior(null);
	}

	@Override
	public boolean onMessage(EnemyShip entity, Telegram telegram) {
		return false;
	} 

}
