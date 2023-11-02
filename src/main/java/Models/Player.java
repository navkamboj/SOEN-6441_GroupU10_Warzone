package Models;

import Constants.GameConstants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.CommonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * This class depicts the player information and services.
 *
 * @author Pranjalesh Ghansiyal, Navjot Kamboj
 * @version 2.0.0
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
     * A String holding Log for individual Player methods.
     */
    String d_logPlayer;

    /**
     * Player order list
     */
    List<Order> d_orderedPlayerList;

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
     * This method specifies the countries player cannot issue an order on.
     *
     * @param p_playerNegotiation The player to negotiate with.
     */
    public void addPlayerNegotiation(Player p_playerNegotiation){
        this.d_negotiatedWith.add(p_playerNegotiation);
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
     * Setter method to print player's log.
     *
     * @param p_logPlayer String as log message
     * @param p_logType Type of log : error, or log
     */
    public void setD_playerLog(String p_logPlayer, String p_logType) {
        this.d_playerLog = p_logPlayer;
        if(p_logType.equals("error"))
            System.err.println(p_logPlayer);
        else if(p_logType.equals("log"))
            System.out.println(p_logPlayer);
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

    /**
     * This method uses random function to assign any random card from the set of available cards to
     * the player once he acquires a territory.
     *
     */
    public void assignCard(){
        if(!d_oneCardPerTurn){
            Random l_random = new Random();
            this.d_cardsOwned.add(GameConstants.CARDS.get(l_random.nextInt(GameConstants.SIZE)));
            this.setD_playerLog("Player: " + this.d_name + " has obtained a card as a reward for a triumphant conquest- " + this.d_cardsOwned.get(this.d_cardsOwned.size()-1), "log");
            this.setD_oneCardPerTurn(true);
        }else{
            this.setD_playerLog("Player: " + this.d_name + " has reached the maximum allowable card acquisition limit for a single turn", "error");
        }
    }

    /**
     * Remove the card which is used.
     *
     * @param p_cardName name of the card to remove.
     */
    public void removeCard(String p_cardName){
        this.d_cardsOwned.remove(p_cardName);
    }

    /**
     * This method checks if the order issued on country is possible or not.
     *
     * @param p_nameOfTargetCountry The country to attack
     * @return The boolean value if it can attack
     */
    public boolean negotiationValidation(String p_nameOfTargetCountry){
        boolean l_canAttack = true;
        for(Player p: d_negotiatedWith){
            if(p.getNamesOfCountries().contains(p_nameOfTargetCountry))
                l_canAttack = false;
        }
        return l_canAttack;
    }

    /**
     * Method to validate if the source and target countries given in advance order
     * exists in the map or not.
     *
     * @param p_NameOfCountry country name to be verified on map
     * @param p_gameState   current GameState object
     * @return boolean tru if country exists
     */
    private Boolean validateIfCountryExists(String p_NameOfCountry, GameState p_gameState) {
        if (p_gameState.getD_map().getCountryByName(p_NameOfCountry) == null) {
            this.setD_playerLog("This Country : " + p_NameOfCountry
                    + " chosen for advance order doesn't exists on the map. Order Rejected !!", "error");
            return false;
        }
        return true;
    }

    /**
     * Method to validate if the given advance order has no armies to move
     *
     * @param p_noOfArmies number of armies
     * @return true if given order has no armies
     */
    private Boolean validateNoArmiesInOrder(String p_noOfArmies) {
        if (Integer.parseInt(p_noOfArmies) == 0) {
            this.setD_playerLog("Advance order with 0 armies to move can't be made", "error");
            return true;
        }
        return false;
    }

    /**
     * Checks if countries given advance order are adjacent or not.
     *
     * @param p_gameState         current state of the game
     * @param p_sourceCountryName source country name
     * @param p_targetCountryName target country name
     * @return boolean true if countries are adjacent or else false
     */
    @SuppressWarnings("unlikely-argument-type")
    public boolean validateAdjacency(GameState p_gameState, String p_sourceCountryName, String p_targetCountryName) {
        Country l_sourceCountry = p_gameState.getD_map().getCountryByName(p_sourceCountryName);
        Country l_targetCountry = p_gameState.getD_map().getCountryByName(p_targetCountryName);
        Integer l_targetCountryId = l_sourceCountry. getD_neighborCountryIDs().stream()
                .filter(l_adjCountry -> l_adjCountry == l_targetCountry.getD_countryID()).findFirst().orElse(null);
        if (l_targetCountryId == null) {
            this.setD_playerLog("Not able to issue Advance Order since target country : " + p_targetCountryName
                    + " is not adjacent to source country : " + p_sourceCountryName, "error");
            return false;
        }
        return true;
    }

    /**
     * Method to create advance orders for the entered commands
     *
     * @param p_enteredCommand command entered by user on CLI
     * @param p_gameState      current game state object
     */
    public void createAdvanceOrder(String p_enteredCommand, GameState p_gameState) {
        try {
            if (p_enteredCommand.split(" ").length == 4) {
                String l_sourceCountry = p_enteredCommand.split(" ")[1];
                String l_targetCountry = p_enteredCommand.split(" ")[2];
                String l_noOfArmies = p_enteredCommand.split(" ")[3];
                if (this.validateIfCountryExists(l_sourceCountry, p_gameState)
                        && this.validateIfCountryExists(l_targetCountry, p_gameState)
                        && !validateNoArmiesInOrder(l_noOfArmies)
                        && validateAdjacency(p_gameState, l_sourceCountry, l_targetCountry)) {
                    this.d_orderedPlayerList
                            .add(new Advance(this, l_sourceCountry, l_targetCountry, Integer.parseInt(l_noOfArmies)));
                    this.setD_playerLog("Advance order is added to the queue for execution. For player: " + this.d_name, "log");
                }
            } else {
                this.setD_playerLog("Arguments Passed For Advance Order are not valid !!", "error");
            }

        } catch (Exception l_e) {
            this.setD_playerLog("Advanced order given is not valid !!", "error");
        }
    }

    /**
     * Method to verify number of armies entered in deploy command to validate that
     * Player cannot deploy more armies exceeding their reinforcement pool.
     *
     * @param p_player     player creating the deploy order
     * @param p_noOfArmies number of armies
     * @return boolean to validate armies to deploy
     */
    public boolean verifyDeployOrderArmies(Player p_player, String p_noOfArmies) {
        return p_player.getD_noOfAllocatedArmies() < Integer.parseInt(p_noOfArmies) ? true : false;
    }

    /**
     * Method to create the deployment order on the commands entered on CLI by player
     *
     * @param p_enteredCommand command entered on CLI
     */
    public void deployOrder(String p_enteredCommand){
        try {
          String  l_targetCountry = p_enteredCommand.split(" ")[1];
          String  l_noOfArmies = p_enteredCommand.split(" ")[2];
            if (verifyDeployOrderArmies(this, l_noOfArmies)) {
                this.setD_playerLog(
                        "Given deploy order cant be executed as armies in deploy order exceeds player's unallocated armies.", "error");
            } else {
                this.d_orderedPlayerList.add(new Deploy(this, l_targetCountry, Integer.parseInt(l_noOfArmies)));
                Integer l_armies = this.getD_noOfAllocatedArmies() - Integer.parseInt(l_noOfArmies);
                this.setD_noOfAllocatedArmies(l_armies);
                this.setD_playerLog("Deploy order is added to the queue for execution. For player: " + this.d_name, "log");

            }
        } catch (Exception l_e) {
            this.setD_playerLog("Invalid Deploy Order !!", "error");
        }

    }

    /**
     * Method to validate card command parameters
     *
     * @param p_enteredCommand a card command
     * @return boolean value if valid parameter
     */
    public boolean verifyCardParameters(String p_enteredCommand){
        if(p_enteredCommand.split(" ")[0].equalsIgnoreCase("airlift")) {
            return p_enteredCommand.split(" ").length == 4;
        } else if (p_enteredCommand.split(" ")[0].equalsIgnoreCase("blockade")
                || p_enteredCommand.split(" ")[0].equalsIgnoreCase("bomb")
                || p_enteredCommand.split(" ")[0].equalsIgnoreCase("negotiate")) {
            return p_enteredCommand.split(" ").length == 2;
        } else {
            return false;
        }
    }

    /**
     * Method to handle card commands by creating and adding orders to the queue
     * @param p_enteredCommand : command entered by user on CLI
     * @param p_gameState : GameState object
     */
    public void cardCommandsHandler(String p_enteredCommand, GameState p_gameState) {
        if (verifyCardParameters(p_enteredCommand)) {
            switch (p_enteredCommand.split(" ")[0]) {
                case "blockade":
                    Card l_blockade = new Blockade(this, p_enteredCommand.split(" ")[1]);
                    if (l_blockade.validateOrder(p_gameState)) {
                        this.d_orderedPlayerList.add(l_blockade);
                        this.setD_playerLog("Blockade card command is added to queue successfully..", "log");
                        p_gameState.logUpdate(getD_playerLog(), "effect");
                    }
                    break;
                case "airlift":
                    Card l_airlift = new Airlift(p_enteredCommand.split(" ")[1], p_enteredCommand.split(" ")[2],
                            Integer.parseInt(p_enteredCommand.split(" ")[3]), this);
                    if (l_airlift.validateOrder(p_gameState)) {
                        this.d_orderedPlayerList.add(l_airlift);
                        this.setD_playerLog("Airlift card command is added to queue successfully..", "log");
                        p_gameState.logUpdate(getD_playerLog(), "effect");
                    }
                    break;
                case "negotiate":
                    Card l_negotiate = new Diplomacy(p_enteredCommand.split(" ")[1],this);
                    if (l_negotiate.validateOrder(p_gameState)) {
                        this.d_orderedPlayerList.add(l_negotiate);
                        this.setD_playerLog("Negotiate card command is added to queue successfully..", "log");
                        p_gameState.logUpdate(getD_playerLog(), "effect");
                    }
                    break;
                case "bomb":
                    Card l_bomb = new Bomb(this, p_enteredCommand.split(" ")[1]);
                    if (l_bomb.validateOrder(p_gameState)) {
                        this.d_orderedPlayerList.add(l_bomb);
                        this.setD_playerLog("Bomb card command is added to queue successfully..", "log");
                        p_gameState.logUpdate(getD_playerLog(), "effect");
                    }
                    break;

                default:
                    this.setD_playerLog(" Command is Invalid !!", "error");
                    p_gameState.logUpdate(getD_playerLog(), "effect");
                    break;
            }
        } else{
            this.setD_playerLog("Invalid Card Commands entered. Please enter a valid command", "error");
        }
    }
}
