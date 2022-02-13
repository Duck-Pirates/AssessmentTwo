package tk.shardsoftware.screens;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.W;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;

import tk.shardsoftware.RobotPlayerInput;

public class GameScreenTest {

	private static RobotPlayerInput rpi;

	@BeforeAll
	public static void init() {
		rpi = new RobotPlayerInput();
		Gdx.input = rpi;
	}

	@BeforeEach
	public void reset() {
		rpi.clearKeys();
	}

	@Test
	public void testRotNoInput() {
		assertEquals(-999, GameScreen.calcPlayerGoalAngle());
	}

	@Test
	public void testRotSingleInputsHorz() {
		rpi.keyPress(A);
		assertEquals(180, GameScreen.calcPlayerGoalAngle());
		rpi.releaseKey(A);

		rpi.keyPress(D);
		assertEquals(0, GameScreen.calcPlayerGoalAngle());
		rpi.releaseKey(D);
	}

	@Test
	public void testRotSingleInputsVert() {
		rpi.keyPress(W);
		assertEquals(90, GameScreen.calcPlayerGoalAngle());
		rpi.releaseKey(W);

		rpi.keyPress(S);
		assertEquals(270, GameScreen.calcPlayerGoalAngle());
		rpi.releaseKey(S);
	}

	@Test
	public void testRotUpDiagonals() {
		rpi.keyPress(W);
		rpi.keyPress(D);
		assertEquals(45, GameScreen.calcPlayerGoalAngle());
		rpi.releaseKey(D);
		rpi.keyPress(A);
		assertEquals(135, GameScreen.calcPlayerGoalAngle());
		rpi.releaseKey(A);
		rpi.releaseKey(W);
	}

	@Test
	public void testRotDownDiagonals() {
		rpi.keyPress(S);
		rpi.keyPress(A);
		assertEquals(225, GameScreen.calcPlayerGoalAngle());
		rpi.releaseKey(A);

		rpi.keyPress(D);
		assertEquals(315, GameScreen.calcPlayerGoalAngle());
		rpi.releaseKey(D);
		rpi.releaseKey(S);
	}

	@Test
	public void testRotHorzCancel() {
		rpi.keyPress(A);
		rpi.keyPress(D);
		assertEquals(-333, GameScreen.calcPlayerGoalAngle());
	}

	@Test
	public void testRotVertCancel() {
		rpi.keyPress(W);
		rpi.keyPress(S);
		assertEquals(-333, GameScreen.calcPlayerGoalAngle());
	}

	@Test
	public void testRotHorzCancelUp() {
		rpi.keyPress(W);
		rpi.keyPress(A);
		rpi.keyPress(D);
		assertEquals(90, GameScreen.calcPlayerGoalAngle());
	}

	@Test
	public void testRotHorzCancelDown() {
		rpi.keyPress(S);
		rpi.keyPress(A);
		rpi.keyPress(D);
		assertEquals(270, GameScreen.calcPlayerGoalAngle());
	}

	@Test
	public void testRotVertCancelLeft() {
		rpi.keyPress(W);
		rpi.keyPress(S);
		rpi.keyPress(A);
		assertEquals(180, GameScreen.calcPlayerGoalAngle());
	}

	@Test
	public void testRotVertCancelRight() {
		rpi.keyPress(W);
		rpi.keyPress(S);
		rpi.keyPress(D);
		assertEquals(0, GameScreen.calcPlayerGoalAngle());
	}

}
