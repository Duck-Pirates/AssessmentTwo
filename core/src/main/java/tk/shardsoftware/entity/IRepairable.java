/**
 * 
 */
package tk.shardsoftware.entity;

/**
 * Similar to a damageable object but it can also be repaired
 * 
 * @author James Burnell
 * @see IDamageable
 */
public interface IRepairable extends IDamageable {

	/**
	 * Repair the object by the given amount (i.e increase the object's health)
	 * 
	 * @param repairAmount the amount of health for the object's health to be
	 *        increased by
	 */
	public void repair(float repairAmount);

}
