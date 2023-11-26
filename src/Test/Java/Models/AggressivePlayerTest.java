package Models;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;

/**
 * This class is used to test the functionalities of AggressivePlayer class methods.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class AggressivePlayerTest {
    /**
     * First Player.
     */
    Player d_playerName;

    /**
     * Player's Strategy.
     */
    PlayerBehaviorStrategy d_behaviorStrategy;

    /**
     * Aggressive player.
     */
    AggressivePlayer d_playerAggressive = new AggressivePlayer();

    /**
     * current Game State.
     */
    GameState d_gameState = new GameState();

    /**
     * object of country class.
     */
    Country d_countryName;

    /**
     * Setup before test case for testing behavior strategy..
     */
    @Before
    public void setup() {
        this.d_countryName = new Country(1, "India", 1);
        Country l_countryName2 = new Country(2, "China", 1);
        Country l_countryName3 = new Country(3, "Pakistan", 1);

        d_countryName.addingNeighbor(2);
        d_countryName.addingNeighbor(3);

        this.d_countryName.setD_numberOfArmies(15);
        l_countryName2.setD_numberOfArmies(10);
        l_countryName3.setD_numberOfArmies(5);

        ArrayList<Country> l_list = new ArrayList<Country>();
        l_list.add(d_countryName);
        l_list.add(l_countryName2);
        l_list.add(l_countryName3);

        d_behaviorStrategy = new AggressivePlayer();
        d_playerName = new Player("Nihal");
        d_playerName.setD_ownedCountries(l_list);
        d_playerName.setStrategy(d_behaviorStrategy);
        d_playerName.setD_noOfAllocatedArmies(12);

        List<Player> l_playerList = new ArrayList<Player>();
        l_playerList.add(d_playerName);

        Map l_map = new Map();
        l_map.setD_countries(l_list);
        d_gameState.setD_map(l_map);
        d_gameState.setD_playerList(l_playerList);
    }

    /**
     * validate that if the most powerful country attacks on the weakest country or not.
     */
    @Test
    public void testPowerfulCountry() {
        assertEquals("India", d_playerAggressive.getMostPowerfulCountry(d_playerName, d_gameState).getD_countryName());
    }
}