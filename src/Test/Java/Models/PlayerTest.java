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

    /**
     * Test case to validate that after execution order is removed from the list
     */
    @Test
    public void testNext_order() {

        Order l_order1 = new Deploy(d_playerList.get(0), "Canada", 5);
        Order l_order2 = new Deploy(d_playerList.get(1), "Australia", 5);

        d_orderList.add(l_order1);
        d_orderList.add(l_order2);

        d_playerList.get(0).setD_executeOrders(d_orderList);
        d_playerList.get(1).setD_executeOrders(d_orderList);

        Order l_topOrder = d_playerList.get(0).next_order();
        assertEquals(l_order1, l_topOrder);
        assertEquals(1, d_playerList.get(0).getD_executeOrders().size());
    }

    /**
     * Test case to validate the no of armies after deploy order and size of order list
     */
    @Test
    public void testDeployOrder() {
        Player l_player1 = new Player("Harsh");
        l_player1.setD_noOfAllocatedArmies(10);
        l_player1.deployOrder("Deploy Canada 5");
        l_player1.deployOrder("Deploy Australia 3");
        assertEquals(l_player1.getD_noOfAllocatedArmies().toString(), "2");
        assertEquals(l_player1.getD_executeOrders().size(), 2);
    }

    /**
     * Validate advance command
     */
    @Test
    public void testCreateAdvanceOrder() {
        Player l_player1 = new Player("Harsh");
        l_player1.createAdvanceOrder("advance India Srilanka 3", l_currGameState);
        assertEquals(l_player1.getD_executeOrders().size(), 1);
    }

    /**
     * Test case to validate that advance order is only executed on neighbouring countries
     */
    @Test
    public void testValidateAdjacency() {
        Player l_player = new Player("Harsh");
        assertTrue(l_player.validateAdjacency(l_currGameState, "India", "Srilanka"));
        assertFalse(l_player.validateAdjacency(l_currGameState, "Srilanka", "India"));
    }

    /**
     * Test case to validate that invalid command does not enters the list
     */
    @Test
    public void testInvalidCreateAdvanceOrder() {
        Player l_player1 = new Player("Harsh");
        // India is not neighbour of Srilanka
        l_player1.createAdvanceOrder("advance Srilanka India 3", l_currGameState);
        assertEquals(l_player1.getD_executeOrders().size(), 0);
        assertNotEquals(l_player1.getD_executeOrders().size(), 1);
    }

}
