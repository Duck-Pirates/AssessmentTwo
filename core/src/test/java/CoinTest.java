import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.entities.Coin;
import com.mygdx.pirategame.screens.GameScreen;
import org.junit.Test;
import static org.junit.Assert.*;


public class CoinTest {
    @Test
    public void testCoinUpdate() {
        Coin testCoin = new Coin(new GameScreen(new PirateGame()), 10, 10);

        // Schedule a destroy
        testCoin.setToDestroyed = true;

        // Assert that this destroy happens only after an update
        assertFalse(testCoin.destroyed);

        testCoin.update();

        assertTrue(testCoin.destroyed);

        Coin testCoin2 = new Coin(new GameScreen(new PirateGame()), 15, 15);
        assertFalse(testCoin2.setToDestroyed);
        assertFalse(testCoin2.destroyed);
        assertEquals(b2body.getPosition().x - getWidth(), testCoin2.getX());
        assertEquals(b2body.getPosition().y - getHeight(), testCoin2.getY());
    }
}