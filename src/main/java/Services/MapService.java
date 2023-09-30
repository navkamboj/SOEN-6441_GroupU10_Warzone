package Services;

import Exceptions.InvalidMap;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public Map mapLoad(GameState p_gameState, String p_mapFileName) {

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
    public void mapModify(GameState p_gameState, String p_editMapPath) throws IOException {

        String l_mapFilePath = CommonUtil.getMapFilePath(p_editMapPath);
        File l_fileToEdit = new File(l_mapFilePath);
        if (l_fileToEdit.createNewFile()) {
            System.out.println("File has been successfully created.");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editMapPath);
            p_gameState.setD_map(l_map);
        } else {
            System.out.println("File already exists.");
            this.mapLoad(p_gameState, p_editMapPath);
            if (null == p_gameState.getD_map()) {
                p_gameState.setD_map(new Map());
            }
            p_gameState.getD_map().setD_mapFile(p_editMapPath);
        }
    }

    /**
     * It processes the revised map into a .map file and saves it to the specified destination.
     *
     * @param p_gameState Current gamestate
     * @param p_mapFileName file name in which map data is stored.
     * @return true/false according to the success/failure of map save operation.
     * @throws InvalidMap Invalid Map exception.
     */
    public boolean mapSave(GameState p_gameState, String p_mapFileName) throws InvalidMap {
        try {
            if (!p_mapFileName.equalsIgnoreCase(p_gameState.getD_map().getD_mapFile())) {
                p_gameState.setD_errorMessage("Please use the same filename for saving that was provided when making edits.");
                return false;
            }
            else {
                if (null != p_gameState.getD_map()) {
                    Models.Map l_currentMap = p_gameState.getD_map();
                    System.out.println("Validating Map...");
                    boolean l_validationStatus = l_currentMap.Validate();
                    if (l_validationStatus) {
                        Files.deleteIfExists(Paths.get(CommonUtil.getMapFilePath(p_mapFileName)));
                        FileWriter l_fileWriter = new FileWriter(CommonUtil.getMapFilePath(p_mapFileName));

                        if (null != p_gameState.getD_map().getD_continents()
                                && !p_gameState.getD_map().getD_continents().isEmpty()) {
//                            writeContinentMetadata(p_gameState, l_fileWriter);
                        }
                        if (null != p_gameState.getD_map().getD_countries()
                                && !p_gameState.getD_map().getD_countries().isEmpty()) {
//                            writeCountryAndBoarderMetaData(p_gameState, l_fileWriter);
                        }
                        l_fileWriter.close();
                    }
                } else {
                    p_gameState.setD_errorMessage("Validation Failed");
                    return false;
                }
            }
            return true;
        } catch (IOException l_ioException) {
            l_ioException.printStackTrace();
            p_gameState.setD_errorMessage("Error in saving map file");
            return false;
        }
    }

    /**
     * Resets Game State Map.
     *
     * @param p_gameState object of GameState.
     */
    public void mapReset(GameState p_gameState) {
        System.out.println("Map cannot be loaded since it is invalid. Please provide a valid map");
        p_gameState.setD_map(new Models.Map());
    }

    /**
     * Method to modify Map based on the operation type ( add/remove) and arguments
     * @param p_mapToBeModified Map under modification
     * @param p_operationType Add/Remove operation
     * @param p_argument Arguments
     * @return Map object with updated continents
     * @throws InvalidMap exception
     */
    public Map addOrRemoveContinents(Map p_mapToBeModified, String p_operationType,
                                     String p_argument) throws InvalidMap {

        if (p_operationType.equalsIgnoreCase("add") && p_argument.split(" ").length==2) {
            p_mapToBeModified.addContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
        } else if (p_operationType.equalsIgnoreCase("remove") && p_argument.split(" ").length==1) {
            p_mapToBeModified.removeContinent(p_argument.split(" ")[0]);
        } else {
            System.out.println("Couldn't "+p_operationType+" the continent");
        }

        return p_mapToBeModified;
    }
    /**
     * Method for modifying a chosen map by adding or removing continents using commands within the editmap function
     * @param p_gameState Gamestate model class object
     * @param p_argument Arguments provided for the command operation
     * @param p_operationType Add/remove type operation
     * @throws IOException given when the filename is not valid / it doesn't exist.
     * @throws InvalidMap Exception
     */
    public void modifyContinent(GameState p_gameState, String p_argument, String p_operationType) throws IOException, InvalidMap {
        String l_mapName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeModified = (CommonUtil.isNull(p_gameState.getD_map().getD_continents())
                && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.mapLoad(p_gameState, l_mapName)
                : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeModified)) {
            Map l_modifiedMap = addOrRemoveContinents(l_mapToBeModified, p_operationType, p_argument);
            p_gameState.setD_map(l_modifiedMap);
            p_gameState.getD_map().setD_mapFile(l_mapName);
        }
    }

    /**
     * Method to add/remove countries in the map
     * @param p_mapToBeModified Map to be modified
     * @param p_operationType Add/Remove type operation
     * @param p_argument Arguments provided for the command operation
     * @return Modified map object
     * @throws InvalidMap
     */
    public Map addOrRemoveCountry(Map p_mapToBeModified, String p_operationType, String p_argument) throws InvalidMap{
        if (p_operationType.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
            p_mapToBeModified.addCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        }else if(p_operationType.equalsIgnoreCase("remove")&& p_argument.split(" ").length==1){
            p_mapToBeModified.removeCountry(p_argument.split(" ")[0]);
        }else{
            System.out.println("Changes can't be saved !!");
        }
        return p_mapToBeModified;
    }

    /**
     * Method to control modify country command flow i.e. to Add/Remove a country
     * @param p_gameState Gamestate model class object
     * @param p_operation Add/Remove operation
     * @param p_argument Arguments provided for the command operation
     * @throws InvalidMap exception
     */
    public void modifyCountry(GameState p_gameState, String p_operation, String p_argument) throws InvalidMap{
        String l_mapName= p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeModified = (CommonUtil.isNull(p_gameState.getD_map().getD_continents())
                && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.mapLoad(p_gameState, l_mapName)
                : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeModified)) {
            Map l_modifiedMap = addOrRemoveCountry(l_mapToBeModified, p_operation, p_argument);
            p_gameState.setD_map(l_modifiedMap);
            p_gameState.getD_map().setD_mapFile(l_mapName);
        }
    }

    /**
     * Method to add/remove neighbor on Map object
     * @param p_mapToBeModified Map object to be modified
     * @param p_operationType Add/remove operation
     * @param p_argument Arguments provided for the command operation
     * @return modified map object
     * @throws InvalidMap exception
     */
    public Map addOrRemoveNeighbor(Map p_mapToBeModified, String p_operationType, String p_argument) throws InvalidMap{
        if (p_operationType.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
            p_mapToBeModified.addCountryNeighbor(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        }else if(p_operationType.equalsIgnoreCase("remove") && p_argument.split(" ").length==2){
            p_mapToBeModified.removeCountryNeighbor(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        }else{
            System.out.println("Changes can't be saved !!");
        }
        return p_mapToBeModified;
    }

    /**
     * Method for modifying a chosen map by adding or removing neighbors using commands
     * @param p_gameState Gamestate object
     * @param p_operation Add/Remove operation type
     * @param p_argument Arguments provided for the command operation
     * @throws InvalidMap exception
     */
    public void modifyNeighbor(GameState p_gameState, String p_operation, String p_argument) throws InvalidMap{
        String l_mapFileName= p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents())
                && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.mapLoad(p_gameState, l_mapFileName)
                : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeUpdated)) {
            Map l_updatedMap = addOrRemoveNeighbor(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }

}
