package Models;

import Exceptions.InvalidMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to test the functionalities of Diplomacy class methods.
 * @version 2.0.0
 * @author Nihal Galani
 */
public class DiplomacyTest {
    /**
     * Player who executes the command
     */
    Player d_playerName1;

    /**
     * Second Player
     */
    Player d_playerName2;

    /**
     * Bomb Order.
     */
    Bomb d_bomb1;

    /**
     * diplomacy order.
     */
    Diplomacy d_diplomacy1;

    /**
     * current Game State.
     */
    GameState d_gameState;

    /**
     * This method diplomacySetup called every time before the execution of every test case that
     * are available in this DiplomacyTest class file
     * @throws InvalidMap Exception
     */
    @Before
    public void diplomacySetup() throws InvalidMap{
        d_gameState = new GameState();
        d_playerName1 = new Player();
        d_playerName1.setD_playerName("Nihal");
        d_playerName2 = new Player();
        d_playerName2.setD_playerName("Harsh");

        List<Country> l_countries = new ArrayList<Country>();
        Country l_countryName = new Country(0, "India", 1);
        l_countryName.setD_numberOfArmies(15);
        l_countries.add(l_countryName);

        Country l_neighbourCountryName = new Country(1, "Nepal", 1);
        l_neighbourCountryName.addingNeighbor(0);
        l_countryName.addingNeighbor(1);
        l_neighbourCountryName.setD_numberOfArmies(5);
        l_countries.add(l_neighbourCountryName);

        List<Country> l_anotherCountries = new ArrayList<Country>();
        Country l_notneighbour = new Country(2, "Afghanistan", 1);
        l_notneighbour.setD_numberOfArmies(8);
        l_anotherCountries.add(l_notneighbour);

        Map l_newMap = new Map();
        l_newMap.setD_countries(new ArrayList<Country>(){{ addAll(l_countries); addAll(l_anotherCountries); }});

        d_gameState.setD_map(l_newMap);
        d_playerName1.setD_ownedCountries(l_countries);
        d_playerName2.setD_ownedCountries(l_anotherCountries);
        List<Player> l_listOfPlayer = new ArrayList<Player>();
        l_listOfPlayer.add(d_playerName1);
        l_listOfPlayer.add(d_playerName2);
        d_gameState.setD_playerList(l_listOfPlayer);
        d_diplomacy1 = new Diplomacy(d_playerName2.getD_playerName(), d_playerName1);
        d_bomb1 = new Bomb(d_playerName2, "India");
    }

    /**
     * testing the diplomacy command.
     */
    @Test
    public void testDiplomacyExecution(){
        d_diplomacy1.execute(d_gameState);
        assertEquals(d_playerName1.d_negotiatedWith.get(0), d_playerName2);
    }

    /**
     * Testing bomb command after diplomacy
     */
    @Test
    public void NegotiationWorking(){
        d_diplomacy1.execute(d_gameState);
        d_bomb1.execute(d_gameState);
        assertEquals(d_gameState.getLatestLog().trim(), "Order of Bomb:-Bomb India will not going to be execute because Harsh has a negotiation pact with target territory: India's player");
    }
}
