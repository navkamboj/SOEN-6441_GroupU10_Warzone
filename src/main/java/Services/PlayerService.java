package Services;

import Models.Player;
import Models.Country;
import Models.GameState;
import Models.Continent;

import Constants.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Utils.CommonUtil;

/**
 * This service is used for handle the players.
 *
 * @author Nihal Galani
 * @version 1.0.0
 */

public class PlayerService {

    /**
     * Country Assignment Log.
     */
    String d_assignmentLog = "Country/Continent Assignment:";

    /**
     * The Log of Player operations present in the player methods.
     */
    String d_logPlayer;

    /**
     * Method is used to check if a player name is exist or not.
     *
     * @param p_allExistingPlayersList list of existing player
     * @param p_playerName             name on which operation needs to be performed
     * @return boolean true if player name is unique otherwise return false if its not unique
     */
    public boolean checkPlayerNameUniqueness(List<Player> p_allExistingPlayersList, String p_playerName) {
        boolean l_checkunique = true;
        if (!CommonUtil.isEmptyCollection(p_allExistingPlayersList)) {
            for (Player l_player : p_allExistingPlayersList) {
                if (l_player.getD_playerName().equalsIgnoreCase(p_playerName)) {
                    l_checkunique = false;
                    break;
                }
            }
        }
        return l_checkunique;
    }

    /**
     * Method is used for adding and removing the player.
     *
     * @param p_allExistingPlayersList list of existing player
     * @param p_operationTask          operation which should be performed either add or remove
     * @param p_argumentTask           name of player which should be remove or add
     * @return updated playerlist will be returned from this method
     */
    public List<Player> addingRemovingPlayers(List<Player> p_allExistingPlayersList, String p_operationTask, String p_argumentTask) {
        List<Player> l_updatePlayers = new ArrayList<>();
        if (!CommonUtil.isEmptyCollection(p_allExistingPlayersList))
            l_updatePlayers.addAll(p_allExistingPlayersList);

        String l_newPlayerName = p_argumentTask.split(" ")[0];
        boolean l_playerNameExist = !checkPlayerNameUniqueness(p_allExistingPlayersList, l_newPlayerName);

        switch (p_operationTask.toLowerCase()) {
            case "remove":
                playerRemoving(p_allExistingPlayersList, l_updatePlayers, l_newPlayerName, l_playerNameExist);
                break;
            case "add":
                insertGamePlayer(l_updatePlayers, l_newPlayerName, l_playerNameExist);
                break;
            default:
                setD_logPlayer("Operation is not valid on player list");
        }
        return l_updatePlayers;
    }

    /**
     * Process of removing the player if it exists
     *
     * @param p_allExistingPlayersList list of existing player
     * @param p_updatePlayers          updated list of player with removal of player name
     * @param p_newPlayerName          the player name which should be remove
     * @param p_playerNameExist        if player already its value will be true otherwise its value will be false
     */
    private void playerRemoving(List<Player> p_allExistingPlayersList, List<Player> p_updatePlayers, String p_newPlayerName,
                                boolean p_playerNameExist) {
        if (p_playerNameExist) {
            for (Player l_player : p_allExistingPlayersList) {
                if (l_player.getD_playerName().equalsIgnoreCase(p_newPlayerName)) {
                    p_updatePlayers.remove(l_player);
                    setD_logPlayer("Player: " + p_newPlayerName + " has been removed successfully.");
                }
            }
        } else {
            setD_logPlayer("Player: " + p_newPlayerName + " does not Exist.");
        }
    }

    /**
     * Process of removing the player if it exists
     *
     * @param p_updatePlayers   updated list of player with removal of player name
     * @param p_newPlayerName   the player name which should be remove
     * @param p_playerNameExist if player already its value will be true otherwise its value will be false
     */
    private void insertGamePlayer(List<Player> p_updatePlayers, String p_newPlayerName,
                                  boolean p_playerNameExist) {
        if (p_playerNameExist) {
            setD_logPlayer("Player: " + p_newPlayerName + " already Exists.");
        } else {
            Player l_addPlayer = new Player(p_newPlayerName);
            p_updatePlayers.add(l_addPlayer);
            setD_logPlayer("Player: " + p_newPlayerName + " has been added successfully.");
        }
    }

    /**
     * Check whether playerslist is available or not
     *
     * @param p_gameState current game state
     * @return boolean returns true if playerslist is available otherwise it will return false
     */
    public boolean checkAvailabilityPlayerList(GameState p_gameState) {
        if (p_gameState.getD_playerList() == null || p_gameState.getD_playerList().isEmpty()) {
            System.out.println("Please add some players to playerlist");
            return false;
        }
        return true;
    }

