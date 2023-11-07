package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import Exceptions.InvalidMap;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is used to test the functionalities of Airlift class methods.
 */
public class BlockadeTest {
    /**
     * Player who executes the command
     */
    Player d_playerName1;

    /**
     * Second Player.
     */
    Player d_playerName2;

    /**
     * Neutral Player.
     */
    Player d_neutralPlayerName;

    /**
     * Blockade Order 1.
     */
    Blockade d_blockade1;

    /**
     * Blockade Order 2.
     */
    Blockade d_blockade2;

    /**
     * Blockade Order 3.
     */
    Blockade d_blockade3;

    /**
     * Target Country.
     */
    String d_targetCountryName;

    /**
     * Order List.
     */
    List<Order> d_orders;

    /**
     * Game State.
     */
    GameState d_gameState;

    /**
     *
     * This method blockadeSetup called every time before the execution of every test case that
     * are available in this BlockadeTest class file
     */
    @Before
    public void blockadeSetup(){
       d_gameState=new GameState();
       d_orders=new ArrayList<Order>();
       d_playerName1=new Player();
       d_playerName1.setD_playerName("Nihal");
       d_neutralPlayerName=new Player();
       d_neutralPlayerName.setD_playerName("neutral");

       List<Country> l_countries = new ArrayList<Country>();
       Country l_country1=new Country(1,"South Korea",1);
       Country l_country2=new Country(2,"Australia",2);
       l_country1.setD_numberOfArmies(6);
       l_country2.setD_numberOfArmies(8);

       l_countries.add(l_country1);
       l_countries.add(l_country2);

       d_playerName1.setD_ownedCountries(l_countries);
       d_neutralPlayerName.setD_ownedCountries(l_countries);
       Map l_map = new Map();
       l_map.setD_countries(l_countries);
       d_gameState.setD_map(l_map);

       List<Player> l_players = new ArrayList<Player>();
       l_players.add(d_neutralPlayerName);
       d_gameState.setD_playerList(l_players);

       d_blockade1 = new Blockade(d_playerName1,"South Korea");
       d_blockade2 = new Blockade(d_playerName1,"North Korea");

       d_orders.add(d_blockade1);
       d_orders.add(d_blockade2);
    }

    /**
     * This test method is used to test that execution of blockade order is correct or not.
     */
    @Test
    public void testExecutionOfBlockade(){
         d_blockade1.execute(d_gameState);
         Country l_countryAfterBlockade = d_gameState.getD_map().getCountryByName("South Korea");
         assertEquals("18",l_countryAfterBlockade.getD_numberOfArmies().toString());
    }

    /**
     *This test method is used to test validation of blockade command.
     */
    @Test
    public void testBlockadeValidation(){
        boolean l_actualanswer1=d_blockade1.validateOrder(d_gameState);
        boolean l_actualanswer2=d_blockade2.validateOrder(d_gameState);

        assertTrue(l_actualanswer1);
        assertFalse(l_actualanswer2);
    }

}
