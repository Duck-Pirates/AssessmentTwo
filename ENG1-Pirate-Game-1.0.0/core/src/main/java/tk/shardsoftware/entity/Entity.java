package tk.shardsoftware.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;
import tk.shardsoftware.util.ResourceUtil;

/**
 * To be used for any object in the world that moves or is interactable
 * 
 * @author James Burnell
 */
public abstract class Entity {

	/** The maximum speed the entity is allowed to travel at */
	protected float maximumSpeed = 250f;
	/**
	 * The direction the entity is facing in degrees. Note that this can be
	 * different than the direction it is moving.
	 */
	protected float direction = 0f;
	/** The vector the entity is traveling in (unnormalized) */
	protected Vector2 velocityVec = new Vector2(0, 0);
	/** The entity's current position in the world */
	protected Vector2 positionVec = new Vector2(0, 0);
	protected float width, height;
	/** The hitbox of the entity. Note that this does not rotate. */
	protected Rectangle hitbox = new Rectangle();
	/** How large the hitbox is in comparison to the entity */
	protected float hitboxScale = 1f;
	/** The World object the entity belongs to */
	protected World worldObj;
	/** Tells the World object if the entity should be deleted */
	public boolean remove = false;
	/** The texture used to depict the entity */
	protected TextureRegion texture;
	protected boolean ignoreWorldCollision = false;
	protected boolean ignoreEntityCollision = false;
	/** Determines if it prevents other entities from moving if they collide */
	protected boolean isSolid = true;

	/**
	 * Constructor for Entity.
	 * 
	 * @param worldObj World object that the entity will be part of
	 * @param x x-position of the entity
	 * @param y y-position of the entity
	 * @param w width of the entity
	 * @param h height of the entity
	 */
	protected Entity(World worldObj, float x, float y, float w, float h) {
		this(worldObj, w, h);
		setPosition(x, y);
	}

	/**
	 * Constructor for Entity.
	 * 
	 * @param worldObj World object that the entity will be part of
	 * @param w width of the entity
	 * @param h height of the entity
	 */
	protected Entity(World worldObj, float w, float h) {
		// If texture is unset, use null
		texture = new TextureRegion(ResourceUtil.nullTexture);
		setWidth(w);
		setHeight(h);
		this.worldObj = worldObj;
	}

	/**
	 * Entity constructor to be used only for test purposes where the worldObj is
	 * not required
	 */
	protected Entity() {
		this(null, 0, 0, 50, 50);
	}

	/**
	 * Set the texture of the entity
	 * 
	 * @param textureName the path/name of the texture file
	 * @return This entity object for easy building
	 */
	public Entity setTexture(String textureName) {
		texture = new TextureRegion(ResourceUtil.getTexture(textureName));
		return this;
	}

	/**
	 * Get the Texture of the entity
	 * 
	 * @return The entity's texture
	 */
	public TextureRegion getTexture() {
		return texture;
	}

	/**
	 * Set the scale of the entity's hitbox relative to its size (useful if the
	 * texture you are using doesn't match the hitbox)
	 * 
	 * @param hbScale the new scale of the hitbox
	 */
	public void setHitboxScale(float hbScale) {
		this.hitboxScale = hbScale;
		updateHitbox();
	}

	/**
	 * Get the scale of the entity's hitbox
	 * 
	 * @return The scale of the entity's hitbox
	 */
	public float getHitboxScale() {
		return this.hitboxScale;
	}

	/**
	 * The logical game function called on each game tick
	 * 
	 * @param delta the time between the previous update and this one
	 */
	public void update(float delta) {
		// prevent entity from traveling faster than the maximum speed
		velocityVec.limit(maximumSpeed);
		stepPosition(delta);
		keepWithinWorldBounds();
	}

	/** Ensure the entity doesn't leave the world */
	private void keepWithinWorldBounds() {
		boolean flag = false;
		if (positionVec.x < 0) {
			setPosition(0, positionVec.y);
			flag = true;
		} else if (positionVec.x + width > World.getWidth()) {
			setPosition(World.getWidth() - width, positionVec.y);
			flag = true;
		}
		if (positionVec.y < 0) {
			setPosition(positionVec.x, 0);
			flag = true;
		} else if (positionVec.y + height > World.getHeight()) {
			setPosition(positionVec.x, World.getHeight() - height);
			flag = true;
		}
		if (flag) onTouchingBorder();
	}

	/**
	 * Called when the entity touches the border of the map.
	 */
	public void onTouchingBorder() {
	}

	/**
	 * Step the position forward one game tick.<br>
	 * Tests for collisions with other entities before moving.
	 * 
	 * @param delta the time between the previous update and this one
	 */
	protected void stepPosition(float delta) {
		// Calculate the hitbox after next step
		Rectangle nextHitbox = new Rectangle(hitbox).setPosition(hitbox.x + velocityVec.x * delta,
				hitbox.y + velocityVec.y * delta);

		boolean collidedFlag = testCollision(nextHitbox);

		if (collidedFlag) {
			// Has collided so remove velocity (ignoring momentum)
			// FIXME: Only zero out the direction of collision
			velocityVec.setZero();
		} else {
			// update the position of the entity
			positionVec.mulAdd(velocityVec, delta);
			// sync hitbox with entity position
			updateHitbox();
		}
	}

