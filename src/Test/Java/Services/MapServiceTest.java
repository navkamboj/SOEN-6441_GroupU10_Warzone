package Services;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;

import Models.GameState;
import Models.Map;
import Utils.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * This MapServiceTest class file is used to test some important functionalities of MapService file
 *
 * @version 2.0.0
 * @author Nihal Galani
 */

public class MapServiceTest {
    /**
     * Object of MapService class
     */
    MapService d_mapService;

    /**
     * Reference object of Map file
     */
    Map d_map;

    /**
     * current Game State
     */
    GameState d_gamestate;

    /**
     *  This method mapServiceSetup called every time before the execution of every test case that
     *  are available in this MapServiceTest class file
     */
    @Before
    public void mapServiceSetup() throws InvalidMap {
        d_mapService = new MapService();
        d_map = new Map();
        d_gamestate = new GameState();
        d_map = d_mapService.mapLoad(d_gamestate, "canada.map");
    }

    /**
     * Testing the functionality of editMap.
     *
     * @throws IOException throws IOException
     */
    @Test
    public void testModifyMap() throws IOException, InvalidMap {
        d_mapService.mapModify(d_gamestate, "test.map");
        File l_fileName = new File(CommonUtil.getMapFilePath("test.map"));

        assertTrue(l_fileName.exists());
    }

    /**
     * testing addition of continent
     * @throws IOException Exceptions
     * @throws InvalidMap Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testContinentAddition() throws IOException, InvalidMap, InvalidCommand {
        d_gamestate.setD_map(new Map());
        Map l_updatedContinents = d_mapService.addOrRemoveContinents(d_gamestate, d_gamestate.getD_map(), "Add", "Asia 5");

        assertEquals(l_updatedContinents.getD_continents().size(), 1);
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentName(), "Asia");
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentValue().toString(), "5");
    }

    /**
     * Testing addition of country
     * @throws IOException Exception
     * @throws InvalidMap Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testCountryAddition() throws IOException, InvalidMap, InvalidCommand {
        d_mapService.mapLoad(d_gamestate, "test.map");
        d_mapService.editFunctionality(d_gamestate, "Asia 10", "add", 1 );
        d_mapService.editFunctionality(d_gamestate, "India Asia", "add", 2);

        assertEquals(d_gamestate.getD_map().getCountryByName("India").getD_countryName(), "India");
    }

    /**
     * Testing removal of country
     * @throws InvalidMap Exception
     * @throws InvalidCommand Exception
     * @throws IOException handles input output exception
     */
    @Test
    public void testRemovalOfCountry() throws InvalidMap, IOException, InvalidCommand {
        d_mapService.mapLoad(d_gamestate, "test.map");
        d_mapService.editFunctionality(d_gamestate, "Pakistan", "remove", 2);
        assertEquals("Country: Pakistan does not exist"+System.lineSeparator(),d_gamestate.getLatestLog() );
    }

    /**
     * Tests the add neighbor operation via editneighbor
     * @throws InvalidMap Exception
     * @throws IOException Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testAdditionOfNeighbor() throws InvalidMap, IOException, InvalidCommand {
        d_mapService.mapLoad(d_gamestate, "test.map");
        d_mapService.editFunctionality(d_gamestate, "Asia 10", "add", 1 );
        d_mapService.editFunctionality(d_gamestate, "India Asia", "add",  2);
        d_mapService.editFunctionality(d_gamestate, "Nepal Asia","add", 2);
        d_mapService.editFunctionality(d_gamestate, "India Nepal", "add", 3);

        assertEquals(d_gamestate.getD_map().getCountryByName("India").getD_neighborCountryIDs().get(0), d_gamestate.getD_map().getCountryByName("Nepal").getD_countryID());
    }

    /**
     *Tests the remove neighbor operation via editneighbor
     * @throws InvalidMap Exception
     * @throws IOException Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testRemovalOfNeighbor() throws InvalidMap, IOException, InvalidCommand{
        d_mapService.mapModify(d_gamestate, "test.map");
        d_mapService.editFunctionality(d_gamestate, "Asia 15", "add",   1);
        d_mapService.editFunctionality(d_gamestate, "India Asia", "add", 2);
        d_mapService.editFunctionality(d_gamestate, "Nepal Asia", "add", 2);
        d_mapService.editFunctionality(d_gamestate, "India Nepal", "add", 3);
        d_mapService.editFunctionality(d_gamestate, "Nepal India", "remove", 3);
        assertEquals("No Such Neighbour Exists"+System.lineSeparator(), d_gamestate.getLatestLog());
    }
}
