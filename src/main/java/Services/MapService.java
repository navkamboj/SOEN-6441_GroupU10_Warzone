package Services;

import Constants.GameConstants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The MapService class is used to load, read, parse, edit and the save map file.
 *
 * @version 1.0.0
 * @author Harsh Tank, Navjot Kamboj, Pranjalesh Ghansiyal, Yatish Chutani
 */
public class MapService implements Serializable {

    /**
     * This method processes the map file.
     *
     * @param p_gameState this is the current state of the game
     * @param p_mapFileName this is the map file name
     * @return Map object after processing the map file.
     * @throws InvalidMap shows that Map Validation has been failure
     */
    public Map mapLoad(GameState p_gameState, String p_mapFileName) throws InvalidMap {

        Map l_map = new Map();
        List<String> l_fileLines = mapFile(p_mapFileName);

        if (null != l_fileLines && !l_fileLines.isEmpty()) {
            if(l_fileLines.contains("[Territories]")) {
                MapReaderAdaptor l_mapReaderAdapter = new MapReaderAdaptor(new ConquestMapFileReader());
                l_mapReaderAdapter.parseMapFile(p_gameState, l_map, l_fileLines);
            } else if(l_fileLines.contains("[countries]")) {
                new MapFileReader().mapFileParse(p_gameState, l_map, l_fileLines);
            }
        }
        return l_map;
    }

    /**
     * This method loads and reads the map file.
     *
     * @param p_mapFileName Name of the map file
     * @return All the lines in List from the map file.
     * @throws InvalidMap shows Map Validation has been failed
     */
    public List<String> mapFile(String p_mapFileName) throws InvalidMap{

        String l_filePath = CommonUtil.getMapFilePath(p_mapFileName);
        List<String> l_listOfLines = new ArrayList<>();

        BufferedReader l_reader;
        try {
            l_reader = new BufferedReader(new FileReader(l_filePath));
            l_listOfLines = l_reader.lines().collect(Collectors.toList());
            l_reader.close();
        } catch (IOException l_e1) {
            throw new InvalidMap("Map File not Found!");
        }
        return l_listOfLines;
    }