	/**
	 * Check if the entity's hitbox collides with another hitbox.
	 * 
	 * @param nextHitbox The other hitbox
	 * @return {@code true} if collided, {@code false} otherwise
	 */
	public boolean testCollision(Rectangle nextHitbox) {
		// Change to bounding-box hierarchy if performance is too low
		boolean collidedFlag = false;

		if (!ignoreEntityCollision) {
			collidedFlag |= worldObj.getEntities().stream().filter(e -> !e.equals(this))
					.anyMatch(e -> e.isSolid && e.hitbox.overlaps(nextHitbox));
		}

		// Test for world collision
		if (!ignoreWorldCollision) {
			collidedFlag |= worldObj.worldMap.isSolidTileWithinArea(nextHitbox);
		}

		return collidedFlag;
	}

	/**
	 * Calculates the center point of the entity
	 * 
	 * @return The center point of the entity
	 */
	public Vector2 getCenterPoint() {
		return new Vector2(positionVec.x + width / 2f, positionVec.y + height / 2f);
	}

	/**
	 * Set the center point of the entity
	 * 
	 * @param x the new center x position
	 * @param y the new center y position
	 */
	public void setCenter(float x, float y) {
		positionVec.x = x - width / 2f;
		positionVec.y = y - height / 2f;
	}

	/**
	 * Set the center point of the entity
	 * 
	 * @param center the new center point of the entity
	 */
	public void setCenter(Vector2 center) {
		setCenter(center.x, center.y);
		updateHitbox();
	}

	/**
	 * Get the direction of the entity
	 * 
	 * @return The direction of the entity
	 */
	public float getDirection() {
		return direction;
	}

	/**
	 * Set the angle the entity is facing and standardize to be within 0-360 degrees
	 * 
	 * @param angle the angle in degrees
	 */
	public void setDirection(float angle) {
		float temp = angle % 360;
		this.direction = temp < 0 ? temp + 360 : temp;
	}

	/**
	 * Adds a value to the direction
	 * 
	 * @param angle the angle in degrees to rotate by
	 */
	public void rotate(float angle) {
		this.setDirection(direction + angle);
	}

	public Vector2 getVelocity() {
		return velocityVec;
	}

	public void setVelocity(Vector2 vel) {
		setVelocity(vel.x, vel.y);
	}

	public void addVelocity(Vector2 vel) {
		addVelocity(vel.x, vel.y);
	}

	public void setVelocity(float x, float y) {
		this.velocityVec.set(x, y);
	}

	public void addVelocity(float x, float y) {
		this.velocityVec.add(x, y);
	}

	public Vector2 getPosition() {
		return positionVec;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	/** Synchronizes the hitbox with the position of the entity */
	public void updateHitbox() {
		hitbox.setWidth(width * hitboxScale);
		hitbox.setHeight(height * hitboxScale);
		hitbox.setX(positionVec.x + (width - hitbox.width) / 2f);
		hitbox.setY(positionVec.y + (height - hitbox.height) / 2f);
	}

	/**
	 * Set the current position of the Entity
	 * 
	 * @param pos the new position
	 * 
	 * @see #setPosition(float, float)
	 */
	public void setPosition(Vector2 pos) {
		setPosition(pos.x, pos.y);
	}

	/**
	 * Set the current position of the Entity to (x, y)
	 * 
	 * @param x the new x position
	 * @param y the new y position
	 */
	public void setPosition(float x, float y) {
		this.positionVec.set(x, y);
		updateHitbox();
	}

	/**
	 * Set the maximum speed of the entity
	 * 
	 * @param speed the maximum speed
	 * @return This entity object for easy building
	 */
	public Entity setMaxSpeed(float speed) {
		this.maximumSpeed = speed;
		return this;
	}

	public float getMaxSpeed() {
		return maximumSpeed;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		updateHitbox();
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		updateHitbox();
	}

	public boolean isIgnoreWorldCollision() {
		return ignoreWorldCollision;
	}

	public void setIgnoreWorldCollision(boolean ignoreWorldCollision) {
		this.ignoreWorldCollision = ignoreWorldCollision;
	}

	public boolean isIgnoreEntityCollision() {
		return ignoreEntityCollision;
	}

	public void setIgnoreEntityCollision(boolean ignoreEntityCollision) {
		this.ignoreEntityCollision = ignoreEntityCollision;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}

	public float getX() {
		return positionVec.x;
	}

	public float getY() {
		return positionVec.y;
	}

	/** Called when entity is removed from the world */
	public void onRemove() {
	}

}
