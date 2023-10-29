package Models;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
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
     * List of Continents owned by the player.
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
     * More orders to be accepted for player.
     */
    boolean d_isMoreOrders;

    /**
     * If the card per turn is already assigned.
     */
    boolean d_oneCardPerTurn = false;

    /**
     * List of players with whom negotiation is done hence can't attack.
     */
    List<Player> d_negotiatedWith = new ArrayList<Player>();

    /**
     * Name of the card owned by player
     */
    List<String> d_cardsOwned = new ArrayList<String>();

    /**
     * String to store log for individual Player methods.
     */
    String d_playerLog;

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
        this.d_isMoreOrders = true;
    }

    /**
     * Clears all the negotiations from the last turn.
     */
    public void clearNegotiation(){
        d_negotiatedWith.clear();
    }

    /**
     * Sets the Card per turn to the allocated bool.
     *
     * @param p_value Bool to Set.
     */
    public void setD_oneCardPerTurn(Boolean p_value){
        this.d_oneCardPerTurn = p_value;
    }

    /**
     * Gets info if more orders from player are to be accepted or not.
     *
     * @return boolean true if player wants to give more orders or else false
     */
    public boolean getD_moreOrders() {
        return d_isMoreOrders;
    }

    /**
     * Sets info if more orders from player are to be accepted or not.
     *
     * @param p_moreOrders Boolean true if player wants to give more order or else
     *                     false
     */
    public void setD_moreOrders(boolean p_moreOrders) {
        this.d_isMoreOrders = p_moreOrders;
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
    public String getD_playerName() {
        return d_name;
    }

    /**
     * This setter is used to set the player name.
     *
     * @param p_name set player name.
     */
    public void setD_playerName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Retrieves the player logs.
     *
     * @return Logs of player.
     */
    public String getD_playerLog(){
        return this.d_playerLog;
    }

    /**
     * This getter is used to get list of countries owned by player.
     *
     * @return return countries owned by player.
     */
    public List<Country> getD_ownedCountries() { return d_ownedCountries; }

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
    public Integer getD_noOfAllocatedArmies() {return d_noOfAllocatedArmies;}

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
     * It takes order as an input and adds it to players unassigned
     * orders pool.
     *
     * @throws IOException exception in reading inputs from user
     */
    public void issue_order(IssueOrderPhase p_issueOrderPhase) throws InvalidCommand, IOException, InvalidMap {
        p_issueOrderPhase.inputOrder(this);
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

    /**
     * Returns the cards owned by the game player.
     *
     * @return List of cards of string type
     */
    public List<String> getD_cardsOwned(){
        return this.d_cardsOwned;
    }

    /**
     * Verifies if more orders are to be accepted for player in further turn.
     *
     * @throws IOException exception to handle I/O operation
     */
    void checkForMoreOrders() {
        Scanner l_scanner = new Scanner(System.in);
        System.out.println("\nDo you want to give more orders for: " + this.getD_playerName()
                + " in further turns ? \nEnter Y/N");
        String l_checkNextOrder = l_scanner.nextLine();
        if (l_checkNextOrder.equalsIgnoreCase("Y") || l_checkNextOrder.equalsIgnoreCase("N")) {
            this.setD_moreOrders(l_checkNextOrder.equalsIgnoreCase("Y") ? true : false);
        } else {
            System.err.println("Enter a valid Input");
            this.checkForMoreOrders();
        }
    }
}
