package Models;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Controller.GameEngine;
import Exceptions.InvalidMap;
/**
 * This class is used to test the functionalities of OrderExecutionPhase class methods.
 */
public class OrderExecutionPhaseTest {
    /**
     * first player.
     */
    Player d_playerName1;

    /**
     * Second Player.
     */
    Player d_playerName2;

    /**
     * current Game State.
     */
    GameState d_gameState;

    /**
     * This method orderExecutionSetup called every time before the execution of every test case that
     * are available in this OrderExecutionPhaseTest class file
     *
     * @throws InvalidMap Invalid Map
     */
    @Before
    public void orderExecutionSetup() throws InvalidMap {
        d_gameState = new GameState();
        d_playerName1 = new Player();
        d_playerName1.setD_playerName("Nihal");
        d_playerName2 = new Player();
        d_playerName2.setD_playerName("Navjot");

        List<Country> l_countries = new ArrayList<Country>();
        Country l_countryName = new Country(0, "India", 1);
        l_countryName.setD_numberOfArmies(15);
        l_countries.add(l_countryName);

        Country l_neighbourCountryName = new Country(1, "Nepal", 1);
        l_neighbourCountryName.addingNeighbor(0);
        l_countryName.addingNeighbor(1);
        l_neighbourCountryName.setD_numberOfArmies(5);
        l_countries.add(l_neighbourCountryName);

        d_playerName1.setD_ownedCountries(l_countries);

        Map l_newMap = new Map();
        l_newMap.setD_countries(l_countries);
        d_gameState.setD_map(l_newMap);
        d_gameState.setD_playerList(Arrays.asList(d_playerName1, d_playerName2));
    }

    /**
     *Test if any player conquered all the countries of the map to end the game
     */
    @Test
    public void testEndOfTheGame() {
        OrderExecutionPhase l_executeOrder = new OrderExecutionPhase(new GameEngine(), d_gameState);
        assertTrue(l_executeOrder.checkForGameEnd(d_gameState));
    }
}
