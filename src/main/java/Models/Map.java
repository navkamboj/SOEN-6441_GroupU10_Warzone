package Models;

import Exceptions.InvalidMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import Utils.CommonUtil;

import java.util.Collections;

/**
 * Model class to manage the uploaded maps
 *
 * @author Navjot Kamboj
 * @version 1.0.0
 */
public class Map implements Serializable {
    /**
     * List of countries
     */
    List<Country> d_countries;

    /**
     * List of the continents
     */
    List<Continent> d_continents;

    /**
     * String to store the name of map file name
     */
    String d_mapFile;

    /**
     * Hashmap representing the availability of spanning to different countries from current position
     */
    HashMap<Integer, Boolean> d_countrySpan = new HashMap<Integer, Boolean>();

    /**
     * Getter method to retrieve the list of countries
     *
     * @return country list
     */
    public List<Country> getD_countries() {
        return d_countries;
    }

    /**
     * Setter method to set the list of countries
     *
     * @param p_countries
     */
    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }


    /**
     * Getter method to retrieve the continent list
     *
     * @return Continent list
     */
    public List<Continent> getD_continents() {
        return d_continents;
    }

    /**
     * Setter method to set the continent list
     *
     * @param p_continents
     */
    public void setD_continents(List<Continent> p_continents) {
        this.d_continents = p_continents;
    }

    /**
     * Getter method to retrieve the map file
     *
     * @return Map file
     */
    public String getD_mapFile() {
        return d_mapFile;
    }

    /**
     * Setter method to set the map file
     *
     * @param p_mapFile
     */
    public void setD_mapFile(String p_mapFile) {
        this.d_mapFile = p_mapFile;
    }

    /**
     * Method to return an object of type Continent for the provided continent Name.
     *
     * @param p_continentName
     * @return
     */
    public Continent getContinent(String p_continentName) {
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentName().equals(p_continentName)).findFirst().orElse(null);
    }

    /**
     * Method to returns object of type Continent for the provided continent ID.
     *
     * @param p_continentID Continent ID to be searched
     * @return continent object
     */
    public Continent getContinentByID(Integer p_continentID){
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentID().equals(p_continentID)).findFirst().orElse(null);
    }

    /**
     * Method to returns object of type Country for the provided country ID.
     *
     * @param p_countryID Country ID to be searched
     * @return country object
     */
    public Country getCountryByID(Integer p_countryID){
        return d_countries.stream().filter(l_country -> l_country.getD_countryID().equals(p_countryID)).findFirst().orElse(null);
    }

    /**
     * Method to retrieve list of IDs for the countries
     *
     * @return Country ID list
     */
    public List<Integer> getCountryIdList() {
        List<Integer> l_countryIDs = new ArrayList<Integer>();
        if (!d_countries.isEmpty()) {
            for (Country country : d_countries) {
                l_countryIDs.add(country.getD_countryID());
            }
        }
        return l_countryIDs;
    }

    /**
     * Method to retrieve list of IDs for the continents
     *
     * @return Continent ID list
     */
    public List<Integer> getContinentIdList() {
        List<Integer> l_continentIDs = new ArrayList<Integer>();
        if (!d_continents.isEmpty()) {
            for (Continent continent : d_continents) {
                l_continentIDs.add(continent.getD_continentID());
            }
        }
        return l_continentIDs;
    }

    /**
     * Verifies if the objects (country or continent ) are null
     *
     * @return Boolean ,if false
     * @throws InvalidMap exception for Invalid conditions
     */
    public Boolean verifyNullCountryOrContinent() throws InvalidMap {
        if (d_continents == null || d_continents.isEmpty()) {
            throw new InvalidMap("Uploaded map should contain at least one continent!");
        }
        if (d_countries == null||d_countries.isEmpty()) {
            throw new InvalidMap("Uploaded map should contain at least one country!");
        }
        for (Country country : d_countries) {
            if (country.getD_neighborCountryIDs().size() < 1) {
                throw new InvalidMap(country.getD_countryName() + " has no neighbor");
            }
        }
        return false;
    }

    /**
     * Method to return a Country object when country ID is provided
     *
     * @param p_countryID
     * @return Country object that matches the country ID
     */
    public Country getCountry(Integer p_countryID) {
        return d_countries.stream().filter(l_country -> l_country.getD_countryID().equals(p_countryID)).findFirst().orElse(null);
    }

    /**
     * Method to verify connectivity for country in the map
     *
     * @return a boolean when all countries are connected
     * @throws InvalidMap exception when country is not connected
     */
    public boolean verifyCountryConnectivity() throws InvalidMap {
        for (Country country : d_countries) {
            d_countrySpan.put(country.getD_countryID(), false);
        }
        dfsCountry(d_countries.get(0));

        // Iteration over the countries to find the position of unreachable country
        for (Entry<Integer, Boolean> hash : d_countrySpan.entrySet()) {
            if (!hash.getValue()) {
                String l_exceptionString = getCountry(hash.getKey()).getD_countryName() + " country not reachable";
                throw new InvalidMap(l_exceptionString);
            }
        }
        return !d_countrySpan.containsValue(false);
    }

    /**
     * Method to perform DFS search from the entered node
     *
     * @param p_country
     * @throws InvalidMap
     */
    public void dfsCountry(Country p_country) throws InvalidMap {
        d_countrySpan.put(p_country.getD_countryID(), true);
        for (Country l_nextCountry : getNeighborCountry(p_country)) {
            if (!d_countrySpan.get(l_nextCountry.getD_countryID())) {
                dfsCountry(l_nextCountry);
            }
        }
    }

    /**
     * Method to provide list of neighboring country objects
     *
     * @param p_country (neighbor country)
     * @return Neighboring country list
     * @throws InvalidMap exception when neighboring countries are not present
     */
    public List<Country> getNeighborCountry(Country p_country) throws InvalidMap {
        List<Country> l_nghCountries = new ArrayList<Country>();

        if (p_country.getD_neighborCountryIDs().size() > 0) {
            for (int i : p_country.getD_neighborCountryIDs()) {
                l_nghCountries.add(getCountry(i));
            }
        } else {
            throw new InvalidMap(p_country.getD_countryName() + " doesn't have neighboring countries");
        }
        return l_nghCountries;
    }

    /**
     * Method to check inner connectivity of continent
     *
     * @param p_continent is the continent being verified
     * @return a boolean if continent is connected
     * @throws InvalidMap exception when country is not connected
     */
    public boolean subGraphConnection(Continent p_continent) throws InvalidMap {
        HashMap<Integer, Boolean> l_continentCountry = new HashMap<Integer, Boolean>();

        for (Country country : p_continent.getD_countries()) {
            l_continentCountry.put(country.getD_countryID(), false);
        }
        dfsSubgraph(p_continent.getD_countries().get(0), l_continentCountry, p_continent);

        // Iterates Over Entries to locate unreachable countries in continent
        for (Entry<Integer, Boolean> hash : l_continentCountry.entrySet()) {
            if (!hash.getValue()) {
                Country l_country = getCountry(hash.getKey());
                String l_exceptionMessage = l_country.getD_countryName() + " in Continent " + p_continent.getD_continentName() + " is not reachable";
                throw new InvalidMap(l_exceptionMessage);
            }
        }
        return !l_continentCountry.containsValue(false);
    }

    /**
     * Method to apply DFS to the continent subgraph
     *
     * @param p_country          visited country
     * @param p_continentCountry hashmap of visited bool values
     * @param p_continent        to be checked for connectivity
     */
    public void dfsSubgraph(Country p_country, HashMap<Integer, Boolean> p_continentCountry, Continent p_continent) {
        p_continentCountry.put(p_country.getD_countryID(), true);
        for (Country country : p_continent.getD_countries()) {
            if (p_country.getD_neighborCountryIDs().contains(country.getD_countryID())) {
                if (!p_continentCountry.get(country.getD_countryID())) {
                    dfsSubgraph(country, p_continentCountry, p_continent);
                }
            }
        }
    }

    /**
     * Method to verify inner connectivity of the continent
     *
     * @return Boolean value if the continents are connected
     * @throws InvalidMap exception when a continent is not connected
     */
    public Boolean verifyContinentConnectivity() throws InvalidMap {
        boolean l_connectivity = true;
        for (Continent continent : d_continents) {
            if (null == continent.getD_countries() || continent.getD_countries().size() < 1) {
                throw new InvalidMap(continent.getD_continentName() + " has no countries, it must possess atleast 1 country");
            }
            if (!subGraphConnection(continent)) {
                l_connectivity = false;
            }
        }
        return l_connectivity;
    }

    /**
     * Method to validate the map
     *
     * @return Boolean value for a valid map
     * @throws InvalidMap Exception if invalid map
     */
    public Boolean Validate() throws InvalidMap {
        return (!verifyNullCountryOrContinent() && verifyContinentConnectivity() && verifyCountryConnectivity());
    }

    /**
     * Method to return a Country object provided the country name
     *
     * @param p_countryName is name of the country to be searched
     * @return a matching country object
     */
    public Country getCountryByName(String p_countryName) {
        return d_countries.stream().filter(l_country -> l_country.getD_countryName().equals(p_countryName)).findFirst().orElse(null);
    }

    /**
     * Method to perform removal of country from the map
     *
     * @param p_countryName Country to be added
     * @throws InvalidMap exception for country that doesn't exist
     */
    public void removeCountry(String p_countryName) throws InvalidMap {
        if (d_countries != null && !CommonUtil.isNull(getCountryByName(p_countryName))) {
            for (Continent continent : d_continents) {
                if (continent.getD_continentID().equals(getCountryByName(p_countryName).getD_continentID())) {
                    continent.removeCountry(getCountryByName(p_countryName));
                }
                continent.removeCountryNeighboursFromAll(getCountryByName(p_countryName).getD_countryID());
            }
            removeNeighboringCountriesFromAll(getCountryByName(p_countryName).getD_countryID());
            d_countries.remove(getCountryByName(p_countryName));

        } else {
            throw new InvalidMap("Country: " + p_countryName + " does not exist");
        }
    }

    /**
     * Method to remove a country as neighbor from all associated countries in Country list
     *
     * @param p_countryID ID of country to be removed from map
     */
    public void removeNeighboringCountriesFromAll(Integer p_countryID) throws InvalidMap {
        for (Country country : d_countries) {
            if (!CommonUtil.isNull(country.getD_neighborCountryIDs())) {
                if (country.getD_neighborCountryIDs().contains(p_countryID)) {
                    country.removingNeighbor(p_countryID);
                }
            }
        }
    }

    /**
     * Method to perform addition of a country on the Map
     *
     * @param p_countryName   Name of country to be added
     * @param p_continentName Name of continent where country is to be added
     * @throws InvalidMap exception if country can't be added or it already exists
     */
    public void addCountry(String p_countryName, String p_continentName) throws InvalidMap {
        int l_countryId;
        if (d_countries == null) {
            d_countries = new ArrayList<Country>();
        }
        if (CommonUtil.isNull(getCountryByName(p_countryName))) {
            l_countryId = d_countries.size() > 0 ? Collections.max(getCountryIdList()) + 1 : 1;
            if (d_continents != null && getContinentIdList().contains(getContinent(p_continentName).getD_continentID())) {
                Country l_country = new Country(l_countryId, p_countryName, getContinent(p_continentName).getD_continentID());
                d_countries.add(l_country);
                for (Continent continent : d_continents) {
                    if (continent.getD_continentName().equals(p_continentName)) {
                        continent.addCountry(l_country);
                    }
                }
            } else {
                throw new InvalidMap("Country "+p_countryName+" can't be added to a non-existing continent");
            }
        } else {
            throw new InvalidMap("This " + p_countryName + " already Exists!");
        }
    }

    /**
     * Method to perform addition of continent on the map
     *
     * @param p_continentName Continent name to be added
     * @param p_value         is a control value
     * @throws InvalidMap exception to handle invalid addition
     */
    public void addContinent(String p_continentName, Integer p_value) throws InvalidMap {
        int l_continentId;

        if (d_continents != null) {
            l_continentId = d_continents.size() > 0 ? Collections.max(getContinentIdList()) + 1 : 1;
            if (CommonUtil.isNull(getContinent(p_continentName))) {
                d_continents.add(new Continent(l_continentId, p_continentName, p_value));
            } else {
                throw new InvalidMap("Continent"+p_continentName+" already exists so it can't be added");
            }
        } else {
            d_continents = new ArrayList<Continent>();
            d_continents.add(new Continent(1, p_continentName, p_value));
        }
    }

    /**
     * Method to remove a continent from the map
     * <ul>
     *     <li> Deletes countries and data associated with them in the map</li>
     *     <li> Deletes a specified continent</li>
     * </ul>
     *
     * @param p_continentName
     * @throws InvalidMap exception for invalid/ non-existing continents
     */
    public void removeContinent(String p_continentName) throws InvalidMap {
        if (d_continents != null) {
            if (!CommonUtil.isNull(getContinent(p_continentName))) {

                // To delete the continent and updates neighbours count
                if (getContinent(p_continentName).getD_countries() != null) {
                    for (Country country : getContinent(p_continentName).getD_countries()) {
                        removeNeighboringCountriesFromAll(country.getD_countryID());
                        updateNeighboursCount(country.getD_countryID());
                        d_countries.remove(country);
                    }
                }
                d_continents.remove(getContinent(p_continentName));
            } else {
                throw new InvalidMap("This continent does not exist!");
            }
        } else {
            throw new InvalidMap("There are no continents in the Map that can be removed");
        }
    }

    /**
     * Method to remove particular country as a neighbor from all associated countries
     *
     * @param p_countryID Country to be removed
     */
    public void updateNeighboursCount(Integer p_countryID) throws InvalidMap {
        for (Continent country : d_continents) {
            country.removeCountryNeighboursFromAll(p_countryID);
        }
    }

    /**
     * Method to perform addition of country as a neighbor
     *
     * @param p_countryName  Country whose neighbors are to be updated
     * @param p_neighborName Country to be added as a neighbor
     * @throws InvalidMap exception
     */
    public void addCountryNeighbor(String p_countryName, String p_neighborName) throws InvalidMap {
        if (d_countries != null) {
            if (!CommonUtil.isNull(getCountryByName(p_countryName)) && !CommonUtil.isNull(getCountryByName(p_neighborName))) {
                d_countries.get(d_countries.indexOf(getCountryByName(p_countryName))).addingNeighbor(getCountryByName(p_neighborName).getD_countryID());
            } else {
                throw new InvalidMap("Invalid Neighbour Pair! "+p_countryName+" "+p_neighborName+"! Either of the Countries Doesn't exist!");
            }
        }
    }

    /**
     * Performs the removal of country as a neigbor
     *
     * @param p_countryName  Country whose neighbors are to be updated
     * @param p_neighborName Country to be removed as neighbor
     * @throws InvalidMap exception
     */
    public void removeCountryNeighbor(String p_countryName, String p_neighborName) throws InvalidMap {
        if (d_countries != null) {
            if (!CommonUtil.isNull(getCountryByName(p_countryName)) && !CommonUtil.isNull(getCountryByName(p_neighborName))) {
                d_countries.get(d_countries.indexOf(getCountryByName(p_countryName))).removingNeighbor(getCountryByName(p_neighborName).getD_countryID());
            } else {
                throw new InvalidMap("Invalid Neighbour Pair !!! Either of the countries don't exist");
            }
        }
    }

}
