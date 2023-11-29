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
     * This method retrieves the corresponding map file lines.
     *
     * @param p_fileLines this tells about the lines in the map document
     * @param p_switchCaseParameter the type of lines needed : country, continent, borders
     * @return the list of required sets of lines
     */
    public List<String> retrieveMetaData(List<String> p_fileLines, String p_switchCaseParameter){
        switch (p_switchCaseParameter) {
            case "continent":
                List<String> l_linesOfContinents = p_fileLines.subList(
                        p_fileLines.indexOf(GameConstants.CONTINENTS) + 1,
                        p_fileLines.indexOf(GameConstants.COUNTRIES) - 1
                );
                return l_linesOfContinents;
            case "country":
                List<String> l_linesOfCountries = p_fileLines.subList(
                        p_fileLines.indexOf(GameConstants.COUNTRIES) + 1,
                        p_fileLines.indexOf(GameConstants.BORDERS) - 1
                );
                return l_linesOfCountries;
            case "border":
                List<String> l_linesOfBorders = p_fileLines.subList(
                        p_fileLines.indexOf(GameConstants.BORDERS) + 1,
                        p_fileLines.size()
                );
                return l_linesOfBorders;
            default:
                return null;
        }
    }

    /**
     * This method loads the extracted continent data of the map file.
     *
     * @param p_listOfContinents this includes a list of continent's data from map file.
     * @return  a list of processed continent's meta data.
     */
    public List<Continent> loadContinentsMetaData(List<String> p_listOfContinents){
        int l_continentID = 1;
        List<Continent> l_continents = new ArrayList<Continent>();

        for(String cont : p_listOfContinents){
            String[] l_metaData = cont.split(" ");
            l_continents.add(new Continent(l_continentID, l_metaData[0], Integer.parseInt(l_metaData[1])));
            l_continentID++;
        }
        return l_continents;
    }

    /**
     * This method loads the extracted border and country data of the map file.
     *
     * @param p_listOfCountries this includes a list of country's data from map file.
     * @return  a list of processed country's meta data.
     */
    public List<Country> loadCountriesMetaData(List<String> p_listOfCountries){
        LinkedHashMap<Integer, List<Integer>> l_neighborOfCountries = new LinkedHashMap<Integer, List<Integer>>();
        List<Country> l_listOfCountries = new ArrayList<Country>();

        for(String country : p_listOfCountries){
            String[] l_metaDataCountries =country.split(" ");
            l_listOfCountries.add(new Country(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1],
                    Integer.parseInt(l_metaDataCountries[2])));
        }
        return l_listOfCountries;
    }

    /**
     * this method links the country's objects to their respective neighbors.
     *
     * @param p_listOfCountries this includes a list of country's data from map file
     * @param p_listOfBorders this includes a list of border's data from map file
     * @return a list of updated Country Objects
     */
    public List<Country> loadBorderMetaData(List<Country> p_listOfCountries, List<String> p_listOfBorders){
        LinkedHashMap<Integer, List<Integer>> l_neighborOfCountries = new LinkedHashMap<Integer, List<Integer>>();


        for(String l_border : p_listOfBorders) {
            if(null != l_border && !l_border.isEmpty()){
                ArrayList<Integer> l_neighbors = new ArrayList<Integer>();
                String[] l_splitString = l_border.split(" ");
                for(int i = 1; i <= l_splitString.length - 1; i++){
                    l_neighbors.add(Integer.parseInt(l_splitString[i]));
                }
                l_neighborOfCountries.put(Integer.parseInt(l_splitString[0]), l_neighbors);
            }
        }
        for(Country c : p_listOfCountries){
            List<Integer> l_adjacentCountries = l_neighborOfCountries.get(c.getD_countryID());
            c.setD_neighborCountryIDs(l_adjacentCountries);
        }
        return p_listOfCountries;
    }

    /**
     * this method connects countries to the respective continents and sets them in object of
     * continent.
     *
     * @param p_countries this is the total Country Objects
     * @param p_continents this is the total Continent Objects
     * @return a list of updated continents
     */
    public List<Continent> connectContinentsCountry(List<Country> p_countries, List<Continent> p_continents){
        for(Country c : p_countries){
            for(Continent cont : p_continents){
                if(cont.getD_continentID().equals(c.getD_continentID())){
                    cont.addCountry(c);
                }
            }
        }
        return p_continents;
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
            System.out.println("File has been created.");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editMapPath);
            p_gameState.setD_map(l_map);
            p_gameState.logUpdate(p_editMapPath+ " File has been created for user to edit", "effect");
        } else {
            System.out.println("File already exists.");
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
        } catch (IOException l_ioException) {
            l_ioException.printStackTrace();
            p_gameState.setD_errorMessage("Error in saving map file");
            return false;
        }
    }

    /**
     * this method retrieves country and boarder data from the game state and writes it to file writer.
     *
     * @param p_gameState this is the current GameState Object
     * @param p_writer this is the writer object for file
     * @throws IOException handles I/0
     */
    private void countryAndBorderMetaData(GameState p_gameState, FileWriter p_writer) throws IOException{
        String l_countryMetaData = new String();
        String l_bordersMetaData = new String();
        List<String> l_bordersList = new ArrayList<>();

        p_writer.write(System.lineSeparator() + GameConstants.COUNTRIES + System.lineSeparator());
        for(Country l_country : p_gameState.getD_map().getD_countries()){
            l_countryMetaData = new String();
            l_countryMetaData = l_country.getD_countryID().toString().concat(" ").concat(l_country.getD_countryName())
                    .concat(" ").concat(l_country.getD_continentID().toString());
            p_writer.write(l_countryMetaData + System.lineSeparator());

            if(null != l_country.getD_neighborCountryIDs() && !l_country.getD_neighborCountryIDs().isEmpty()){
                l_bordersMetaData = new String();
                l_bordersMetaData = l_country.getD_countryID().toString();
                for(Integer l_nghCountry : l_country.getD_neighborCountryIDs()){
                    l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_nghCountry.toString());
                }
                l_bordersList.add(l_bordersMetaData);
            }
        }

        if(null != l_bordersList && !l_bordersList.isEmpty()){
            p_writer.write(System.lineSeparator() + GameConstants.BORDERS + System.lineSeparator());
            for(String l_borderString : l_bordersList){
                p_writer.write(l_borderString + System.lineSeparator());
            }
        }
    }

    /**
     * this method retrieves continents' data from game state and writes it to file.
     *
     * @param p_gameState this is the current GameState
     * @param p_writer this is the writer object for file
     * @throws IOException handles I/O
     */
    private void continentMetaData(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + GameConstants.CONTINENTS + System.lineSeparator());
        for(Continent l_continent : p_gameState.getD_map().getD_continents()){
            p_writer.write(
                    l_continent.getD_continentName().concat(" ").concat(l_continent.getD_continentValue().toString())
                    + System.lineSeparator()
            );
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
