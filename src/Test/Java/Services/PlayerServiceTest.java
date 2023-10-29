package Services;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Models.Player;
import Utils.CommonUtil;

import static org.junit.Assert.*;

/**
 * This playerServiceTest class file is used to test some important functionalities of playerservice file
 *
 * @version 1.0.0
 * @author Nihal Galani
 */

public class PlayerServiceTest {
    /**
     * Reference variable for PlayerService class
     */
    PlayerService d_testPlayerService;

    /**
     *Reference variable for Map class
     */
    Map d_testMap;

    /**
     * Reference variable for Player class
     */
    Player d_testInformationAboutPlayer;

    /**
     *Reference variable for MapService class
     */
    MapService d_testMapService;

    /**
     *Reference variable for GameState class
     */
    GameState d_testGameState;

    /**
     * Storing existing players list
     */
    List<Player> d_currentlyAvailablePlayersList = new ArrayList<>();

    private final ByteArrayOutputStream d_outputStreamContent = new ByteArrayOutputStream();

    /**
     * This method playerSetup called every time before the execution of every test case that
     * are availble in this PlayerServiceTest class file
     */
    @Before
    public void playerSetup(){
         d_testInformationAboutPlayer = new Player();
         d_testPlayerService = new PlayerService();
         d_testGameState = new GameState();
         d_currentlyAvailablePlayersList.add(new Player("Nihal"));
         d_currentlyAvailablePlayersList.add(new Player("Pranjlesh"));
    }

    /**
     * This testing method is used to calculate total number ofreinforcemet armies for the player
     */
    @Test
    public void testArmiesCountForPlayer(){
        Player l_informationOfPlayer = new Player();
        List<Country> l_countries = new ArrayList<Country>();

        l_countries.add(new Country("Montreal"));
        l_countries.add(new Country("Mumbai"));
        l_countries.add(new Country("Ahmedabad"));
        l_countries.add(new Country("New York"));
        l_countries.add(new Country("Seoul"));
        l_countries.add(new Country("Busan"));
        l_informationOfPlayer.setD_ownedCountries(l_countries);

        List<Continent> l_continents = new ArrayList<Continent>();
        l_continents.add(new Continent(7, "Europe", 4));

        l_informationOfPlayer.setD_ownedContinents(l_continents);

        Integer l_countedResult = d_testPlayerService.armiesCountForPlayer(l_informationOfPlayer);
        Integer l_expectedCountResult = 7;
        assertEquals(l_expectedCountResult,l_countedResult);
    }

    /**
     * This testing method is used to test that player can not deploy number of
     * armies  than player have in their balance
     */
    @Test
    public void testDeployOrderValidation(){
        d_testInformationAboutPlayer.setD_noOfAllocatedArmies(8);
        String l_totalArmies ="5";
        boolean l_booleancheck = d_testPlayerService.deployOrderValidation(d_testInformationAboutPlayer,l_totalArmies);
        assertFalse(l_booleancheck);
    }

    /**
     *This testing method is used to test that adding player functionalities
     */
    @Test
    public void testAddingPlayers(){
     assertFalse(CommonUtil.isEmptyCollection(d_currentlyAvailablePlayersList));
     List<Player> l_updatingPlayers = d_testPlayerService.addingRemovingPlayers(d_currentlyAvailablePlayersList,"add","Yatish");
     assertEquals("Yatish",l_updatingPlayers.get(2).getD_playerName());

     System.setOut(new PrintStream(d_outputStreamContent));
     d_testPlayerService.addingRemovingPlayers(d_currentlyAvailablePlayersList,"add","Nihal");
     assertEquals("Player: Nihal already Exists." ,d_outputStreamContent.toString() );

    }

    /**
     *This testing method is used to test that player removal is done or not of addingRemovingOlayers
     */
    @Test
    public void testRemovalPlayers(){
    List<Player> l_updatingPlayers = d_testPlayerService.addingRemovingPlayers(d_currentlyAvailablePlayersList,"remove","Pranjlesh");
    assertEquals(1, l_updatingPlayers.size());

    System.setOut(new PrintStream(d_outputStreamContent));
    d_testPlayerService.addingRemovingPlayers(d_currentlyAvailablePlayersList,"remove","Harsh");
    assertEquals("Player: Harsh does not Exist." ,d_outputStreamContent.toString() );
    }

    /**
     *This testing method is used to test that is player added or not
     */
    @Test
    public void testChecksAvailabilityPlayers(){
      boolean l_existanceOfPlayers = d_testPlayerService.checkAvailabilityPlayerList(d_testGameState);
      assertFalse(l_existanceOfPlayers);
    }


}