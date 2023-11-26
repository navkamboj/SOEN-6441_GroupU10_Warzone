package Services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;
import Exceptions.InvalidCommand;

import Models.GameState;
import Models.Map;

/**
 * This class is used to test the parsing conquest map file.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class ConquestMapFileReaderTest {
    /**
     * Object of mapService class.
     */
    MapService d_mapservice;

    /**
     *List of strings.
     */
    List<String> d_mapLineList;

    /**
     * Object of map class.
     */
    Map d_map;

    /**
     * current Game State
     */
    GameState d_gameState;

    /**
     * Conquest file reader for parsing the map file.
     */
    ConquestMapFileReader d_objectOfConquestMapFileReader;

    /**
     * Setup For testing before each map service operations.
     *
     * @throws InvalidMap Invalid map exception
     */
    @Before
    public void setup() throws InvalidMap {
        d_objectOfConquestMapFileReader = new ConquestMapFileReader();
        d_mapservice = new MapService();
        d_map = new Map();
        d_gameState= new GameState();
        d_mapLineList = d_mapservice.mapFile("testconquest.map");
    }

    /**
     * This test is used to validate the functionality of reading conquest map.
     *
     * @throws IOException throws IOException
     * @throws InvalidMap Invalid map exception
     */
    @Test
    public void testReadConquestFile() throws IOException, InvalidMap {
        d_objectOfConquestMapFileReader.scanConquestFile(d_gameState, d_map, d_mapLineList);

        assertNotNull(d_gameState.getD_map());
        assertEquals(d_gameState.getD_map().getD_continents().size(), 8);
        assertEquals(d_gameState.getD_map().getD_countries().size(), 99);
    }

    /**
     * tests addition or deletion of continent via editcontinent operation
     * @throws IOException Exceptions
     * @throws InvalidMap Exception
     * @throws InvalidCommand Exception
     */
    @Test
    public void testMapUpdation() throws IOException, InvalidMap, InvalidCommand {
        d_objectOfConquestMapFileReader.scanConquestFile(d_gameState, d_map, d_mapLineList);
        Map l_updatedContinentList;
        l_updatedContinentList=d_mapservice.addOrRemoveContinents(d_gameState, d_gameState.getD_map(), "Add", "Africa 20");

        assertEquals(l_updatedContinentList.getD_continents().size(), 9);
        assertEquals(l_updatedContinentList.getD_continents().get(8).getD_continentName(), "Africa");
        assertEquals(l_updatedContinentList.getD_continents().get(8).getD_continentValue().toString(), "20");

        l_updatedContinentList = d_mapservice.addOrRemoveCountry(d_gameState, d_gameState.getD_map(), "Remove", "Africa");
        assertEquals(l_updatedContinentList.getD_continents().size(), 8);

        l_updatedContinentList=d_mapservice.addOrRemoveContinents(d_gameState, d_gameState.getD_map(), "Add", "Africa 20");
        assertEquals(l_updatedContinentList.getD_continents().size(), 9);

        d_mapservice.editFunctionality(d_gameState, "add", "Namibia Africa", 5);
        d_mapservice.editFunctionality(d_gameState, "add", "Egypt Africa", 4);
        assertEquals(d_gameState.getD_map().getD_countries().size(), 101);

        d_mapservice.editFunctionality(d_gameState, "remove", "Namibia", 5);
        assertEquals(d_gameState.getD_map().getD_countries().size(), 100);
    }
}
