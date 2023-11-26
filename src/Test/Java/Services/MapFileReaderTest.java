package Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;
import Models.GameState;
import Models.Map;

/**
 * This class is used to test parsing map file to map ofgame state
 * @version 3.0.0
 * @author Nihal Galani
 */
public class MapFileReaderTest {
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
     * File reader object for parsing the map file.
     */
    MapFileReader d_mapFileReaderObject;

    /**
     * Setup For testing before each map service operations.
     *
     * @throws InvalidMap Invalid map exception
     */
    @Before
    public void setup() throws InvalidMap {
        d_mapFileReaderObject = new MapFileReader();
        d_mapservice = new MapService();
        d_map = new Map();
        d_gameState = new GameState();
        d_mapLineList = d_mapservice.mapFile("canada.map");
    }

    /**
     * This test case tests reading of conquest file
     *
     * @throws IOException throws IOException
     * @throws InvalidMap Invalid map exception
     */
    @Test
    public void testReadMapFile() throws IOException, InvalidMap {
        d_mapFileReaderObject.mapFileParse(d_gameState, d_map, d_mapLineList);

        assertNotNull(d_gameState.getD_map());
        assertEquals(d_gameState.getD_map().getD_continents().size(), 6);
        assertEquals(d_gameState.getD_map().getD_countries().size(), 31);
    }

}