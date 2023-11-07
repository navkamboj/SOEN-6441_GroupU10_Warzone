package Models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * This class is used to test the functionalities of Bomb class methods.
 */
public class BombTest {

    /**
     * Player who executes the command
     */
    Player d_playerName1;

    /**
     * Second Player
     */
    Player d_playerName2;

    /**
     * Bomb order one.
     */
    Bomb d_bombOne;

    /**
     * Bomb order two.
     */
    Bomb d_bombtwo;

    /**
     * Bomb order three.
     */
    Bomb d_bombthree;

    /**
     * Bomb Order four.
     */
    Bomb d_bombfour;

    /**
     * Order list.
     */
    List<Order> d_orders;

    /**
     * This is used to check validation of bomb class file
     */
    Bomb d_checkBombValidation;
    /**
     * current Gamestate.
     */
    GameState d_gameState;



    /**
     *
     * This method bombSetup called every time before the execution of every test case that
     * are available in this BombTest class file
     */
    @Before
    public void bombSetup(){
        d_gameState = new GameState();
        d_orders = new ArrayList<Order>();

        d_playerName1 = new Player();
        d_playerName1.setD_playerName("Nihal");
        d_playerName2 = new Player();
        d_playerName2.setD_playerName("Pranjlesh");

        List<Country> l_countries = new ArrayList<Country>();
        l_countries.add(new Country("India"));
        l_countries.add(new Country("Nepal"));

        d_playerName1.setD_ownedCountries(l_countries);

        List<Country> l_countriesOfMap = new ArrayList<Country>();
        Country l_countryone = new Country(1, "India", 1);
        Country l_countrytwo = new Country(2, "Nepal", 2);
        Country l_countrythree = new Country(3, "Sri lanka", 2);
        Country l_countryfour = new Country(4, "Pakistan", 2);
        Country l_countryfive = new Country(5, "China", 2);

        l_countrythree.setD_numberOfArmies(10);
        l_countryfour.setD_numberOfArmies(5);
        l_countryfive.setD_numberOfArmies(1);

        l_countriesOfMap.add(l_countryone);
        l_countriesOfMap.add(l_countrytwo);
        l_countriesOfMap.add(l_countrythree);
        l_countriesOfMap.add(l_countryfour);
        l_countriesOfMap.add(l_countryfive);

        Map l_map = new Map();
        l_map.setD_countries(l_countriesOfMap);
        d_gameState.setD_map(l_map);
        d_bombOne = new Bomb(d_playerName1, "Sri lanka");
        d_bombtwo = new Bomb(d_playerName1, "India");
        d_bombthree = new Bomb(d_playerName1, "Pakistan");
        d_bombfour = new Bomb(d_playerName1, "China");

        d_orders.add(d_bombOne);
        d_orders.add(d_bombtwo);

        d_playerName2.setD_executeOrders(d_orders);
    }

    @Test
    public void testExecutionOfBomb(){
        // Test calculation of half armies.
        d_bombOne.execute(d_gameState);
        Country l_target = d_gameState.getD_map().getCountryByName("Sri lanka");
        assertEquals("5", l_target.getD_numberOfArmies().toString());

        // Test round down of armies calculation.
        d_bombthree.execute(d_gameState);
        Country l_target2 = d_gameState.getD_map().getCountryByName("Pakistan");
        assertEquals("2", l_target2.getD_numberOfArmies().toString());

        // Testing:- targeting a territory with 1 army will leave 0.
        d_bombfour.execute(d_gameState);
        Country l_target3 = d_gameState.getD_map().getCountryByName("China");
        assertEquals("0", l_target3.getD_numberOfArmies().toString());
    }

    @Test
    public void testBombValidation(){
        boolean l_actual=d_bombOne.isValid(d_gameState);
        assertTrue(l_actual);

        d_checkBombValidation=new Bomb(d_playerName1,"Afghanistan");
        boolean l_answer=d_checkBombValidation.validateOrder(d_gameState);
        assertFalse(l_answer);

    }
}
