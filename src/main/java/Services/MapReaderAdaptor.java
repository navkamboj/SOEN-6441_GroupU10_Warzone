package Services;
import java.util.*;
import Models.Map;
import Models.GameState;

/**
 * This class acts as Adapter class for reading conquest map file.
 * @author Harsh Tank
 * @version 3.0.0
 */

public class MapReaderAdaptor extends MapFileReader {

    private ConquestMapFileReader l_conquestMapFileReader;

    /**
     * This is constructor method for initializing the conquest map file reader.
     *
     * @param p_conquestMapFileReader conquest map file reader
     */
    public MapReaderAdaptor(ConquestMapFileReader p_conquestMapFileReader) {
        this.l_conquestMapFileReader = p_conquestMapFileReader;
    }

    /**
     * This method act as Adapter for reading different type of map file with the help of adaptee.
     *
     * @param p_gameState current state of the game
     * @param p_map map to be set
     * @param p_fileLines lines of loaded file
     */
    public void parseMapFile(GameState p_gameState, Map p_map, List<String> p_fileLines) {
        l_conquestMapFileReader.scanConquestFile(p_gameState, p_map, p_fileLines);
    }

}
