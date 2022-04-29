package com.mygdx.pirategame.fsm;

import java.util.ArrayList;

import com.mygdx.pirategame.entities.Entity;
import com.mygdx.pirategame.entities.SteerableEntity;

public class EntityProximity {
	
	public static ArrayList<Entity> findAgents(SteerableEntity owner, Iterable<? extends Entity> agents, float radius) {
		
		ArrayList<Entity> targets = new ArrayList<>();
		for (Entity currentAgent : agents) {
			if (currentAgent != owner) {
				float squareDistance = owner.getPosition().dst2(currentAgent.getPosition());
				if (squareDistance < radius * radius) {
					if (currentAgent instanceof SteerableEntity) {
						if (((SteerableEntity) currentAgent).getCollege() != owner.getCollege()) {
							targets.add(currentAgent);
						}
					} else {
						targets.add(currentAgent);
					}
				}
			}
		}
		
		if(targets.isEmpty()) {
			return null;
		}
		return targets;
	}
}