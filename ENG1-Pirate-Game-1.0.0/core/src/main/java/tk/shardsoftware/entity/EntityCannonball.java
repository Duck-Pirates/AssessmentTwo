package tk.shardsoftware.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;
import tk.shardsoftware.util.SoundManager;

/**
 * Cannonballs do damage to other entities that can be damaged.
 * 
 * @author James Burnell
 * @see IDamageable
 * @see ICannonCarrier
 */
public class EntityCannonball extends Entity {

	/** The object that created the cannonball */
	private ICannonCarrier parentObj;

	/** The maximum distance a cannonball can travel before stopping */
	public float maximumTravelDist = 200f;
	/** The position the cannonball started at */
	private Vector2 startingPos;

	/**
	 * @param worldObj the World the cannonball is part of
	 * @param x the center x position
	 * @param y the center y position
	 * @param parentObj the {@link ICannonCarrier} object that created the
	 *        cannonball
	 */
	public EntityCannonball(World worldObj, float x, float y, ICannonCarrier parentObj) {
		super(worldObj, 5, 5);
		this.setCenter(x, y);
		this.setMaxSpeed(250);
		this.setHitboxScale(0.5f);
		this.setIgnoreWorldCollision(true);
		this.setIgnoreEntityCollision(true);
		this.setTexture("textures/entity/cannonball.png");
		this.setSolid(false);
		this.parentObj = parentObj;
		this.startingPos = this.getPosition().cpy();
	}

	/**
	 * Constructor for EntityCannonball
	 * 
	 * @param worldObj Instance of World, the world the cannonball will be a part of
	 * @param x the x-position of the cannonball
	 * @param y the y-position of the cannonball
	 * @param dirVec the initial direction of the cannonball
	 * @param parentObj the entity which shot the cannonball
	 */
	public EntityCannonball(World worldObj, float x, float y, Vector2 dirVec,
			ICannonCarrier parentObj) {
		this(worldObj, x, y, parentObj);
		setDirection(dirVec);
	}

	/**
	 * Sets the direction of the cannonball.
	 * 
	 * @param dirVec the new direction for the cannonball.
	 */
	public void setDirection(Vector2 dirVec) {
		this.setVelocity(dirVec.setLength(maximumSpeed));
	}

	@Override
	public void update(float delta) {
		// Cannonballs spin through the air
		this.setDirection(direction + delta * 60 * 15);
		super.update(delta);

		// If the cannonball has travelled the maximum distance, remove
		if (getDistanceTravelled2() >= maximumTravelDist * maximumTravelDist) {
			this.remove = true;
		}
	}

	/**
	 * Check that the cannonball is able to damage the object
	 * 
	 * @param obj object to be damaged
	 * @return {@code true} if cannonball can damage, {@code false} otherwise
	 */
	public boolean checkCanDamage(IDamageable obj) {
		// if parent is a college, then make sure it can't damage other colleges
		if (parentObj instanceof College
				&& (obj instanceof College || (obj instanceof EntityAIShip))) {
			return false;
		}
		// if parent is the player, make sure they can't damage a friendly college.
		if (parentObj instanceof EntityShip && ((EntityShip) parentObj).isPlayer
				&& obj instanceof College && ((College) obj).isFriendly == true) {
			return false;
		}
		return true;
	}

	/**
	 * Called when the cannonball touches an entity which implements
	 * {@link IDamageable}
	 * 
	 * @param obj the object the cannonball is touching
	 */
	public void onTouchingDamageable(IDamageable obj) {

		if (!checkCanDamage(obj)) {
			return;
		}

		obj.damage(
				MathUtils.random(parentObj.getCannonDamage() - 2, parentObj.getCannonDamage() + 2));
		if (obj instanceof College) {
			SoundManager.playSound(((College) obj).hitSound);
		}
		this.remove = true;
	}

	/**
	 * Checks whether an object which implements IDamageable is the same as the
	 * object which shot this cannonball
	 * 
	 * @param dmgObj the object to test equivalence
	 * @return {@code true} if parent is the specified object, {@code false}
	 *         otherwise
	 */
	@SuppressWarnings("unlikely-arg-type")
	public boolean isObjParent(IDamageable dmgObj) {
		return dmgObj.equals(parentObj);
	}

	/**
	 * Called when the cannonball touches the borders of the map.
	 */
	@Override
	public void onTouchingBorder() {
		this.remove = true;
	}

	/**
	 * Get the distance travelled
	 * 
	 * @return The distance travelled
	 */
	public float getDistanceTravelled() {
		return Vector2.len(startingPos.x - positionVec.x, startingPos.y - positionVec.y);
	}

	/**
	 * Get the distance travelled squared
	 * 
	 * @return The distance travelled
	 */
	public float getDistanceTravelled2() {
		return Vector2.len2(startingPos.x - positionVec.x, startingPos.y - positionVec.y);
	}

	public float getMaximumTravelDist() {
		return maximumTravelDist;
	}

	public void setMaximumTravelDist(float maximumTravelDist) {
		this.maximumTravelDist = maximumTravelDist;
	}

}
