package Services;

import Exceptions.InvalidMap;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The MapService class is used to load, read, parse, edit and the save map file.
 */
public class MapService {

    /**
     * This method processes the map file.
     *
     * @param p_gameState this is the current state of the game
     * @param p_mapFileName this is the map file name
     * @return Map object after processing the map file.
     */
    public Map loadMap(GameState p_gameState, String p_mapFileName) {

        Map l_map = new Map();
        List<String> l_linesOfFile = mapFile(p_mapFileName);
        if (null != l_linesOfFile && !l_linesOfFile.isEmpty()) {
            // Parses the file and stores information in objects
        }
        return l_map;
    }

    /**
     * This method loads and reads the map file.
     *
     * @param p_mapFileName Name of the map file
     * @return All the lines in List from the map file.
     */
    public List<String> mapFile(String p_mapFileName) {

        String l_mapFilePath = CommonUtil.getMapFilePath(p_mapFileName);
        List<String> l_lineList = new ArrayList<>();
        try {
            Scanner l_scannerObject = new Scanner(new FileReader(l_mapFilePath));
            while(l_scannerObject.hasNextLine()) {
                String line = l_scannerObject.nextLine();
                l_lineList.add(line);
            }
            l_scannerObject.close();
        } catch (IOException l_ioException) {
            System.out.println("File not Found!");
        }
        return l_lineList;
    }

    /**
     * The method is tasked with generating a new map in case the map to be edited does not exist.
     * If the map already exists, it processes the map file into a game state object.
     *
     * @param p_gameState Current state of the game.
     * @param p_editMapPath contains the filepath.
     * @throws IOException
     */
    public void editMap(GameState p_gameState, String p_editMapPath) throws IOException {

        String l_mapFilePath = CommonUtil.getMapFilePath(p_editMapPath);
        File l_fileToEdit = new File(l_mapFilePath);
        if (l_fileToEdit.createNewFile()) {
            System.out.println("File has been successfully created.");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editMapPath);
            p_gameState.setD_map(l_map);
        } else {
            System.out.println("File already exists.");
            this.loadMap(p_gameState, p_editMapPath);
            if (null == p_gameState.getD_map()) {
                p_gameState.setD_map(new Map());
            }
            p_gameState.getD_map().setD_mapFile(p_editMapPath);
        }
    }
}
