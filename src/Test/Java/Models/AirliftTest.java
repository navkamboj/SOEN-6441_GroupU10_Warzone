package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;

/**
 * This class is used to test the functionalities of Airlift class methods.
 */
public class AirliftTest {
    /**
     * Player who executes the command
     */
    Player d_playerName;

    /**
     * variable that holds Airlift Order
     */
    Airlift d_orderOfAirlift;

    /**
     * country from player want to shift armies to another country
     */
    Airlift d_checkAirliftValidation;

    /**
     * current game state
     */
    GameState d_gameState;

    /**
     *
     * This method airliftSetup called every time before the execution of every test case that
     * are available in this AirliftTest class file
     * @throws InvalidMap Invalid Map
     */
    @Before
    public void airliftSetup() throws InvalidMap{
         d_gameState=new GameState();
         d_playerName =new Player();
         d_playerName.setD_playerName("Nihal");

         List<Country> l_listOfCountries = new ArrayList<Country>();
         Country l_newCountry =new Country(0,"India",1);
         l_newCountry.setD_numberOfArmies(20);
         l_listOfCountries.add(l_newCountry);

         Country l_neighbourCountry =new Country(1,"Nepal",1);
         l_neighbourCountry.addingNeighbor(0);
         l_newCountry.addingNeighbor(1);
         l_neighbourCountry.setD_numberOfArmies(7);
         l_listOfCountries.add(l_neighbourCountry);

         Map l_map = new Map();
         l_map.setD_countries(l_listOfCountries);

         d_gameState.setD_map(l_map);
         d_playerName.setD_ownedCountries(l_listOfCountries);
         d_orderOfAirlift = new Airlift("India","Nepal",1,d_playerName);
    }

    /**
     * This test method is used to test that execution of airlift order is correct or not.
     */
    @Test
    public void testExecutionOfAirlift(){
         d_orderOfAirlift.execute(d_gameState);
         Country l_countryAfterAirlift =d_gameState.getD_map().getCountryByName("Nepal");
         assertEquals("8",l_countryAfterAirlift.getD_numberOfArmies().toString());
    }

    /**
     *This test method is used to test validation of airlift command.
     */
    @Test
    public void testAirliftValidation(){
         d_checkAirliftValidation= new Airlift("India","China",1,d_playerName);
         boolean actual=d_checkAirliftValidation.validateOrder(d_gameState);
         assertFalse(actual);
    }
}