    /**
     * Assign different countries to individual player
     *
     * @param p_gameState current game state
     */
    public boolean countryAssign(GameState p_gameState) {
        if (!checkAvailabilityPlayerList(p_gameState)){
            p_gameState.logUpdate("Countries can only be assigned once players are added.", "effect");
        return false;
    }
        List<Country> l_countrylist = p_gameState.getD_map().getD_countries();
        int l_playerListSize = p_gameState.getD_playerList().size();
        Player l_neutralPlayer = p_gameState.getD_playerList().stream()
                .filter(l_player -> l_player.getD_playerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);
        if (l_neutralPlayer != null)
            l_playerListSize = l_playerListSize - 1;

        int l_countriesForPlayer = Math.floorDiv(l_countrylist.size(), l_playerListSize);

        this.assignmentOfRandomCountries(l_countriesForPlayer, l_countrylist, p_gameState.getD_playerList(), p_gameState);
        this.assignmentOfContinents(p_gameState.getD_playerList(), p_gameState.getD_map().getD_continents());

        p_gameState.logUpdate(d_assignmentLog, "effect");
        System.out.println("Countries have been assigned to all individual players");
        return true;
    }

    /**
     * Give different colors to different players
     *
     * @param p_gameState Current Game State
     */
    public void colorAssign(GameState p_gameState) {
        if (!checkAvailabilityPlayerList(p_gameState))
            return;

        List<Player> l_players = p_gameState.getD_playerList();

        for (int i = 0; i < l_players.size(); i++) {
            l_players.get(i).setD_color(GameConstants.COLORS_LIST.get(i));
        }
    }

    /**
     * Checks if player is having any continent as a result of random country
     * assignment.
     *
     * @param p_playerList players list
     * @param p_continents continents list
     */
    public void assignmentOfContinents(List<Player> p_playerList, List<Continent> p_continents) {
        for (Player l_player : p_playerList) {
            List<String> l_ownedCountries = new ArrayList<>();
            if (!CommonUtil.isEmptyCollection(l_player.getD_ownedCountries())) {
                l_player.getD_ownedCountries().forEach(l_country -> l_ownedCountries.add(l_country.getD_countryName()));

                for (Continent l_continent : p_continents) {
                    List<String> l_continentCountries = new ArrayList<>();
                    l_continent.getD_countries().forEach(l_country -> l_continentCountries.add(l_country.getD_countryName()));
                    if (l_ownedCountries.containsAll(l_continentCountries)) {
                        if (l_player.getD_ownedContinents() == null)
                            l_player.setD_ownedContinents(new ArrayList<>());

                        l_player.getD_ownedContinents().add(l_continent);
                        System.out.println("Player : " + l_player.getD_playerName() + " is assigned continent : "
                                + l_continent.getD_continentName());
                        d_assignmentLog += "\n Player : " + l_player.getD_playerName() + " is assigned with continent : "
                                + l_continent.getD_continentName();
                    }
                }
            }
        }
    }

    /**
     * Assign Random countries to players
     *
     * @param p_countriesForPlayer number of countries which should be assigned to each player
     * @param p_countryList        all countries that are there in the map
     * @param p_playerList         players list
     */
    private void assignmentOfRandomCountries(int p_countriesForPlayer, List<Country> p_countryList,
                                             List<Player> p_playerList, GameState p_gameState) {
        List<Country> l_notAssignedCountries = new ArrayList<>(p_countryList);
        for (Player l_player : p_playerList) {
            if (!l_player.getD_playerName().equalsIgnoreCase("Neutral")) {
                if (l_notAssignedCountries.isEmpty())
                    break;

                for (int i = 0; i < p_countriesForPlayer; i++) {
                    Random l_random = new Random();
                    int l_indexRandom = l_random.nextInt(l_notAssignedCountries.size());
                    Country l_countryListRandom = l_notAssignedCountries.get(l_indexRandom);

                    if (l_player.getD_ownedCountries() == null)
                        l_player.setD_ownedCountries(new ArrayList<>());

                    l_player.getD_ownedCountries().add(p_gameState.getD_map().getCountryByName(l_countryListRandom.getD_countryName()));
                    System.out.println("Player : " + l_player.getD_playerName() + " is assigned  : "
                            + l_countryListRandom.getD_countryName());
                    d_assignmentLog += "\n Player : " + l_player.getD_playerName() + " is assigned with country : "
                            + l_countryListRandom.getD_countryName();
                    l_notAssignedCountries.remove(l_countryListRandom);
                }
            }
        }
        // If some countries are left in the list then it will be assigned to players
        if (!l_notAssignedCountries.isEmpty()) {
            assignmentOfRandomCountries(1, l_notAssignedCountries, p_playerList, p_gameState);
        }
    }

