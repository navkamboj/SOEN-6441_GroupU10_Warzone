package Models;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test cases to validate functionality of player class
 *
 * @version 2.0.0
 * @author Harsh Tank
 */

public class PlayerTest {

    GameState l_currGameState = new GameState();
    List<Player> d_playerList = new ArrayList<>();
    List<Order> d_orderList = new ArrayList<>();
    Player d_newPlayer = new Player();

    @Before
    public void setUp() {
        d_playerList.add(new Player("Harsh"));
        d_playerList.add(new Player("Nihal"));

        Map l_mapObj = new Map();
        Country l_country1 = new Country(1, "India", 1);
        Country l_country2 = new Country(2, "Srilanka", 1);
        l_country1.setD_neighborCountryIDs(Arrays.asList(2));
        List<Country> l_countryList = new ArrayList<>();
        l_countryList.add(l_country1);
        l_countryList.add(l_country2);
        l_mapObj.setD_countries(l_countryList);
        l_currGameState.setD_map(l_mapObj);

    }

    /**
     * Test case to validate that no of deployed armies should not be more than reinforcement pool
     */
    @Test
    public void testVerifyDeployOrderArmies() {
        d_newPlayer.setD_noOfAllocatedArmies(8);
        String l_totalNoOfArmies = "5";
        boolean l_bool = d_newPlayer.verifyDeployOrderArmies(d_newPlayer, l_totalNoOfArmies);
        assertFalse(l_bool);
    }



}
