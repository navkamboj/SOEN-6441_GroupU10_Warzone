package Services;

import Constants.GameConstants;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
            List<String> l_continentData = retrieveMetaData(l_linesOfFile, "continent");
            List<Continent> l_continentObjects = loadContinentsMetaData(l_continentData);
            List<String> l_countryData = retrieveMetaData(l_linesOfFile, "country");
            List<String> l_bordersMetaData = retrieveMetaData(l_linesOfFile, "border");
            List<Country> l_countryObjects = loadCountriesMetaData(l_countryData);

            // Updates the neighbour of countries in Objects
            l_countryObjects = loadBorderMetaData(l_countryObjects, l_bordersMetaData);
            l_continentObjects = connectContinentsCountry(l_countryObjects, l_continentObjects);
            l_map.setD_continents(l_continentObjects);
            l_map.setD_countries(l_countryObjects);
            p_gameState.setD_map(l_map);
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
            String[] l_metaData = cont.split("");
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
            String[] l_metaDataCountries =country.split("");
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
                String[] l_splitString = l_border.split("");
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
                            continentMetaData(p_gameState, l_fileWriter);
                        }
                        if (null != p_gameState.getD_map().getD_countries()
                                && !p_gameState.getD_map().getD_countries().isEmpty()) {
                            countryAndBorderMetaData(p_gameState, l_fileWriter);
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
