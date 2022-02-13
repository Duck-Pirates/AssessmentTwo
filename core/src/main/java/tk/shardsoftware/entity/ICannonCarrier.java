/**
 * 
 */
package tk.shardsoftware.entity;

/**
 * Defines what an object with cannons should be able to do
 * 
 * @author James Burnell
 */
public interface ICannonCarrier {

	/**
	 * Fires all cannons.
	 * 
	 * @return {@code true} if spawned cannonballs, {@code false} if still reloading
	 */
	public boolean fireCannons();

	/**
	 * Returns how long it takes for the carrier to reload in seconds
	 * 
	 * @return The length of time it takes to reload
	 */
	public float getReloadTime();

	/**
	 * Returns how many seconds are remaining before the carrier has reloaded. 0
	 * means it has reloaded and is ready to fire.
	 * 
	 * @return the length of time remaining before the cannon can fire again
	 */
	public float getReloadProgress();

	/**
	 * Returns the average amount of damage each cannonball will do
	 * 
	 * @return The amount of damage the cannonballs deal
	 */
	public float getCannonDamage();

}
