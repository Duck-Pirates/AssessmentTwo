package com.mygdx.pirategame.fsm;

import com.mygdx.pirategame.entities.Entity;
import com.mygdx.pirategame.entities.SteerableEntity;

import java.util.ArrayList;

/**
 * EntityProximity class
 * Checks for proximity with other entities
 *
 * @author Alex Davis
 * @version 1.0
 */
public class EntityProximity {

	/**
	 * Finds if, there are any, agents that are in a circle with a specific radius. This circle has the center located
	 * with an Entity world center to check these entities are close enough to it
	 *
	 * @param owner The Entity that is seeking the targets. Its center is the center of circle
	 * @param agents The possible targets
	 * @param radius The circle's radius
	 * @return @Nullable The targets
	 */
	public static ArrayList<Entity> findAgents(SteerableEntity owner, Iterable<? extends Entity> agents, float radius) {
		
		ArrayList<Entity> targets = new ArrayList<>();
		for (Entity currentAgent : agents) {
			if (currentAgent != owner) {
				float squareDistance = owner.getPosition().dst2(currentAgent.getPosition());
				if (squareDistance < radius * radius) {
					if (currentAgent instanceof SteerableEntity) {
						if (!((SteerableEntity) currentAgent).getCollege().equals(owner.getCollege())) {
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