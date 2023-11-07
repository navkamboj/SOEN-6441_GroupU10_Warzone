package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
/**
 * This class is used to test functionality of Advance order.
 */
public class AdvanceTest {
    /**
     * current game state
     */
    GameState d_gameState = new GameState();

    /**
     * Checks whether advance order given is invalid.
     */
    @Test
    public void testAdvanceOrderValidation() {
        Player l_playerName = new Player("Nihal");
        Country l_countryName1 = new Country("India");
        l_countryName1.setD_numberOfArmies(15);
        Country l_countryName2 = new Country("Nepal");
        l_countryName2.setD_numberOfArmies(5);
        List<Country> l_countrieList = Arrays.asList(l_countryName1, l_countryName2);
        l_playerName.setD_ownedCountries(l_countrieList);

        assertFalse(new Advance(l_playerName, "India", "Pakistan", 16).isValid(d_gameState));
        assertFalse(new Advance(l_playerName, "Nepal", "Srilanka", 5).isValid(d_gameState));
        assertFalse(new Advance(l_playerName, "Srilanka", "China", 5).isValid(d_gameState));
        assertTrue(new Advance(l_playerName, "India", "Afghanistan", 5).isValid(d_gameState));
    }

    /**
     * Checks if first player wins the battle,countries and armies are updated correctly
     * or not.
     */
    @Test
    public void testFirstPlayerWin() {
        Player l_player1 = new Player("Nihal");
        Country l_countryName1 = new Country("India");
        l_countryName1.setD_numberOfArmies(15);
        List<Country> l_countries1 = new ArrayList<>();
        l_countries1.add(l_countryName1);
        l_player1.setD_ownedCountries(l_countries1);

        Player l_player2 = new Player("Yatish");
        Country l_countryName2 = new Country("Nepal");
        l_countryName2.setD_numberOfArmies(5);
        List<Country> l_countries2 = new ArrayList<>();
        l_countries2.add(l_countryName2);
        l_player2.setD_ownedCountries(l_countries2);

        Advance l_advance = new Advance(l_player1, "India", "Nepal", 5);
        l_advance.handleRemainingArmies(5, 0, l_countryName1, l_countryName2, l_player2);

        assertEquals(l_player2.getD_ownedCountries().size(), 0);
        assertEquals(l_player1.getD_ownedCountries().size(), 2);
        assertEquals(l_player1.getD_ownedCountries().get(1).getD_numberOfArmies().toString(), "5");
    }

    /**
     * Checks if deployed armies are deployed to target country or not.
     */
    @Test
    public void testDeployArmiesOnTarget() {
        Player l_playerName1 = new Player("Nihal");
        List<Country> l_countries1 = new ArrayList<>();

        Country l_countryName1 = new Country("India");
        l_countryName1.setD_numberOfArmies(15);
        l_countries1.add(l_countryName1);

        Country l_countryName2 = new Country("Nepal");
        l_countryName2.setD_numberOfArmies(5);
        l_countries1.add(l_countryName2);
        l_playerName1.setD_ownedCountries(l_countries1);

        Advance l_advance = new Advance(l_playerName1, "India", "Nepal", 2);
        l_advance.deployArmiesOnTargetCountry(l_countryName2);
        assertEquals(l_countryName2.getD_numberOfArmies().toString(), "7");
    }

    /**
     * Checks if second player has won the battle,countries and armies are updated correctly
     * or not.
     */
    @Test
    public void testSecondPlayerWin() {
        Player l_player1 = new Player("Nihal");
        Country l_countryName1 = new Country("India");
        l_countryName1.setD_numberOfArmies(5);
        List<Country> l_countries1 = new ArrayList<>();
        l_countries1.add(l_countryName1);
        l_player1.setD_ownedCountries(l_countries1);

        Player l_player2 = new Player("Yatish");
        Country l_countryName2 = new Country("Nepal");
        l_countryName2.setD_numberOfArmies(15);
        List<Country> l_countries2 = new ArrayList<>();
        l_countries2.add(l_countryName2);
        l_player2.setD_ownedCountries(l_countries2);

        Advance l_advance = new Advance(l_player1, "India", "Nepal", 10);
        l_advance.handleRemainingArmies(6,12 , l_countryName1, l_countryName2, l_player2);

        assertEquals(l_player2.getD_ownedCountries().size(), 1);
        assertEquals(l_player1.getD_ownedCountries().size(), 1);
        assertEquals(l_player1.getD_ownedCountries().get(0).getD_numberOfArmies().toString(), "11");
        assertEquals(l_player2.getD_ownedCountries().get(0).getD_numberOfArmies().toString(), "12");
    }


}
