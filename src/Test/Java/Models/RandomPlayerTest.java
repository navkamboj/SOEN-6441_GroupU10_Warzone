package Models;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import java.io.IOException;
import java.util.List;

/**
 * This class is used to test the RandomPlayer behavior strategy.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class RandomPlayerTest {
    /**
     * First Player.
     */
    Player d_playerName;

    /**
     *Player's strategy.
     */
    PlayerBehaviorStrategy d_behaviorStrategy;

    /**
     * current Game State.
     */
    GameState d_gameState = new GameState();

    /**
     * Setup For testing Random Behavior Strategy.
     */
    @Before
    public void setup() {
        Country l_countryName1 = new Country(1, "Canada", 1);
        Country l_countryName2 = new Country(2, "Usa", 1);
        ArrayList<Country> l_listOfCountry = new ArrayList<Country>();
        l_listOfCountry.add(l_countryName1);
        l_listOfCountry.add(l_countryName2);

        d_behaviorStrategy = new RandomPlayer();
        d_playerName = new Player();
        d_playerName.setD_ownedCountries(l_listOfCountry);
        d_playerName.setStrategy(d_behaviorStrategy);
        d_playerName.setD_noOfAllocatedArmies(4);
        List<Player> l_playerList = new ArrayList<Player>();
        l_playerList.add(d_playerName);

        Map l_map = new Map();
        l_map.setD_countries(l_listOfCountry);
        d_gameState.setD_map(l_map);
        d_gameState.setD_playerList(l_playerList);
    }

    /**
     * validates if this method creates an Order String and compare it with first order is deploy.
     *
     * @throws IOException Exception
     */
    @Test
    public void testOrderCreation() throws IOException {
        assertEquals(d_playerName.getPlayerOrder(d_gameState).split(" ")[0], "deploy");
    }


}
