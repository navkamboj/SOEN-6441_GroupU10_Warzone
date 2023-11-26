package Models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * This class is used to test cheater strategy.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class CheaterPlayerTest {
    /**
     * First Player.
     */
    Player d_playerName;

    /**
     * Random player on which test wil be performed.
     */
    Player d_arbitraryPlayerName;

    /**
     * Player's Strategy.
     */
    PlayerBehaviorStrategy d_behaviorStrategy;

    /**
     * Cheater player.
     */
    CheaterPlayer d_cheaterPlayerName = new CheaterPlayer();

    /**
     * current Game State.
     */
    GameState d_gameState = new GameState();

    /**
     * object of country class.
     */
    Country d_countryName;

    /**
     * Setup before each test case for testing behavior strategy.
     */
    @Before
    public void setup() {

        this.d_countryName = new Country(1, "India", 1);
        Country l_countryName2 = new Country(2, "China", 1);
        Country l_countryName3 = new Country(3, "Pakistan", 1);


        d_countryName.addingNeighbor(2);
        d_countryName.addingNeighbor(3);
        l_countryName2.addingNeighbor(3);

        l_countryName2.setD_numberOfArmies(10);
        l_countryName3.setD_numberOfArmies(5);

        ArrayList<Country> l_allCountries = new ArrayList<Country>();
        l_allCountries.add(d_countryName);
        l_allCountries.add(l_countryName2);
        l_allCountries.add(l_countryName3);


        Continent l_continent = new Continent("Asia");
        l_continent.setD_continentID(1);
        l_continent.setD_countries(l_allCountries);

        ArrayList<Continent> l_continents = new ArrayList<Continent>();
        l_continents.add(l_continent);

        ArrayList<Country> l_ownedCountriesPlayerOne = new ArrayList<Country>();
        l_ownedCountriesPlayerOne.add(d_countryName);

        ArrayList<Country> l_ownedCountriesPlayerTwo = new ArrayList<Country>();
        l_ownedCountriesPlayerTwo.add(l_countryName2);
        l_ownedCountriesPlayerTwo.add(l_countryName3);

        d_behaviorStrategy = new CheaterPlayer();
        d_playerName = new Player("Harsh");
        d_playerName.setD_noOfAllocatedArmies(10);
        d_playerName.setD_ownedCountries(l_ownedCountriesPlayerOne);
        d_playerName.setStrategy(d_behaviorStrategy);

        d_arbitraryPlayerName = new Player("Opponent");
        RandomPlayer l_arbitraryPlayerBehaviorStrategy = new RandomPlayer();
        d_arbitraryPlayerName.setStrategy(l_arbitraryPlayerBehaviorStrategy);
        d_arbitraryPlayerName.setD_ownedCountries(l_ownedCountriesPlayerTwo);
        d_arbitraryPlayerName.setD_noOfAllocatedArmies(0);


        List<Player> l_listOfPlayer = new ArrayList<Player>();
        l_listOfPlayer.add(d_playerName);
        l_listOfPlayer.add(d_arbitraryPlayerName);

        Map l_map = new Map();
        l_map.setD_countries(l_allCountries);
        l_map.setD_continents(l_continents);
        d_gameState.setD_map(l_map);
        d_gameState.setD_playerList(l_listOfPlayer);
    }

    /**
     * Validate if it creates an null Order.
     *
     * @throws IOException Exception
     */
    @Test
    public void testOrderCreationNull() throws IOException {
        String l_acceptOrder = d_playerName.getPlayerOrder(d_gameState);
        assertNull(l_acceptOrder);
    }

    /**
     * Validate that all unallocated armies are deployed to player's country.
     *
     * @throws IOException Exception
     */
    @Test
    public void testunassignedArmyDeployment() throws IOException {
        String l_acceptedOrder = d_playerName.getPlayerOrder(d_gameState);
        assertNull(l_acceptedOrder);

        int l_unassignedArmies = d_playerName.getD_noOfAllocatedArmies();
        assertEquals(0, l_unassignedArmies);
    }

    /**
     * validate that player now owns all opponent's countries.
     *
     * @throws IOException Exception
     */
    @Test
    public void testCheaterOwnsAllOpponents() throws IOException {
        String l_acceptOrder = d_playerName.getPlayerOrder(d_gameState);
        assertNull(l_acceptOrder);

        int l_ownedCountriesCount = d_playerName.getD_ownedCountries().size();
        assertEquals(3, l_ownedCountriesCount);

        int l_targetOwnedCountriesCount = d_arbitraryPlayerName.getD_ownedCountries().size();
        assertEquals(0, l_targetOwnedCountriesCount);
    }
}