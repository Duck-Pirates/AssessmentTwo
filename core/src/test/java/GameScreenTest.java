import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.entities.College;
import com.mygdx.pirategame.entities.EnemyShip;
import com.mygdx.pirategame.world.AvailableSpawn;
import org.junit.Test;

import static com.mygdx.pirategame.screens.GameScreen.changeDamage;
import static com.mygdx.pirategame.screens.GameScreen.colleges;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import com.mygdx.pirategame.screens.GameScreen;
import static com.mygdx.pirategame.configs.Constants.PPM;
import com.mygdx.pirategame.configs.Difficulty;

public class GameScreenTest {
    @Test
    public void testCollegesSetup() {
        // Assert that the correct number of colleges has been added to the hashmap
        assertEquals(4, colleges.size());

        // Create a new college
        College testCollege = new College(this, "Test College", 100 / PPM, 200 / PPM,
                 30);

        // Assert that it is correctly created with all the correct attributes generated
        assertEquals((100 / PPM), testCollege.getX());
        assertEquals((200 / PPM), testCollege.getY());

        assertEquals(30, testCollege.getFleet().size());

        assertEquals("Test College", testCollege.getFleet().get(0).getCollege());

        for (int i = 0; i < testCollege.getFleet().size(); i++) {
            assertEquals("Test College", testCollege.getFleet().get(i).getCollege());
        }
    }

    @Test
    public void testCollegesDestroy() {
        College testCollege = new College(this, "Test College", 100 / PPM, 200 / PPM,
                 30);

        // Assert that the college starts with the correct amount of health
        assertEquals(100, testCollege.getHealth());

        // That the health goes down as expected
        testCollege.setHealth(testCollege.getHealth() - 50);

        assertEquals(50, testCollege.getHealth());
        assertFalse(testCollege.isDestroyed());

        // That the college is destroyed as health passes zero, but damage below zero won't crash the game
        testCollege.setHealth(testCollege.getHealth() - 70);

        assertTrue(testCollege.isDestroyed());
    }

    @Test
    public void testShipSetup() {
        College testCollege = new College(this, "Test College", 100 / PPM, 200 / PPM,
                1);

        EnemyShip ship = testCollege.getFleet().get(0);

        assertEquals(100, ship.getHealth());
        assertEquals("Test College", ship.getCollege());
        assertFalse(ship.isDestroyed());
    }

    @Test
    public void testShipDestroy() {
        College testCollege = new College(this, "Test College", 100 /.PPM, 200 / PPM,
                 1);

        EnemyShip ship = testCollege.getFleet().get(0);

        // Assert that the college starts with the correct amount of health
        assertEquals(100, testCollege.getHealth());

        // That the health goes down as expected
        testCollege.setHealth(testCollege.getHealth() - 50);

        assertEquals(50, testCollege.getHealth());
        assertFalse(testCollege.isDestroyed());

        // That the college is destroyed as health passes zero, but damage below zero won't crash the game
        testCollege.setHealth(testCollege.getHealth() - 70);

        assertTrue(testCollege.isDestroyed());
    }

    @Test
    public void testGamePause() {
        GameScreen gs = new GameScreen(new PirateGame(), new Difficulty )

        gs.resume();
        gs.pause();

        assertEquals(gs.GAME_PAUSED, gs.gameStatus);
    }

    @Test
    public void testGameResume() {
        GameScreen gs = new GameScreen(new PirateGame(), new Difficulty )

        gs.pause();
        gs.resume();

        assertEquals(gs.GAME_PAUSED, gs.gameStatus);
    }

    @Test
    public void testChangeDamage() {
        College testCollege = new College(this, "Test College", 100 / PPM, 200 / PPM,
                5);

        int previousDamage = testCollege.getFleet(0).damage;

        changeDamage(15);

        for (int i = 0; i < testCollege.getFleet().size(); i++) {
            assertEquals(previousDamage + 15, testCollege.getFleet(i).damage)
        }

    }
}