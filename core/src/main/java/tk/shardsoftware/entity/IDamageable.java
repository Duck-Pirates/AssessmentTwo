package tk.shardsoftware.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * A collection of methods to allow objects to be damaged. To be used in
 * conjunction with {@link Entity}.
 * 
 * @author James Burnell
 */
public interface IDamageable {

	/**
	 * Returns the maximum amount of health the object has
	 * 
	 * @return The maximum health
	 */
	public float getMaxHealth();

	/**
	 * Returns the current health of the object
	 * 
	 * @return The current health
	 */
	public float getHealth();

	/**
	 * Damage the object by a given amount
	 * 
	 * @param dmgAmount the amount of damage for the object to take
	 */
	public void damage(float dmgAmount);

	/**
	 * @return The hitbox of the object
	 */
	public Rectangle getHitbox();

}
