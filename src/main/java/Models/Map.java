package Models;

import java.util.HashMap;
import java.util.List;

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
     * @return country list
     */
    public List<Country> getD_countries() {
        return d_countries;
    }

    /**
     * Setter method to set the list of countries
     * @param p_countries
     */
    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }


    /**
     * Getter method to retrieve the continent list
     * @return Continent list
     */
    public List<Continent> getD_continents() {
        return d_continents;
    }

    /**
     * Setter method to set the continent list
     * @param p_continents
     */
    public void setD_continents(List<Continent> p_continents) {
        this.d_continents = p_continents;
    }

    /**
     * Getter method to retrieve the map file
     * @return Map file
     */
    public String getD_mapFile() {
        return d_mapFile;
    }

    /**
     * Setter method to set the map file
     * @param p_mapFile
     */
    public void setD_mapFile(String p_mapFile) {
        this.d_mapFile = p_mapFile;
    }

}
