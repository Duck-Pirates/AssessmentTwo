import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.entities.College;
import com.mygdx.pirategame.world.AvailableSpawn;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
package com.mygdx.pirategame.world;
import static com.mygdx.pirategame.configs.Constants.PPM;


public class CollegeTest {
    @Test
    public void testCollegeSpawn() {
        // Doesn't allow out-of-map spawning
        AvailableSpawn av_spawn = new AvailableSpawn();

        assertFalse(av_spawn.add( *UPPER BOUND* + 1, 15));

        // Does allow in-map spawning
        assertTrue(av_spawn.add(*LOWER BOUND* - 1, noSpawn.yCap - 1));

    }

    @Test
    public void testImposingDeletion() {
        College testCollege = new College(this, "Test College", 100 / PPM, 200 / PPM,
                1);

        // Schedule a destroy
        testCollege.setSetToDestroy(true);

        // Assert that this destroy happens only after an update
        assertFalse(testCollege.isDestroyed());

        testCollege.update(10.5F);

        assertTrue(testCollege.isDestroyed());

        // Do the above implicitly...

        College testCollege2 = new College(this, "Test College", 100 200,
                1);

        testCollege.setHealth(0);

        assertFalse(testCollege.isDestroyed());

        testCollege.update(10.5F);

        assertTrue(testCollege.isDestroyed());
    }
}