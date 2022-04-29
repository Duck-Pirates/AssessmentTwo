package com.mygdx.pirategame.fsm;

import static com.mygdx.pirategame.configs.Constants.PPM;

import java.util.ArrayList;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.entities.EnemyShip;
import com.mygdx.pirategame.entities.Entity;
import com.mygdx.pirategame.entities.SteerableEntity;
import com.mygdx.pirategame.screens.GameScreen;

public enum EnemyStateMachine implements State<EnemyShip> {
	
	SLEEP() {
		public void update(EnemyShip entity) {
			entity.stateMachine.changeState(WANDER);
		}
	},
	
	WANDER() {
		public void enter(EnemyShip entity) {
			Wander<Vector2> wander = new Wander<Vector2>(entity)
					.setAlignTolerance(0.0174533f)
					.setEnabled(true)
					.setFaceEnabled(true)
					.setWanderOffset(0f)
					.setWanderRadius(50f / PPM)
					.setWanderRate((float) Math.PI / 2);
			entity.setBehavior(wander);
		}
		
		public void update(EnemyShip entity) {
			if (EntityProximity.findAgents(entity, GameScreen.getShips(), 600 / PPM) != null) { 
				entity.stateMachine.changeState(PERSUE); 
			} else if (EntityProximity.findAgents(entity, GameScreen.getCoins(), 600 / PPM) != null) {
				entity.stateMachine.changeState(SEEK);
			}
		}
	},
	
	SEEK() {
		public void enter(EnemyShip entity) {
			ArrayList<Entity> coins = EntityProximity.findAgents(entity, GameScreen.getCoins(), 600 / PPM);
			Seek<Vector2> seek = new Seek<Vector2>(entity)
					.setEnabled(true)
					.setTarget(coins.get(0));
			entity.setBehavior(seek);
		}
		
		public void update(EnemyShip entity) {
			if (EntityProximity.findAgents(entity, GameScreen.getShips(), 600 / PPM) != null) { 
				entity.stateMachine.changeState(PERSUE); 
			} else if (entity.getPosition() == ((Seek<Vector2>) entity.getBehavior()).getTarget()) {
				entity.stateMachine.changeState(WANDER);
			}
		}
	},
	
	PERSUE() {
		public void enter(EnemyShip entity) {
			ArrayList<Entity> ships = EntityProximity.findAgents(entity, GameScreen.getShips(), 600 / PPM);
			Pursue<Vector2> persue = new Pursue<Vector2>(entity, (SteerableEntity) ships.get(0))
					.setEnabled(true)
					.setMaxPredictionTime(0.01f);
			entity.setBehavior(persue);
		}
		
		public void update(EnemyShip entity) {
			// attack enemy
			if(entity.health <= 15) {
				entity.stateMachine.changeState(FLEE);
			} else if(entity.getPosition().dst(((Pursue<Vector2>) entity.getBehavior()).getTarget().getPosition()) >= 1000 / PPM) {
				entity.stateMachine.changeState(WANDER);
			}
			
			entity.fire();
		}
	},
	
	FLEE() {
		public void enter(EnemyShip entity) {
			Flee<Vector2> flee = new Flee<Vector2>(entity)
					.setTarget(GameScreen.coins.get(0));
			entity.setBehavior(flee);
		}
		
		public void update(EnemyShip entity) {
			if(entity.health >= 70 && EntityProximity.findAgents(entity, GameScreen.getShips(), 600 / PPM) == null) {
				entity.stateMachine.changeState(WANDER);
			}
		}
	};

	@Override
	public void enter(EnemyShip entity) {

	}

	@Override
	public void update(EnemyShip entity) {
		
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
