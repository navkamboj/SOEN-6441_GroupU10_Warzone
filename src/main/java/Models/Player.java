package Models;

import Constants.ApplicationConstants;
import Utils.Command;
import Utils.CommonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class depicts the player information and services.
 *
 * @author Pranjalesh Ghansiyal
 * @version 1.0.0
 */
public class Player {
    /**
     * Color to show the details with on the map.
     */
    private String d_color;

    /**
     * Player name.
     */
    private String d_name;

    /**
     * List of countries owned by player.
     */
    List<Country> d_ownedCountries;

    /**
     * List of Continents owned by player.
     */
    List<Continent> d_ownedContinents;

    /**
     * List of orders given by the player.
     */
    List<Order> d_executeOrders;

    /**
     * Number of armies given to the player.
     */
    Integer d_noOfAllocatedArmies;

    /**
     * This is default constructor.
     */
    public Player() {
    }

    /**
     * This parameterized constructor is used to create player with name and assign default
     * armies.
     *
     * @param p_playerName name of the player.
     */
    public Player(String p_playerName) {
        this.d_name = p_playerName;
        this.d_noOfAllocatedArmies = 0;
        this.d_executeOrders = new ArrayList<>();
    }

    /**
     * This getter is used to get color code for player.
     *
     * @return Color
     */
    public String getD_color() {
        return d_color;
    }

    /**
     *
     * @param p_color ANSI color code.
     */
    public void setD_color(String p_color) {
        d_color = p_color;
    }

    /**
     * This getter is used to get the player name.
     *
     * @return player name.
     */
    public String getPlayerName() {
        return d_name;
    }

    /**
     * This setter is used to set the player name.
     *
     * @param p_name set player name.
     */
    public void setPlayerName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * This getter is used to get list of countries owned by player.
     *
     * @return return countries owned by player.
     */
    public List<Country> getD_ownedCountries() {
        return d_ownedCountries;
    }

    /**
     * This setter is used to set list of countries owned by player.
     *
     * @param p_ownedCountries set countries owned by player.
     */
    public void setD_ownedCountries(List<Country> p_ownedCountries) {
        this.d_ownedCountries = p_ownedCountries;
    }

    /**
     * This getter is used to get list of continents owned by player.
     *
     * @return return list of continents owned by player.
     */
    public List<Continent> getD_ownedContinents() {
        return d_ownedContinents;
    }

    /**
     * This setter is used to set list of continents owned by player.
     *
     * @param p_ownedContinents set continents owned by player.
     */
    public void setD_ownedContinents(List<Continent> p_ownedContinents) {
        this.d_ownedContinents = p_ownedContinents;
    }

    /**
     * This getter is used to get the allocated armies for the player.
     *
     * @return return allocated armies of player.
     */
    public Integer getD_noOfAllocatedArmies() {
        return d_noOfAllocatedArmies;
    }

    /**
     * This setter is used to set number of allocated armies to player.
     *
     * @param p_numberOfArmies set number of armies to player.
     */
    public void setD_noOfAllocatedArmies(Integer p_numberOfArmies) {
        this.d_noOfAllocatedArmies = p_numberOfArmies;
    }

    /**
     * This getter is used to get execute orders of player.
     *
     * @return return execute orders.
     */
    public List<Order> getD_executeOrders() {
        return d_executeOrders;
    }

    /**
     * This setter is used to set execute orders player.
     *
     * @param p_executeOrders set execute orders.
     */
    public void setD_executeOrders(List<Order> p_executeOrders) {
        this.d_executeOrders = p_executeOrders;
    }

    /**
     * Extracts the list of names of countries owned by the player.
     *
     * @return list of country names
     */
    public List<String> getNamesOfCountries(){
        List<String> l_countryNames=new ArrayList<String>();
        for(Country c: d_ownedCountries){
            l_countryNames.add(c.getD_countryName());
        }
        return l_countryNames;
    }

    /**
     * Retrieves the list of continent names owned by the player.
     *
     * @return list of continent names
     */
    public List<String> getNamesOfContinents(){
        List<String> l_continentNames = new ArrayList<String>();
        if (d_ownedContinents != null) {
            for(Continent c: d_ownedContinents){
                l_continentNames.add(c.getD_continentName());
            }
            return l_continentNames;
        }
        return null;
    }

    /**
     * Gives the first order from the list of orders by the player and then removes it from the given
     * list.
     *
     * @return First order from the list of orders given by the player.
     */
    public Order next_order() {
        if (CommonUtil.isEmptyCollection(this.d_executeOrders)) {
            return null;
        }
        Order l_order = this.d_executeOrders.get(0);
        this.d_executeOrders.remove(l_order);
        return l_order;
    }
}
