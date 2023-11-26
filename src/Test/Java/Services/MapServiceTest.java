package Services;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * This MapServiceTest class file is used to test some important functionalities of MapService file
 *
 * @version 3.0.0
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
     * Tests the add neighbor operation via editneighbor
     * @throws InvalidMap Exception
     * @throws IOException Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testAdditionOfNeighbor() throws InvalidMap, IOException, InvalidCommand {
        d_mapService.mapLoad(d_gamestate, "testing.map");
        d_mapService.editFunctionality(d_gamestate, "Asia 10", "add", 1 );
        d_mapService.editFunctionality(d_gamestate, "India Asia", "add",  2);
        d_mapService.editFunctionality(d_gamestate, "Nepal Asia","add", 2);
        d_mapService.editFunctionality(d_gamestate, "India Nepal", "add", 3);

        assertEquals(d_gamestate.getD_map().getCountryByName("India").getD_neighborCountryIDs().get(0), d_gamestate.getD_map().getCountryByName("Nepal").getD_countryID());
    }

    /**
     * Testing removal of country
     * @throws InvalidMap Exception
     * @throws InvalidCommand Exception
     * @throws IOException handles input output exception
     */
    @Test
    public void testRemovalOfCountry() throws InvalidMap, IOException, InvalidCommand {
        d_mapService.mapLoad(d_gamestate, "testing.map");
        d_mapService.editFunctionality(d_gamestate, "Pakistan", "remove", 2);
        assertEquals("Log : Country: Pakistan does not exist"+System.lineSeparator(),d_gamestate.getLatestLog() );
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
        assertEquals("Log : No Such Neighbour Exists"+System.lineSeparator(), d_gamestate.getLatestLog());
    }

    /**
     * This test case is used to validate countryId and their neighborId.
     */
    @Test
    public void testIdOfTheCountryAndNeighbor() {
        List<Integer> l_actualCountryIds = new ArrayList<Integer>();
        LinkedHashMap<Integer, List<Integer>> l_actualCountryNeighborList = new LinkedHashMap<Integer, List<Integer>>();

        List<Integer> l_expectedCountryIds = new ArrayList<Integer>();
        l_expectedCountryIds.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31));

        int counter=0;
        LinkedHashMap<Integer, List<Integer>> l_expectedCountryNeighborList = new LinkedHashMap<Integer, List<Integer>>() {
            {
                put(1, new ArrayList<Integer>(Arrays.asList(8, 2, 3)));
                put(2, new ArrayList<Integer>(Arrays.asList(1, 3)));
                put(3, new ArrayList<Integer>(Arrays.asList(1, 2, 4)));
                put(4, new ArrayList<Integer>(Arrays.asList(3, 5, 7)));
                put(5, new ArrayList<Integer>(Arrays.asList(4, 7, 6)));
            }
        };

        for (Country l_country : d_map.getD_countries()) {
            l_actualCountryIds.add(l_country.getD_countryID());
        }

        for(Country l_country : d_map.getD_countries()){
            ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
            l_neighbours.addAll(l_country.getD_neighborCountryIDs());
            l_actualCountryNeighborList.put(l_country.getD_countryID(), l_neighbours);
            counter++;
            if(counter==5)
                break;
        }

        assertEquals(l_expectedCountryIds, l_actualCountryIds);
        assertEquals(l_expectedCountryNeighborList, l_actualCountryNeighborList);
    }

    /**
     *This test case is used to validate continentId and their values.
     */
    @Test
    public void testIdOfContinentAndTheirValues() {
        List<Integer> l_actualContinentIds = new ArrayList<Integer>();
        List<Integer> l_actualContinentValues = new ArrayList<Integer>();

        List<Integer> l_expectedContinentValues = new ArrayList<Integer>();
        l_expectedContinentValues.addAll(Arrays.asList(3,4,3,2,3,2));

        List<Integer> l_expectedContinentIds = new ArrayList<Integer>();
        l_expectedContinentIds.addAll(Arrays.asList(1, 2, 3, 4,5,6));

        for (Continent l_continent : d_map.getD_continents()) {
            l_actualContinentIds.add(l_continent.getD_continentID());
            l_actualContinentValues.add(l_continent.getD_continentValue());
        }

        assertEquals(l_expectedContinentIds, l_actualContinentIds);
        assertEquals(l_expectedContinentValues, l_actualContinentValues);
    }

    /**
     * Tests the savemap operation on an Invalid Map
     *
     *  @throws InvalidMap Exception
     */
    @Test
    public void testSaveInvalidMap() throws InvalidMap {
        Map l_temp=new Map();
        l_temp.setD_mapFile("testing.map");
        d_gamestate.setD_map(l_temp);
        d_mapService.mapSave(d_gamestate, "testing.map");
        assertEquals("Log : Couldn't save the changes in map file!"+System.lineSeparator(), d_gamestate.getLatestLog());
    }

    /**
     * This test case is used to remove continent from the list.
     *
     * @throws IOException Exceptions
     * @throws InvalidMap Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testContinentRemoveFromList() throws IOException, InvalidMap, InvalidCommand {
        List<Continent> l_continentList = new ArrayList<>();
        Continent l_continent1 = new Continent();
        l_continent1.setD_continentID(1);
        l_continent1.setD_continentName("Africa");
        l_continent1.setD_continentValue(5);

        Continent l_continent2 = new Continent();
        l_continent2.setD_continentID(2);
        l_continent2.setD_continentName("North America");
        l_continent2.setD_continentValue(10);

        l_continentList.add(l_continent1);
        l_continentList.add(l_continent2);

        Map l_map = new Map();
        l_map.setD_continents(l_continentList);
        d_gamestate.setD_map(l_map);
        Map l_updatedContinents = d_mapService.addOrRemoveContinents(d_gamestate, d_gamestate.getD_map(), "Remove", "Africa");

        assertEquals(l_updatedContinents.getD_continents().size(), 1);
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentName(), "North America");
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentValue().toString(), "10");
    }
}
