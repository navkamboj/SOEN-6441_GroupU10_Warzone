package Models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.Before;

/**
 * This class is used to test the Strategy of Benevolent Player behavior.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class BenevolentPlayerTest {
    /**
     * First Player.
     */
    Player d_playerName;

    /**
     * Player's Strategy.
     */
    PlayerBehaviorStrategy d_behaviorStrategy;

    /**
     * Benevolent player strategy.
     */
    BenevolentPlayer d_benevolentPlayerName = new BenevolentPlayer();

    /**
     * current Game State.
     */
    GameState d_gameState = new GameState();

    /**
     * object of Country class.
     */
    Country d_countryName;

    /**
     * Setup For testing Benevolent Behavior Strategy.
     */
    @Before
    public void setup() {
        this.d_countryName = new Country(1, "South Korea", 1);
        Country l_countryName2 = new Country(2, "North Korea", 1);
        Country l_countryName3 = new Country(3, "Japan", 1);

        d_countryName.addingNeighbor(2);
        d_countryName.addingNeighbor(3);

        this.d_countryName.setD_numberOfArmies(15);
        l_countryName2.setD_numberOfArmies(5);
        l_countryName3.setD_numberOfArmies(10);

        ArrayList<Country> l_list = new ArrayList<Country>();
        l_list.add(d_countryName);
        l_list.add(l_countryName2);
        l_list.add(l_countryName3);

        d_behaviorStrategy = new BenevolentPlayer();
        d_playerName = new Player("Nihal");
        d_playerName.setD_ownedCountries(l_list);
        d_playerName.setStrategy(d_behaviorStrategy);
        d_playerName.setD_noOfAllocatedArmies(12);

        List<Player> l_listOfPlayer = new ArrayList<Player>();
        l_listOfPlayer.add(d_playerName);

        Map l_map = new Map();
        l_map.setD_countries(l_list);
        l_map.setD_countries(l_list);
        d_gameState.setD_map(l_map);
        d_gameState.setD_playerList(l_listOfPlayer);
    }

    /**
     * validate if benevolent player deploy armies on the weakest country or not
     */
    @Test
    public void testWeakestCountry() {
        assertEquals("North Korea", d_benevolentPlayerName.retrieveWeakestCountry(d_playerName).getD_countryName());
    }

    /**
     * Validate if benevolent player attacks to the weakest neighbor or not.
     */
    @Test
    public void testWeakestNeighbor() {
        assertEquals("North Korea", d_benevolentPlayerName.retrieveWeakestNeighbor(d_countryName, d_gameState, d_playerName).getD_countryName());
    }

}