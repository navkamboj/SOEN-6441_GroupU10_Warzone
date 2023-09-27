package Models;

import Exceptions.InvalidMap;

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
public class Map {
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
     * Method to add a country to the list of countries
     *
     * @param p_country
     */
    public void addCountry(Country p_country) {
        d_countries.add(p_country);
    }

    /**
     * Method to add a continent to the list of continents
     *
     * @param p_continent
     */
    public void addContinent(Continent p_continent) {
        d_continents.add(p_continent);
    }

    public Continent getContinent(String p_continentName) {
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentName().equals(p_continentName)).findFirst().orElse(null);
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

    public Boolean verifyNullCountryOrContinent() throws InvalidMap {
        if (d_continents.isEmpty() || d_continents == null) {
            throw new InvalidMap("Upload map should contain at least one continent!");
        }
        if (d_countries.isEmpty() || d_countries == null) {
            throw new InvalidMap("Upload map should contain at least one country!");
        }
        for (Country country : d_countries) {
            if (country.getD_neighborCountryIDs().size() < 1) {
                throw new InvalidMap(country.getD_countryName() + " has no neighbor");
            }
        }
        return false;
    }

    public Country getCountry(Integer p_countryID) {
        return d_countries.stream().filter(l_country -> l_country.getD_countryID().equals(p_countryID)).findFirst().orElse(null);
    }

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

    public void dfsCountry(Country p_country) throws InvalidMap {
        d_countrySpan.put(p_country.getD_countryID(), true);
        for (Country l_nextCountry : getNeighborCountry(p_country)) {
            if (!d_countrySpan.get(l_nextCountry.getD_countryID())) {
                dfsCountry(l_nextCountry);
            }
        }
    }

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

    public Boolean Validate() throws InvalidMap {
        return (!verifyNullCountryOrContinent() && verifyContinentConnectivity() && verifyCountryConnectivity());
    }


    public Country getCountryByName(String p_countryName) {
        return d_countries.stream().filter(l_country -> l_country.getD_countryName().equals(p_countryName)).findFirst().orElse(null);
    }

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
            throw new InvalidMap("Country:  " + p_countryName + " does not exist");
        }
    }

    public void removeNeighboringCountriesFromAll(Integer p_countryID) {
        for (Country country : d_countries) {
            if (!CommonUtil.isNull(country.getD_neighborCountryIDs())) {
                if (country.getD_neighborCountryIDs().contains(p_countryID)) {
                    country.removingNeighbor(p_countryID);
                }
            }
        }
    }

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
                throw new InvalidMap("Country can't be added to a non-existing continent");
            }
        } else {
            throw new InvalidMap("This " + p_countryName + " already Exists!");
        }
    }

}
