package Models;

import Exceptions.InvalidCommand;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
/**
 * This CountryTest class file is used to test some important functionalities of Country file
 *
 * @version 3.0.0
 * @author Nihal Galani
 */

public class DeployTest {
    /**
     * First Player
     */
    Player d_playerName1;

    /**
     * Second Player
     */
    Player d_playerName2;

    /**
     * Deploy Order 1.
     */
    Deploy d_deployOrder1;

    /**
     * Deploy Order 2.
     */
    Deploy d_deployOrder2;

    /**
     * Current Game State.
     */
    GameState d_gameState = new GameState();

    /**
     * The setup is called before each test case of this class is executed.
     */
    @Before
    public void setup() {
        d_playerName1 = new Player();
        d_playerName1.setD_playerName("Nihal");

        d_playerName2 = new Player();
        d_playerName2.setD_playerName("Yatish");

        List<Country> l_countries = new ArrayList<Country>();
        l_countries.add(new Country("France"));
        l_countries.add(new Country("Germany"));
        d_playerName1.setD_ownedCountries(l_countries);
        d_playerName2.setD_ownedCountries(l_countries);

        List<Country> l_mapCountryList = new ArrayList<Country>();
        Country l_countryName1 = new Country(1, "France", 1);
        Country l_countryName2 = new Country(2, "Germany", 2);
        l_countryName2.setD_numberOfArmies(5);

        l_mapCountryList.add(l_countryName1);
        l_mapCountryList.add(l_countryName2);

        Map l_map = new Map();
        l_map.setD_countries(l_mapCountryList);
        d_gameState.setD_map(l_map);

        d_deployOrder1 = new Deploy(d_playerName1, "France", 5);
        d_deployOrder2 = new Deploy(d_playerName2, "Germany", 15);
    }

    /**
     *This testcase is used to check that country given in the deploy command is
     * belong to the player or not.
     */
    @Test
    public void testCheckingOfDeployOrderCountry() {
        boolean l_actualBoolean = d_deployOrder1.isValid(d_gameState);
        assertTrue(l_actualBoolean);
        boolean l_actualBoolean2 = d_deployOrder2.isValid(d_gameState);
        assertTrue(l_actualBoolean2);
    }

    /**
     * This testcase is used to validate that if deploy order and created or not and also validate
     * re-calculation of armies.
     *
     * @throws InvalidCommand if given command is invalid
     */
    @Test
    public void testDeployOrder() throws InvalidCommand {
        Player l_player = new Player("Navjot");
        l_player.setD_noOfAllocatedArmies(10);
        Country l_country = new Country(1, "Inida", 1);
        l_player.setD_ownedCountries(Arrays.asList(l_country));

        l_player.deployOrder("deploy India 6");

        assertEquals(l_player.getD_noOfAllocatedArmies().toString(), "4");
        assertEquals(l_player.getD_executeOrders().size(), 1);
        Deploy l_order = (Deploy) l_player.d_orderedPlayerList.get(0);
        assertEquals("India", l_order.d_targetCountry);
        assertEquals("6", String.valueOf(l_order.d_countOfArmies));
    }

    /**
     * This testcase is use to execute deploy order and validate that if armies are
     * deploy to the target country or not.
     */
    @Test
    public void testExecutionOfDeployOrder() {
        d_deployOrder1.execute(d_gameState);
        Country l_countryIndia = d_gameState.getD_map().getCountryByName("France");
        assertEquals("5", l_countryIndia.getD_numberOfArmies().toString());

        d_deployOrder2.execute(d_gameState);
        Country l_countryCanada = d_gameState.getD_map().getCountryByName("Germany");
        assertEquals("20", l_countryCanada.getD_numberOfArmies().toString());
    }
}
