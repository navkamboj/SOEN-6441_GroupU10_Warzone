package Services;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import Constants.GameConstants;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;

public class ConquestMapFileReader implements Serializable{
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

    public List<String> renderMetaData(List<String> p_linesOfFile, String p_switchParameter){
        switch (p_switchParameter){
            case "country":
                List<String> l_countryLines = p_linesOfFile.subList(p_linesOfFile.indexOf(GameConstants.CONQUEST_TERRITORIES) + 1, p_linesOfFile.size());
                return l_countryLines;
            case "continents":
                List<String> l_continentLines = p_linesOfFile.subList(p_linesOfFile.indexOf(GameConstants.CONQUEST_CONTINENTS) + 1, p_linesOfFile.indexOf(GameConstants.CONQUEST_TERRITORIES) - 1);
                return l_continentLines;
            default:
                return null;
        }
    }

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

    public Continent renderContinentByName(List<Continent> p_continentList, String p_continentName){
        Continent l_continent = p_continentList.stream().filter(l_cont -> l_cont.getD_continentName().equalsIgnoreCase(p_continentName)).findFirst().orElse(null);
        return l_continent;
    }

    public Country renderCountryByName(List<Country> p_countryList, String p_countryName){
        Country l_country = p_countryList.stream().filter(l_cont -> l_cont.getD_countryName().equalsIgnoreCase(p_countryName)).findFirst().orElse(null);
        return l_country;
    }
}