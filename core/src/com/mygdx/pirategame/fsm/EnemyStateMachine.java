package com.mygdx.pirategame.fsm;

import static com.mygdx.pirategame.PirateGame.PPM;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.entities.EnemyShip;
import com.mygdx.pirategame.entities.SteerableEntity;

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
			// if (enemy == close) { entity.stateMachine.changeState(PERSUE) }
			// else if (coin == close) { entity.stateMachine.changeState(SEEK); }
		}
	},
	
	SEEK() {
		public void enter(EnemyShip entity, Vector2 target) {
			@SuppressWarnings("unchecked")
			Seek<Vector2> seek = new Seek<Vector2>(entity)
					.setEnabled(true)
					.setTarget((Location<Vector2>) target);
			entity.setBehavior(seek);
		}
		
		public void update(EnemyShip entity) {
			// if (coinCollected) { entity.stateMachine.changeState(WANDER); }
		}
	},
	
	PERSUE() {
		public void enter(EnemyShip entity, SteerableEntity target) {
			 Pursue<Vector2> persue = new Pursue<Vector2>(entity, target);
			 entity.setBehavior(persue);
		}
		
		public void update(EnemyShip entity) {
			// persue enemy
		}
	},
	
	FLEE() {
		public void enter(EnemyShip entity) {
			// Flee<Vector2> flee = new Flee<Vector2>(entity);
			// entity.setBehavior(flee);
		}
		
		public void update(EnemyShip entity) {
			// if(healthLow || outNumbered ) { flee enemy towards college }
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
		
	}

	@Override
	public boolean onMessage(EnemyShip entity, Telegram telegram) {
		return false;
	} 

}
