package Models;
import Utils.CommonUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class that manages all the continents on the provided map.
 *
 * @author Yatish Chutani
 * @version 1.0.0
 */
public class Continent{
    /**
     * Continent ID.
     */
    Integer d_continentID;
    /**
     * Name of the Continent.
     */
    String d_continentName;
    /**
     * Value of the Continent.
     */
    Integer d_continentValue;
    /**
     * List of the countries.
     */
    List<Country> d_countries;

    /**
     * default constructor to the class Continent.
     */
    public Continent(){

    }

    /**
     * parameterized constructor to the class Continent.
     *
     * @param p_continentID continent ID
     * @param p_continentName continent name
     * @param p_continentValue continent value
     */
    public Continent(Integer p_continentID, String p_continentName, Integer p_continentValue){
        this.d_continentID = p_continentID;
        this.d_continentName = p_continentName;
        this.d_continentValue = p_continentValue;
    }

    /**
     * parameterized constructor to the class Continent.
     *
     * @param p_continentName Continent Name
     */
    public Continent(String p_continentName){
        this.d_continentName = p_continentName;
    }

    /**
     * getter method to retrieve the name of the Continent.
     *
     * @return continent name
     */
    public String getD_continentName(){
        return d_continentName;
    }

    /**
     * setter method to set the name of the Continent.
     *
     * @param p_continentName Name of the Continent
     */
    public void setD_continentName(String p_continentName){
        this.d_continentName= p_continentName;
    }

    /**
     * getter method to retrieve the value of Continent.
     *
     * @return Continent Value
     */
    public Integer getD_continentValue(){
        return d_continentValue;
    }

    /**
     * setter method to set the value of Continent.
     *
     * @param p_continentValue the Continent Value
     */
    public void setD_continentValue(Integer p_continentValue){
        this.d_continentValue= p_continentValue;
    }

    /**
     * getter method to retrieve the continent ID.
     *
     * @return continent ID
     */
    public Integer getD_continentID(){
        return d_continentID;
    }

    /**
     * setter method to set the Continent ID.
     *
     * @param p_continentID continent ID
     */
    public void setD_continentID(Integer p_continentID){
        this.d_continentID = p_continentID;
    }

    /**
     * getter method to retrieve the list of countries.
     *
     * @return list of countries
     */
    public List<Country> getD_countries(){
        return d_countries;
    }

    /**
     * setter method to set the list of Countries.
     *
     * @param p_countries list of countries
     */
    public void setD_countries(List<Country> p_countries){
        this.d_countries = p_countries;
    }

    /**
     * Method to add a specified country.
     *
     * @param p_country the country to be added
     */
    public void addCountry(Country p_country){
        if(d_countries!=null){
            d_countries.add(p_country);
        }
        else{
            d_countries = new ArrayList<Country>();
            d_countries.add(p_country);
        }
    }

    /**
     * Method to remove a Country from the Continent.
     *
     * @param p_country the country to be removed
     */
    public void removeCountry(Country p_country){
        if(d_countries==null){
            System.out.println("There is no such Country");
        }
        else{
            d_countries.remove(p_country);
        }
    }

    /**
     * Removes particular country ID from the adjacent neighbor list of all countries in continent.
     *
     * @param p_countryID ID of country to be removed
     */
    public void removeCountryNeighboursFromAll(Integer p_countryID){
        if(null!=d_countries && !d_countries.isEmpty()){
            for(Country c : d_countries){
                if(!CommonUtil.isNull(c.d_neighborCountryIDs)){
                    if(c.getD_neighborCountryIDs().contains(p_countryID)){
                        c.removingNeighbor(p_countryID);
                    }
                }
            }
        }
    }
}