    /**
     * This method count total number of armies for Countries and continents that are owned by player
     *
     * @param p_player armies will be calculated for this player
     * @return Integer total armies that is allocated to player
     */
    public int armiesCountForPlayer(Player p_player) {
        int l_armies = 0;
        if (!CommonUtil.isEmptyCollection(p_player.getD_ownedCountries())) {
            l_armies = Math.max(3, Math.round((p_player.getD_ownedCountries().size()) / 3));
        }
        if (!CommonUtil.isEmptyCollection(p_player.getD_ownedContinents())) {
            int l_valueOfContinents = 0;
            for (Continent l_continent : p_player.getD_ownedContinents()) {
                l_valueOfContinents = l_valueOfContinents + l_continent.getD_continentValue();
            }
            l_armies = l_armies + l_valueOfContinents;
        }
        return l_armies;
    }

    /**
     * Armies assigns to every player
     *
     * @param p_gameState current game state
     */
    public void armiesAssign(GameState p_gameState) {
        for (Player l_player : p_gameState.getD_playerList()) {
            Integer l_armies = this.armiesCountForPlayer(l_player);
            this.setD_logPlayer("Player : " + l_player.getD_playerName() + " has assigned " + l_armies + " armies");
            p_gameState.logUpdate(this.d_logPlayer, "effect");

            l_player.setD_noOfAllocatedArmies(l_armies);
        }
    }

    /**
     * This method is called by controller for updation of gamestate and adding of any player
     *
     * @param p_gameState     current game state
     * @param p_operationTask operation which should be performed either add or remove
     * @param p_argumentTask  name of player which should be remove or add
     */
    public void playerListUpdation(GameState p_gameState, String p_operationTask, String p_argumentTask) {
        if (!mapLoadedOrNot(p_gameState)) {
            this.setD_logPlayer("Load the map before adding or removing any player  " + p_argumentTask);
            p_gameState.logUpdate(this.d_logPlayer, "effect");
            return;
        }
        List<Player> l_playersListUpdation = this.addingRemovingPlayers(p_gameState.getD_playerList(), p_operationTask, p_argumentTask);

        if (!CommonUtil.isNull(l_playersListUpdation)) {
            p_gameState.setD_playerList(l_playersListUpdation);
            p_gameState.logUpdate(d_logPlayer, "effect");
        }

    }

    /**
     * This method is used for checking whether map is loaded or not
     *
     * @param p_gameState current game state
     * @return boolean map returns true if map is loaded otherwise it will returns false
     */
    public boolean mapLoadedOrNot(GameState p_gameState) {
        if (CommonUtil.isNull(p_gameState.getD_map()))
            return false;
        else
            return true;
    }

    /**
     * This method checks if any players have unassigned armies or not
     *
     * @param p_playersList list of players
     * @return boolean true if any players has unassigned armies otherwise it will returns false
     */
    public boolean existanceOfUnassignedArmies(List<Player> p_playersList) {
        int l_armyUnassigned = 0;
        for (Player l_player : p_playersList) {
            l_armyUnassigned = l_armyUnassigned + l_player.getD_noOfAllocatedArmies();
        }
        return l_armyUnassigned != 0;
    }

    /**
     * This method checks existance of unexecutedorders
     *
     * @param p_playersList list of players
     * @return boolean true returns true if unexecuted orders exists with any of the players otherwise it will return false
     */
    public boolean existanceOfUnexecutedOrder(List<Player> p_playersList) {
        int l_differentunexecutedOrder = 0;
        for (Player l_player : p_playersList) {
            l_differentunexecutedOrder = l_differentunexecutedOrder + l_player.getD_executeOrders().size();
        }
        return l_differentunexecutedOrder != 0;
    }

    /**
     * Resets each players information for accepting further orders.
     *
     * @param p_playersList players involved in game
     */
    public void resetPlayerFlag(List<Player> p_playersList) {
        for (Player l_player : p_playersList) {
            if (!l_player.getD_playerName().equalsIgnoreCase("Neutral"))
                l_player.setD_moreOrders(true);
            l_player.setD_oneCardPerTurn(false);
            l_player.clearNegotiation();
        }
    }

    /**
     * Setter method for the Player Log in player methods.
     *
     * @param p_logPlayer The Player Operation Log.
     */
    public void setD_logPlayer(String p_logPlayer){
        this.d_logPlayer = p_logPlayer;
        System.out.println(p_logPlayer);
    }

    /**
     * Verify if the player wants to give further order.
     *
     * @param p_playersList Players in game
     * @return boolean if there are more orders to execute
     */
    public boolean checkForMoreOrders(List<Player> p_playersList) {
        for (Player l_player : p_playersList) {
            if (l_player.getD_moreOrders())
                return true;
        }
        return false;
    }

    /**
     * This method finds the player by name.
     *
     * @param p_playerName The player name to be found
     * @param p_gameState The GameState Instance.
     * @return p_player object
     */
    public Player findPlayerByName(String p_playerName, GameState p_gameState){
        return p_gameState.getD_playerList().stream().filter(l_player -> l_player.getD_playerName().equals(p_playerName)).findFirst().orElse(null);
    }
}