    /**
     * The method is tasked with generating a new map in case the map to be edited does not exist.
     * If the map already exists, it processes the map file into a game state object.
     *
     * @param p_gameState Current state of the game.
     * @param p_editMapPath contains the filepath.
     * @throws InvalidMap shows Map Validation has been failed
     * @throws IOException
     */
    public void mapModify(GameState p_gameState, String p_editMapPath) throws IOException,InvalidMap {

        String l_mapFilePath = CommonUtil.getMapFilePath(p_editMapPath);
        File l_fileToBeEdited = new File(l_mapFilePath);

        if (l_fileToBeEdited.createNewFile()) {
            System.out.println("The file has been created.");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editMapPath);
            p_gameState.setD_map(l_map);
            p_gameState.logUpdate(p_editMapPath+ " File has been generated for user to edit", "effect");
        } else {
            System.out.println("This file already exists.");
            this.mapLoad(p_gameState, p_editMapPath);
            if (null == p_gameState.getD_map()) {
                p_gameState.setD_map(new Map());
            }
            p_gameState.getD_map().setD_mapFile(p_editMapPath);
            p_gameState.logUpdate(p_editMapPath+ " already exists and is loaded for editing", "effect");
        }
    }

    /**
     * This method converts the Map Object to File.
     *
     * @param p_gameState current gamestate
     * @param l_writer file writer object.
     * @param l_formatOfMap  Map Format
     * @throws IOException Exception
     */
    private void parseMapObjectToFile(GameState p_gameState, FileWriter l_writer, String l_formatOfMap) throws IOException {
        if(l_formatOfMap.equalsIgnoreCase("ConquestMap")) {
            MapWriterAdaptor l_mapWriterAdapter = new MapWriterAdaptor(new ConquestMapFileWriter());
            l_mapWriterAdapter.parseMapToFile(p_gameState, l_writer, l_formatOfMap);
        } else {
            new MapFileWriter().parseMapObjectToFile(p_gameState, l_writer, l_formatOfMap);
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
            String l_formatMap = null;
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
                        l_formatMap = this.getFormatToSave();
                        Files.deleteIfExists(Paths.get(CommonUtil.getMapFilePath(p_mapFileName)));
                        FileWriter l_fileWriter = new FileWriter(CommonUtil.getMapFilePath(p_mapFileName));

                        parseMapObjectToFile(p_gameState, l_fileWriter, l_formatMap);
                        p_gameState.logUpdate("Map Saved Successfully", "effect");
                        l_fileWriter.close();
                    }
                } else {
                    p_gameState.logUpdate("Validation failed! Cannot Save the Map file!", "effect");
                    p_gameState.setD_errorMessage("Validation Failed");
                    return false;
                }
            }
            return true;
        } catch (IOException | InvalidMap l_e) {
            this.setD_LogMapService(l_e.getMessage(), p_gameState);
            p_gameState.logUpdate("Couldn't save the changes in map file!", "effect");
            p_gameState.setD_errorMessage("Error in saving map file");
            return false;
        }
    }

    /**
     * Resets Game State Map.
     *
     * @param p_gameState object of GameState.
     * @param p_loadFile File which couldn't be loaded
     */
    public void mapReset(GameState p_gameState, String p_loadFile) {
        System.out.println("Map cannot be loaded since it is invalid. Please provide a valid map");
        p_gameState.logUpdate(p_loadFile+" map is invalid, it can't be loaded !","effect");
        p_gameState.setD_map(new Models.Map());
    }

    /**
     * Method to modify Map based on the operation type ( add/remove) and arguments
     * @param p_gameState Current GameState Object
     * @param p_mapToBeModified Map under modification
     * @param p_operationType Add/Remove operation
     * @param p_argument Arguments
     * @return Map object with updated continents
     * @throws InvalidMap exception
     */
    public Map addOrRemoveContinents(GameState p_gameState, Map p_mapToBeModified, String p_operationType,
                                     String p_argument) throws InvalidMap {
        try {
            if (p_operationType.equalsIgnoreCase("add") && p_argument.split(" ").length==2) {
                p_mapToBeModified.addContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
                this.setD_LogMapService("Continent "+ p_argument.split(" ")[0]+ " successfully added!", p_gameState);
            } else if (p_operationType.equalsIgnoreCase("remove") && p_argument.split(" ").length==1) {
                p_mapToBeModified.removeContinent(p_argument.split(" ")[0]);
                this.setD_LogMapService("Continent "+ p_argument.split(" ")[0]+ " successfully removed!", p_gameState);
            } else {
                throw new InvalidMap("Continent "+p_argument.split(" ")[0]+" couldn't be added or removed." +
                        " Invalid Command Passed, hence changes are not done.");
            }
        } catch (InvalidMap | NumberFormatException l_exception) {
            this.setD_LogMapService(l_exception.getMessage(), p_gameState);
        }
        return p_mapToBeModified;
    }

    /**
     * Method to add/remove countries in the map
     * @param p_gameState Current GameState Object
     * @param p_mapToBeModified Map to be modified
     * @param p_operationType Add/Remove type operation
     * @param p_argument Arguments provided for the command operation
     * @return Modified map object
     * @throws InvalidMap
     */
    public Map addOrRemoveCountry(GameState p_gameState, Map p_mapToBeModified, String p_operationType,String p_argument) throws InvalidMap{
        try {
            if (p_operationType.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                p_mapToBeModified.addCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_LogMapService("Country "+ p_argument.split(" ")[0]+ " successfully added!", p_gameState);
            }else if(p_operationType.equalsIgnoreCase("remove")&& p_argument.split(" ").length==1){
                p_mapToBeModified.removeCountry(p_argument.split(" ")[0]);
                this.setD_LogMapService("Country "+ p_argument.split(" ")[0]+ " successfully removed!", p_gameState);
            }else{
                throw new InvalidMap("Country "+p_argument.split(" ")[0]+" could not be "+ p_operationType +"ed!");
            }
        } catch (InvalidMap | NumberFormatException l_exception) {
            this.setD_LogMapService(l_exception.getMessage(), p_gameState);
        }
        return p_mapToBeModified;
    }

    /**
     * Method to add/remove neighbor on Map object
     *
     * @param p_mapToBeModified Map object to be modified
     * @param p_operationType Add/remove operation
     * @param p_argument Arguments provided for the command operation
     * @return modified map object
     * @throws InvalidMap exception
     */
    public Map addOrRemoveNeighbor(GameState p_gameState, Map p_mapToBeModified, String p_operationType, String p_argument) throws InvalidMap{
        try {
            if (p_operationType.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                p_mapToBeModified.addCountryNeighbor(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_LogMapService("Neighbour Pair "+p_argument.split(" ")[0]+" "+p_argument.split(" ")[1]+" successfully added!", p_gameState);
            }else if(p_operationType.equalsIgnoreCase("remove") && p_argument.split(" ").length==2){
                p_mapToBeModified.removeCountryNeighbor(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_LogMapService("Neighbour Pair "+p_argument.split(" ")[0]+" "+p_argument.split(" ")[1]+" successfully removed!", p_gameState);
            }else{
                throw new InvalidMap("Neighbour could not be "+ p_operationType +"ed!");
            }
        } catch (InvalidMap l_exception) {
            this.setD_LogMapService(l_exception.getMessage(), p_gameState);
        }
        return p_mapToBeModified;
    }

    /**
     * This method validates what format user wants to save the map file.
     *
     * @return String format of the map file
     * @throws IOException exception while reading inputs from user
     */
    public String getFormatToSave() throws IOException {
        BufferedReader l_tempReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter 1 to save the map as conquest map format or else enter 2");
        String l_nextOrderCheck = l_tempReader.readLine();
        if (l_nextOrderCheck.equalsIgnoreCase("1")) {
            return "ConquestMap";
        } else if (l_nextOrderCheck.equalsIgnoreCase("2")) {
            return "NormalMap";
        } else {
            System.err.println("Invalid Input Passed.");
            return this.getFormatToSave();
        }
    }

    /**
     * Manages the Edit Operations: editcontinent, editcountry, editneighbor.
     *
     * @param p_gameState Object of the current Game State.
     * @param p_argument Arguments for the command operation.
     * @param p_operation Add or Remove operation to be performed.
     * @param p_switchParameter Type of Edit Operation.
     * @throws IOException Exception.
     * @throws InvalidMap invalidmap exception.
     * @throws InvalidCommand invalid command exception
     */
    public void editFunctionality(GameState p_gameState, String p_argument, String p_operation, Integer p_switchParameter) throws IOException, InvalidMap, InvalidCommand {
        Map l_editedMap;
        String l_mapFileName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeEdited = (CommonUtil.isNull(p_gameState.getD_map().getD_continents())
                && CommonUtil.isNull(p_gameState.getD_map().getD_countries()))
                ? this.mapLoad(p_gameState, l_mapFileName) : p_gameState.getD_map();

        // Editing Logic for Continent, Country And Neighbor
        if(!CommonUtil.isNull(l_mapToBeEdited)){
            switch(p_switchParameter){
                case 1:
                    l_editedMap = addOrRemoveContinents(p_gameState, l_mapToBeEdited, p_operation, p_argument);
                    break;
                case 2:
                    l_editedMap = addOrRemoveCountry(p_gameState, l_mapToBeEdited, p_operation, p_argument);
                    break;
                case 3:
                    l_editedMap = addOrRemoveNeighbor(p_gameState, l_mapToBeEdited, p_operation, p_argument);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + p_switchParameter);
            }
            p_gameState.setD_map(l_editedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }

    /**
     * Setting up logs for map editor methods.
     *
     * @param p_MapServiceLog String containing log
     * @param p_gameState current gamestate instance
     */
    public void setD_LogMapService(String p_MapServiceLog, GameState p_gameState){
        System.out.println(p_MapServiceLog);
        p_gameState.logUpdate(p_MapServiceLog, "effect");
    }
}
