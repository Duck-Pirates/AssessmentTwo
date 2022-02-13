package tk.shardsoftware.entity;

import java.util.function.Function;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.SoundManager;

import static tk.shardsoftware.World.WORLD_TILE_SIZE;

/**
 * Represents the physical location of a college on a map. College is
 * implemented as an extension of Entity which does not move. (i.e physics are
 * never applied to it) Also contains static methods which facilitate adding a
 * number of colleges to the map.
 * 
 * @author Hector Woods
 */
public class College extends Entity implements IRepairable, ICannonCarrier {

	private EntityShip player;
	public boolean isFriendly = false;
	protected String collegeName;
	public String collegeTexturePath = "textures/entity/college.png";
	public Sound hitSound = ResourceUtil.getSound("audio/entity/college-hit.mp3");
	public Sound cannonSfx = ResourceUtil.getSound("audio/entity/cannon.mp3");
	public float maxHealth = 100;
	public float health = maxHealth;
	public float timeUntilFire = 0f;
	public float reloadTime = 3f;
	public float fireDistance = 350f;

	public float shipSpawnWaitTime = 60f;
	public float timeUntilNextShipSpawn = 0f;
	/** The total number of ships the college has spawned */
	public int shipsSpawned = 0;
	/** The maximum number of ships the college should spawn */
	public int maxShipsToSpawn = 1;

	/** The search function for the spawn point of the ships */
	private Function<Vector2, Boolean> shipPosConds = vector2 -> {
		TileType tile = worldObj.worldMap.getTile((int) vector2.x, (int) vector2.y);
		// Check the tile is in water
		if (tile != TileType.WATER_DEEP && tile != TileType.WATER_SHALLOW) {
			return false;
		}
		// Check the position is neither too far or too close to the college
		int tileX = (int) vector2.x * WORLD_TILE_SIZE;
		int tileY = (int) vector2.y * WORLD_TILE_SIZE;
		float distFromCollege = this.getPosition().dst(tileX, tileY);
		if (distFromCollege > 275 || distFromCollege < 50) {
			return false;
		}
		return true;
	};

	/**
	 * Reduces the health of the College by 'dmgAmount'. See IDamageable
	 * 
	 * @param dmgAmount the amount of damage for the college to take
	 */
	public void damage(float dmgAmount) {
		this.health = this.health - dmgAmount;
		if (this.health <= 0) {
			this.remove = true; // flag for removal by entity handler
		}
	}

	/**
	 * Spawns a new ship close to the college.
	 * 
	 * @return {@code true} if successfully spawned ship, {@code false} otherwise
	 */
	public boolean spawnShip() {
		// Do not spawn more ships than the maximum
		if (shipsSpawned >= maxShipsToSpawn) return false;

		// don't spawn if on cooldown or if a friendly college
		if (timeUntilNextShipSpawn > 0 || isFriendly) return false;

		Vector2 startPos = worldObj.worldMap.searchMap(shipPosConds);
		// If nothing was found, stop processing
		if (startPos == null) return false;

		startPos = new Vector2(startPos.x * WORLD_TILE_SIZE, startPos.y * WORLD_TILE_SIZE);

		EntityAIShip ship = new EntityAIShip(worldObj, player);
		ship.setPosition(startPos);
		worldObj.addEntity(ship);

		timeUntilNextShipSpawn += shipSpawnWaitTime;
		shipsSpawned++;
		return true;
	}

	public boolean fireCannons() {
		// If friendly then don't attack the player.
		// TODO: Make friendly colleges attack enemy ships
		if (isFriendly) {
			return false;
		}

		// Do not fire if still reloading
		if (timeUntilFire > 0) return false;
		// Do not fire if too far away from the player
		Vector2 center = getCenterPoint();
		Vector2 playerPos = player.getPosition();
		float distFromPlayer = center.dst(playerPos);
		if (distFromPlayer > fireDistance) {
			return false;
		}

		// Reload
		timeUntilFire += reloadTime;
		// Play sfx
		SoundManager.playSound(cannonSfx, 8);

		for (int i = -2; i < 2; i++) {
			for (int j = -2; j < 2; j++) {
				if (!(i == 0 && j == 0)) { // if i and j ==0 then the cannonball doesn't move
					Vector2 dirVec = new Vector2(i, j);

					float xPos = center.x + dirVec.x;
					float yPos = center.y + dirVec.y;

					EntityCannonball cb = new EntityCannonball(worldObj, xPos, yPos, dirVec, this);
					worldObj.addEntity(cb);
				}
			}
		}

		return true;
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
	public float getCannonDamage() {
		return 5;
	}

	/**
	 * Increases the health of the College by 'repairAmount'.
	 * 
	 * @param repairAmount the amount of health for the College's health to be
	 *        increased by
	 * 
	 */
	public void repair(float repairAmount) {
		this.health = this.health + repairAmount;
		if (this.health > this.maxHealth) {
			this.health = this.maxHealth;
		}
	}

	/**
	 * Gets the current health of the College.
	 * 
	 * @return current health of the College.
	 */
	public float getHealth() {
		return this.health;
	}

	/**
	 * Gets the current maxHealth of the College.
	 * 
	 * @return current maxHealth of the College.
	 */
	public float getMaxHealth() {
		return this.maxHealth;
	}

	public void update(float delta) {
		timeUntilFire -= delta;
		timeUntilNextShipSpawn -= delta;
		timeUntilNextShipSpawn = timeUntilNextShipSpawn <= 0 ? 0 : timeUntilNextShipSpawn;
		timeUntilFire = timeUntilFire <= 0 ? 0 : timeUntilFire;
	}

	/**
	 * Get the name of the College.
	 * 
	 * @return the name of the college.
	 */
	public String getName() {
		return this.collegeName;
	}

	/**
	 * @param worldObj A valid worldObj that the college will be located in
	 * @param collegeName the name of the college
	 * @param x The x-position of the entity on creation
	 * @param y The y-position of the entity on creation
	 * @param w The width of the entity in pixels
	 * @param h The height of the entity in pixels
	 * @param player the ship entity controlled by the player
	 */
	public College(World worldObj, String collegeName, float x, float y, int w, int h,
			EntityShip player) {
		super(worldObj, x, y, w, h);
		this.setTexture(collegeTexturePath);
		this.collegeName = collegeName;
		this.player = player;
		this.setHitboxScale(0.8f);
	}

	public void onRemove() {
		if (health <= 0) worldObj.onCollegeDestroyed(this);
	}

}
