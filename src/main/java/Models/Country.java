package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class that manages all the countries on the provided map.
 *
 * @author Navjot Kamboj
 * @version 1.0.0
 */
public class Country {
    /**
     * Name of Country
     */
    String d_countryName;

    /**
     * Country ID
     */
    Integer d_countryID;

    /**
     * Number of Armies
     */
    Integer d_numberOfArmies;

    /**
     * Continent ID
     */
    Integer d_continentID;

    /**
     * List of countries neighboring to the selected one
     */
    List<Integer> d_neighborCountryIDs = new ArrayList<Integer>();

    /**
     * Constructor of this class with one parameter
     *
     * @param p_countryName
     */
    public Country(String p_countryName) {
        d_countryName = p_countryName;
    }

    /**
     * Constructor of this class with two parameters.
     *
     * @param p_countryID
     * @param p_continentID
     */
    public Country(int p_countryID, int p_continentID) {
        d_countryID = p_countryID;
        d_continentID = p_continentID;
    }

    /**
     * Constructor of this class with three parameters.
     *
     * @param p_countryID
     * @param p_countryName
     * @param p_continentID
     */
    public Country(int p_countryID, String p_countryName, int p_continentID) {
        d_countryID = p_countryID;
        d_countryName = p_countryName;
        d_continentID = p_continentID;
        d_numberOfArmies = 0;
    }

    /**
     * Getter method to retrieve CountryID
     *
     * @return CountryID
     */
    public Integer getD_countryID() {
        return d_countryID;
    }

    /**
     * Setter method to set CountryID
     *
     * @param p_countryID
     */
    public void setD_countryID(Integer p_countryID) {
        this.d_countryID = p_countryID;
    }

    /**
     * Getter method to retrieve Country Name
     *
     * @return Country Name
     */
    public String getD_countryName() {
        return d_countryName;
    }

    /**
     * Setter method to set the Country Name
     *
     * @param p_countryName
     */
    public void setD_countryName(String p_countryName) {
        this.d_countryName = p_countryName;
    }

    /**
     * Getter method to retrieve Continent ID
     *
     * @return Continent ID
     */
    public Integer getD_continentID() {
        return d_continentID;
    }

    /**
     * Setter method to set the Continent ID
     *
     * @param p_continentID
     */
    public void setD_continentID(Integer p_continentID) {
        this.d_continentID = p_continentID;
    }

    /**
     * Getter method to retrieve the number of armies
     *
     * @return numberOfArmies
     */
    public Integer getD_numberOfArmies() {
        return d_numberOfArmies;
    }

    /**
     * Setter method to set the number of armies
     *
     * @param p_numberOfArmies
     */
    public void setD_numberOfArmies(Integer p_numberOfArmies) {
        this.d_numberOfArmies = p_numberOfArmies;
    }

    /**
     * Getter method to retrieve the neighboring country IDs.
     *
     * @return List of IDs for neighboring countries
     */
    public List<Integer> getD_neighborCountryIDs() {
        if (d_neighborCountryIDs == null) {
            d_neighborCountryIDs = new ArrayList<Integer>();
        }
        return d_neighborCountryIDs;
    }

    /**
     * Setter method to set the neighboring country IDs
     *
     * @param p_neighborCountryIDs
     */
    public void setD_neighborCountryIDs(List<Integer> p_neighborCountryIDs) {
        this.d_neighborCountryIDs = p_neighborCountryIDs;
    }

    /**
     * Method to add country ID to the neighbor country list
     *
     * @param p_countryID
     */
    public void addingNeighbor(Integer p_countryID) {
        if (!d_neighborCountryIDs.contains(p_countryID))
            d_neighborCountryIDs.add(p_countryID);
    }

    /**
     * Method to removing an existing country from the neighbor country list
     *
     * @param p_countryID
     */
    public void removingNeighbor(Integer p_countryID) {
        if (d_neighborCountryIDs.contains(p_countryID)) {
            d_neighborCountryIDs.remove(d_neighborCountryIDs.indexOf(p_countryID));
        } else {
            System.out.println("Neighbour" + p_countryID + "doesn't exist");
        }
    }
}
