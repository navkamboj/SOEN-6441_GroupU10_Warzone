package Services;
import java.io.FileWriter;
import java.io.IOException;
import Models.GameState;

/**
 * This class act as an Adapter class for writing to conquest map file.
 * @author Harsh Tank
 * @version 3.0.0
 */
public class MapWriterAdaptor extends MapFileWriter{
    private ConquestMapFileWriter l_conquestMapFileWriter;

    /**
     * This is a constructor for initializing the conquest map file Writer.
     *
     * @param p_conquestMapFileWriter conquest map file Writer
     */
    public MapWriterAdaptor(ConquestMapFileWriter p_conquestMapFileWriter) {
        this.l_conquestMapFileWriter = p_conquestMapFileWriter;
    }

    /**
     * This method acts as an Adapter for writing to different type of map file through adaptee.
     *
     * @param p_gameState current state of the game
     * @param l_writer file writer
     * @param l_formatOfMap format in which map file has to be saved
     * @throws IOException Io exception
     */
    public void parseMapToFile(GameState p_gameState, FileWriter l_writer, String l_formatOfMap) throws IOException {
        l_conquestMapFileWriter.parseMapToFile(p_gameState, l_writer, l_formatOfMap);
    }
}
