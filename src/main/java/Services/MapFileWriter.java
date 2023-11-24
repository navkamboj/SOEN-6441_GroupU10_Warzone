package Services;
import java.io.*;
import java.util.*;

import Constants.GameConstants;
import Models.Country;
import Models.Continent;
import Models.GameState;

/**
 * This Class acts as Writer class to create generic map file.
 * @author Harsh Tank
 * @version 3.0.0
 *
 */
public class MapFileWriter implements Serializable {

    /**
     * This method fetches continents' data from game state and writes it to file.
     *
     * @param p_gameState Current GameState
     * @param p_writer Writer Object for file
     * @throws IOException handles I/O
     */
    private void writeEvaluatedContinentMetaInfo(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + GameConstants.CONTINENTS + System.lineSeparator());
        for (Continent l_tempContinent : p_gameState.getD_map().getD_continents()) {
            p_writer.write(
                    l_tempContinent.getD_continentName().concat(" ").concat(l_tempContinent.getD_continentValue().toString())
                            + System.lineSeparator());
        }
    }


    /**
     * This method fetches the country and boarder data and writes it to file writer.
     *
     * @param p_gameState Current GameState Object
     * @param p_writer Writer object for file
     * @throws IOException handles I/0
     */
    private void writeEvaluatedCountryAndBoarderMetaInfo(GameState p_gameState, FileWriter p_writer) throws IOException {
        String l_cntryMetaInfo;
        String l_bordersMetaInfo;
        List<String> l_listOfBorders = new ArrayList<>();

        // Writes Country Objects to File And Organizes Border Data for each of them
        p_writer.write(System.lineSeparator() + GameConstants.COUNTRIES + System.lineSeparator());
        for (Country l_tempCntry : p_gameState.getD_map().getD_countries()) {
            l_cntryMetaInfo = "";
            l_cntryMetaInfo = l_tempCntry.getD_countryID().toString().concat(" ").concat(l_tempCntry.getD_countryName())
                    .concat(" ").concat(l_tempCntry.getD_continentID().toString());
            p_writer.write(l_cntryMetaInfo + System.lineSeparator());

            if (null != l_tempCntry.getD_neighborCountryIDs() && !l_tempCntry.getD_neighborCountryIDs().isEmpty()) {
                l_bordersMetaInfo = "";
                l_bordersMetaInfo = l_tempCntry.getD_countryID().toString();
                for (Integer l_adjCountry : l_tempCntry.getD_neighborCountryIDs()) {
                    l_bordersMetaInfo = l_bordersMetaInfo.concat(" ").concat(l_adjCountry.toString());
                }
                l_listOfBorders.add(l_bordersMetaInfo);
            }
        }

        // Writes Border data to the File
        if (null != l_listOfBorders && !l_listOfBorders.isEmpty()) {
            p_writer.write(System.lineSeparator() + GameConstants.BORDERS + System.lineSeparator());
            for (String l_borderStr : l_listOfBorders) {
                p_writer.write(l_borderStr + System.lineSeparator());
            }
        }
    }

    /**
     * This method reads the map, parses it and stores it in specific type of map file.
     *
     * @param p_gameState current state of the game
     * @param l_writer file writer
     * @param l_formatOfMap format in which map file has to be saved
     * @throws IOException IO exception
     */
    public void parseMapObjectToFile(GameState p_gameState, FileWriter l_writer, String l_formatOfMap) throws IOException {
        if (null != p_gameState.getD_map().getD_continents()
                && !p_gameState.getD_map().getD_continents().isEmpty()) {
            writeEvaluatedContinentMetaInfo(p_gameState, l_writer);
        }
        if (null != p_gameState.getD_map().getD_countries()
                && !p_gameState.getD_map().getD_countries().isEmpty()) {
            writeEvaluatedCountryAndBoarderMetaInfo(p_gameState, l_writer);
        }
    }

}
