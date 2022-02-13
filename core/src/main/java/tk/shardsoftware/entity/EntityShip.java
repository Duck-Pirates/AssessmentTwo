package tk.shardsoftware.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.SoundManager;

/**
 * @author James Burnell
 */
public class EntityShip extends Entity implements ICannonCarrier, IRepairable {

	/** The length of time in seconds required to wait between firing cannons */
	public float reloadTime = 1f;
	/** How much time left until cannons can be fired */
	public float timeUntilFire = 0f;

	protected float maxHealth = 100f;
	protected float health = maxHealth;

	protected String collegeName;
	public boolean isPlayer = false;

	private Sound cannonSfx = ResourceUtil.getSound("audio/entity/cannon.mp3");

	/** @param worldObj the World object the ship belongs to */
	public EntityShip(World worldObj) {
		super(worldObj, 0, 0, 50, 50);
		this.setTexture("textures/entity/playership.png");
		this.setMaxSpeed(100);
		this.setHitboxScale(0.39f);
	}

	public String getCollegeName() {
		return this.collegeName;
	}

	public void setCollegeName(String name) {
		this.collegeName = name;
	}

	public void update(float delta) {
		super.update(delta);
		// direction = velocityVec.angleDeg();
		velocityVec.setAngleDeg(direction);

		// TODO: Write water drag system
		velocityVec.scl(0.99f);
		timeUntilFire -= delta;
		timeUntilFire = timeUntilFire <= 0 ? 0 : timeUntilFire;
	}

	/**
	 * Set the texture of the entity
	 * 
	 * @param textureName the path/name of the texture file
	 * @return This entity object for easy building
	 */
	public EntityShip setTexture(String textureName) {
		texture = new TextureRegion(ResourceUtil.getTexture(textureName));
		return this;
	}

	/**
	 * Rotates the ship towards a specific angle. Depending on the distance, it will
	 * rotate either clockwise or counterclockwise - whichever is shorter.
	 * 
	 * @param goalAngle the desired angle of the ship
	 * @param delta the time elapsed since the last update
	 */
	public void rotateTowardsGoal(float goalAngle, float delta) {
		delta *= 60; // normalize to 60fps
		float angle = getDirection();
		// float speed = getVelocity().len();

		double rads = Math.toRadians(angle);
		addVelocity((float) Math.cos(rads) * delta, (float) Math.sin(rads) * delta);

		if (angle <= 90 && goalAngle >= 270) goalAngle -= 360;
		if (angle >= 270 && goalAngle <= 90) goalAngle += 360;
		if (angle > 180 && goalAngle < 90) goalAngle += 360;

		if (angle != goalAngle) {
			// delta * 2deg/s
			float turnAmount = delta * 2;

			float testAngle = Math.abs(angle - goalAngle);
			turnAmount = turnAmount > testAngle ? testAngle : turnAmount;

			rotate(angle > goalAngle ? -turnAmount : turnAmount);
		}
	}

	/**
	 * Spawns two cannonballs, one on each side of the ship.
	 * 
	 * @return {@code true} if spawned cannonballs, {@code false} if still reloading
	 */
	public boolean fireCannons() {
		// Do not fire if still reloading
		if (timeUntilFire > 0) return false;
		fireCannonball(true);
		fireCannonball(false);
		// Reload
		timeUntilFire += reloadTime;
		// Play sfx
		SoundManager.playSound(cannonSfx, 8);
		return true;
	}

	/**
	 * Spawns a cannonball on the side of the ship
	 * 
	 * @param rightOrLeft {@code true} for right, {@code false} for left
	 */
	private void fireCannonball(boolean rightOrLeft) {
		Vector2 center = getCenterPoint();

		Vector2 dirVec = new Vector2(1, 1).setAngleDeg(direction + (rightOrLeft ? -90 : 90))
				.setLength(hitbox.width / 2f);

		float xPos = center.x + dirVec.x;
		float yPos = center.y + dirVec.y;

		EntityCannonball cb = new EntityCannonball(worldObj, xPos, yPos, dirVec, this);
		worldObj.addEntity(cb);
	}

	@Override
	public float getReloadTime() {
		return reloadTime;
	}

	@Override
	public float getReloadProgress() {
		return timeUntilFire;
	}

	@Override
	public float getMaxHealth() {
		return maxHealth;
	}

	@Override
	public float getHealth() {
		return health;
	}

	@Override
	public void damage(float dmgAmount) {
		health -= dmgAmount;
		health = health < 0 ? 0 : health;
		if (health <= 0) {
			this.remove = true;
		}
	}

	@Override
	public void repair(float repairAmount) {
		health += repairAmount;
		health = health > maxHealth ? maxHealth : health;
	}

	@Override
	public float getCannonDamage() {
		return 10;
	}

}
