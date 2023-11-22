package Services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import Constants.GameConstants;
import Models.Continent;
import Models.Country;
import Models.GameState;

public class ConquestMapFileWriter implements Serializable{
    public void parseMapToFile(GameState p_gameState, FileWriter p_writer, String p_formatOfMap) throws IOException{
        if (null != p_gameState.getD_map().getD_continents() && !p_gameState.getD_map().getD_continents().isEmpty()){
            writeMetadataContinent(p_gameState, p_writer);
        }
        if (null != p_gameState.getD_map().getD_countries() && !p_gameState.getD_map().getD_countries().isEmpty()){
            writeMetaDataCountryAndBorder(p_gameState, p_writer);
        }
    }

    private void writeMetaDataCountryAndBorder(GameState p_gameState, FileWriter p_writer) throws IOException{
        String l_countryMetaData = new String();

        p_writer.write(System.lineSeparator() + GameConstants.CONQUEST_TERRITORIES + System.lineSeparator());
        for (Country l_country : p_gameState.getD_map().getD_countries()){
            l_countryMetaData = new String();
            l_countryMetaData = l_country.getD_countryName().concat(",dummy1,dummy2,").concat(p_gameState.getD_map().getContinentByID(l_country.getD_continentID()).getD_continentName());

            if(null != l_country.getD_neighborCountryIDs() && !l_country.getD_neighborCountryIDs().isEmpty()) {
                for (Integer l_nghCountry : l_country.getD_neighborCountryIDs()) {
                    l_countryMetaData = l_countryMetaData.concat(",").concat(p_gameState.getD_map().getCountryByID(l_nghCountry).getD_countryName());
                }
            }
            p_writer.write(l_countryMetaData + System.lineSeparator());
        }
    }

    private void writeMetadataContinent(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + GameConstants.CONQUEST_CONTINENTS + System.lineSeparator());
        for (Continent l_continent : p_gameState.getD_map().getD_continents()){
            p_writer.write(
                    l_continent.getD_continentName().concat("=").concat(l_continent.getD_continentValue().toString()) + System.lineSeparator()
            );
        }
    }
}
