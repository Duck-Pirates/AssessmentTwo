package tk.shardsoftware.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** @author James Burnell */
public class EntityShipTest {

	/** A ship's direction is based on the direction it is traveling */
	@Test
	public void testSetDirectionDoesNothing() {
		EntityShip e = new EntityShip(null);
		assertEquals(0, e.direction);
		e.setDirection(180);
		assertEquals(0, e.direction);
	}

	@Test
	public void testSetVelocity() {
		EntityShip e = new EntityShip(null);
		e.setVelocity(1, 1);
		assertEquals(45, e.direction);
		e.setVelocity(-1, 0);
		assertEquals(180, e.direction);
	}

}
