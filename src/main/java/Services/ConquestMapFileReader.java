package Services;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import Constants.GameConstants;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;

/**
 * Reader class to read and parse conquest map file.
 *
 * @author Yatish Chutani
 * @version 3.0.0
 */
public class ConquestMapFileReader implements Serializable{
    /**
     * This method is used to read conquest map file, parses it and stores it in map.
     *
     * @param p_gameState the current state of the game
     * @param p_map map that is to be set
     * @param p_fileLines number of lines in the loaded file
     */
    public void scanConquestFile(GameState p_gameState, Map p_map, List<String> p_fileLines){
        List<String> l_continentData = renderMetaData(p_fileLines, "continent");
        List<String> l_countryData = renderMetaData(p_fileLines, "country");
        List<Continent> l_continentObjects = parseMetaDataContinent(l_continentData);
        List<Country> l_countryObjects = parseMetaDataCountry(l_countryData, l_continentObjects);
        List<Country> l_updatedCountries = parseMetaDataBorder(l_countryObjects, l_countryData);

        l_continentObjects = attachCountryContinents(l_updatedCountries, l_continentObjects);
        p_map.setD_continents(l_continentObjects);
        p_map.setD_countries(l_countryObjects);
        p_gameState.setD_map(p_map);
    }

    /**
     * This method returns the number of lines in the corresponding map file.
     *
     * @param p_linesOfFile Number of lines in the map document
     * @param p_switchParameter The type of lines needed which are country/continent
     * @return Returns the list required for set of lines
     */
    public List<String> renderMetaData(List<String> p_linesOfFile, String p_switchParameter){
        switch (p_switchParameter){
            case "country":
                List<String> l_countryLines = p_linesOfFile.subList(p_linesOfFile.indexOf(GameConstants.CONQUEST_TERRITORIES) + 1, p_linesOfFile.size());
                return l_countryLines;
            case "continent":
                List<String> l_continentLines = p_linesOfFile.subList(p_linesOfFile.indexOf(GameConstants.CONQUEST_CONTINENTS) + 1, p_linesOfFile.indexOf(GameConstants.CONQUEST_TERRITORIES) - 1);
                return l_continentLines;
            default:
                return null;
        }
    }

    /**
     * The parseContinentsMetaData function processes the extracted continent information from a map file.
     *
     * @param p_continentList It includes the continent data in list from map file
     * @return Returns the list of processed continent metadata
     */
    public List<Continent> parseMetaDataContinent(List<String> p_continentList) {
        int l_continentID = 1;
        List<Continent> l_continents = new ArrayList<Continent>();

        for (String cont : p_continentList) {
            String[] l_metaData = cont.split("=");
            l_continents.add(new Continent(l_continentID, l_metaData[0], Integer.parseInt(l_metaData[1])));
            l_continentID++;
        }
        return l_continents;
    }

    /**
     * The parseCountriesMetaData function processes the extracted country and border data from a map file.
     *
     * @param p_countryList It includes the country data in list from map file.
     * @param p_continentList The list of continents present in map file.
     * @return returns the list of processed country metadata.
     */
    public List<Country> parseMetaDataCountry(List<String> p_countryList, List<Continent> p_continentList){
        List<Country> l_countryList = new ArrayList<Country>();
        int l_countryID = 1;
        for (String country : p_countryList){
            String[] l_metaDataCountries = country.split(",");
            Continent l_continent = this.renderContinentByName(p_continentList, l_metaDataCountries[3]);
            Country l_countryObj = new Country(l_countryID, l_metaDataCountries[0], l_continent.getD_continentID());
            l_countryList.add(l_countryObj);
            l_countryID++;
        }
        return l_countryList;
    }

    /**
     * This method links the Country Objects to their respective neighbors.
     *
     * @param p_countryList It is the total Country Objects Initialized
     * @param p_countryLines It shows the country and border information
     * @return Returns a list of updated country objects
     */
    public List<Country> parseMetaDataBorder(List<Country> p_countryList, List<String> p_countryLines){
        List<Country> l_updatedCountryList = new ArrayList<>(p_countryList);
        String l_matchedCountry = null;
        for(Country l_country : l_updatedCountryList){
            for (String l_countryStr : p_countryLines){
                if((l_countryStr.split(",")[0]).equalsIgnoreCase(l_country.getD_countryName())){
                    l_matchedCountry = l_countryStr;
                    break;
                }
            }
            if (l_matchedCountry.split(",").length > 4){
                for(int i = 4; i < l_matchedCountry.split(",").length; i++){
                    Country l_cont = this.renderCountryByName(p_countryList, l_matchedCountry.split(",")[i]);
                    l_country.getD_neighborCountryIDs().add(l_cont.getD_countryID());
                }
            }
        }
        return l_updatedCountryList;
    }

    /**
     * This method links the countries to corresponding continents and sets them in object of continent.
     *
     * @param p_countries The total number of country objects
     * @param p_continents The total number of continent objects
     * @return Returns the list of updated continents
     */
    public List<Continent> attachCountryContinents(List<Country> p_countries, List<Continent> p_continents){
        for (Country c : p_countries){
            for (Continent cont : p_continents){
                if(cont.getD_continentID().equals(c.getD_continentID())){
                    cont.addCountry(c);
                }
            }
        }
        return p_continents;
    }

    /**
     * This method filters the continent based on continent name.
     *
     * @param p_continentList A list of continents from which filtering has to be done
     * @param p_continentName The name of the continent which has to be matched
     * @return Returns the filtered continent based on name
     */
    public Continent renderContinentByName(List<Continent> p_continentList, String p_continentName){
        Continent l_continent = p_continentList.stream().filter(l_cont -> l_cont.getD_continentName().equalsIgnoreCase(p_continentName)).findFirst().orElse(null);
        return l_continent;
    }

    /**
     * This method filters the country based on country name.
     *
     * @param p_countryList A list of countries from which filtering has to be done
     * @param p_countryName The name of the country which has to be matched
     * @return Returns the filtered country based on name
     */
    public Country renderCountryByName(List<Country> p_countryList, String p_countryName){
        Country l_country = p_countryList.stream().filter(l_cont -> l_cont.getD_countryName().equalsIgnoreCase(p_countryName)).findFirst().orElse(null);
        return l_country;
    }
}