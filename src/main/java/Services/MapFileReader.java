package Services;

import java.util.*;
import java.io.Serializable;

import Constants.GameConstants;
import Models.Continent;
import Models.Map;
import Models.GameState;
import Models.Country;


/**
 * The Reader Map file Class to parse Map File.
 * @author Harsh Tank
 * @version 3.0.0
 */
public class MapFileReader implements Serializable {

    /**
     * This method returns the corresponding map file lines.
     *
     * @param p_fileLines lines in the map document
     * @param p_switchParam Parameters options : country, continent, borders
     * @return List the required set of lines
     */
    public List<String> retrieveMetaData(List<String> p_fileLines, String p_switchParam) {
        switch (p_switchParam) {
            case "country":
                List<String> l_cntryFiles = p_fileLines.subList(p_fileLines.indexOf(GameConstants.COUNTRIES) + 1,
                        p_fileLines.indexOf(GameConstants.BORDERS) - 1);
                return l_cntryFiles;
            case "continent":
                List<String> l_continentFiles = p_fileLines.subList(
                        p_fileLines.indexOf(GameConstants.CONTINENTS) + 1,
                        p_fileLines.indexOf(GameConstants.COUNTRIES) - 1);
                return l_continentFiles;
            case "border":
                List<String> l_bordersFiles = p_fileLines.subList(p_fileLines.indexOf(GameConstants.BORDERS) + 1,
                        p_fileLines.size());
                return l_bordersFiles;
            default:
                return null;
        }
    }

    /**
     * This method parse the continent meta information of map
     * file.
     *
     * @param p_listOfContinent list of continent data from map file
     * @return List of evaluated meta data of continent
     */
    public List<Continent> parseEvaluatedContinentsMetaData(List<String> p_listOfContinent) {

        int l_continentId = 1;
        List<Continent> l_tempContinents = new ArrayList<>();

        for (String cont : p_listOfContinent) {
            String[] l_metaInfo = cont.split(" ");
            l_tempContinents.add(new Continent(l_continentId, l_metaInfo[0], Integer.parseInt(l_metaInfo[1])));
            l_continentId++;
        }
        return l_tempContinents;
    }

    /**
     * This method parse the country and border data of map file.
     *
     * @param p_listOfCountries includes country data in list from map file.
     * @return List of evaluated meta data of country.
     */
    public List<Country> parseEvaluatedCountriesMetaData(List<String> p_listOfCountries) {

        List<Country> l_listOfCountries = new ArrayList<>();

        for (String country : p_listOfCountries) {
            String[] l_metaDataCountries = country.split(" ");
            l_listOfCountries.add(new Country(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1],
                    Integer.parseInt(l_metaDataCountries[2])));
        }
        return l_listOfCountries;
    }

    /**
     * This method links countries to associated continents
     *
     * @param p_countries All Country Objects
     * @param p_continents All Continent Objects
     * @return List of updated continents
     */
    public List<Continent> linkCountryWithContinents(List<Country> p_countries, List<Continent> p_continents) {
        for (Country cntry : p_countries) {
            for (Continent cont : p_continents) {
                if (cont.getD_continentID().equals(cntry.getD_continentID())) {
                    cont.addCountry(cntry);
                }
            }
        }
        return p_continents;
    }

    /**
     * This method links the Country Objects to their associated neighbors.
     *
     * @param p_listOfCountries List of Countries Object
     * @param p_listOfBorders List of Border Data Lines
     * @return List of Updated Objects of Country type
     */
    public List<Country> parseEvaluatedBorderMetaData(List<Country> p_listOfCountries, List<String> p_listOfBorders) {
        LinkedHashMap<Integer, List<Integer>> l_cntryNeighbors = new LinkedHashMap<>();

        for (String l_tempBorder : p_listOfBorders) {
            if (null != l_tempBorder && !l_tempBorder.isEmpty()) {
                ArrayList<Integer> l_neighboursList = new ArrayList<>();
                String[] l_splitString = l_tempBorder.split(" ");
                for (int i = 1; i <= l_splitString.length - 1; i++) {
                    l_neighboursList.add(Integer.parseInt(l_splitString[i]));

                }
                l_cntryNeighbors.put(Integer.parseInt(l_splitString[0]), l_neighboursList);
            }
        }
        for (Country c : p_listOfCountries) {
            List<Integer> l_adjacentCountries = l_cntryNeighbors.get(c.getD_countryID());
            c.setD_neighborCountryIDs(l_adjacentCountries);
        }
        return p_listOfCountries;
    }

    /**
     * Converts the map file to game state map object.
     *
     * @param p_fileLines lines in the map document
     * @param p_gameState current gameState
     * @param p_map current map
     */

    public void mapFileParse(GameState p_gameState, Map p_map, List<String> p_fileLines) {
        // Parse the Map file
        List<String> l_cntryData = retrieveMetaData(p_fileLines, "country");
        List<String> l_bordersData = retrieveMetaData(p_fileLines, "border");
        List<Country> l_cntryObj = parseEvaluatedCountriesMetaData(l_cntryData);
        List<String> l_continentData = retrieveMetaData(p_fileLines, "continent");
        List<Continent> l_continentObj = parseEvaluatedContinentsMetaData(l_continentData);

        // Updates the neighbour of countries in Objects
        l_cntryObj = parseEvaluatedBorderMetaData(l_cntryObj, l_bordersData);
        l_continentObj = linkCountryWithContinents(l_cntryObj, l_continentObj);
        p_map.setD_continents(l_continentObj);
        p_map.setD_countries(l_cntryObj);
        p_gameState.setD_map(p_map);
    }
}
