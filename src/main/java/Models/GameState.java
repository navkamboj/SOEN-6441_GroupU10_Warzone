package Models;

import java.util.ArrayList;
import java.util.List;
import Models.LogEntryBuffer;

/**
 * Model class to manage the GameState functions
 *
 * @author Navjot Kamboj, Yatish Chutani
 * @version 2.0.0
 */
public class GameState {

    /**
     * List of players
     */
    List<Player> d_playerList;

    /**
     * Recording Game State as Log Entries.
     */
    LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();

    /**
     * Data member to store Map object
     */
    Map d_map;

    /**
     * Error message string
     */
    String d_errorMessage;

    /**
     * Checks if player has used the load command
     */
    Boolean d_checkLoadCommand = false;

    /**
     * Count of turns in tournament.
     */
    int d_maxCountOfTurns = 0;

    /**
     * Count of remaining turns in tournament.
     */
    int d_countOfRemainingTurns = 0;

    /**
     * Maintains a list of players lost in the game.
     */
    List<Player> d_listOfPlayersFailed = new ArrayList<Player>();

    /**
     * Winner Player.
     */
    Player d_winningPlayer;

    /**
     * Getter method to retrieve error message string
     * @return error message
     */
    public String getD_errorMessage() {
        return d_errorMessage;
    }

    /**
     * Setter method to set the error message string
     * @param p_errorMessage
     */
    public void setD_errorMessage(String p_errorMessage) {
        this.d_errorMessage = p_errorMessage;
    }

    /**
     * Getter method to retrieve the map object
     * @return Map object
     */
    public Map getD_map() {
        return d_map;
    }

    /**
     * Setter method to set the map object
     * @param p_map
     */
    public void setD_map(Map p_map) {
        this.d_map = p_map;
    }

    /**
     * Getter method to retrieve the list of players
     * @return List of players
     */
    public List<Player> getD_playerList() {
        return d_playerList;
    }

    /**
     * Setter method to set the players
     * @param p_playerList
     */
    public void setD_playerList(List<Player> p_playerList) {
        this.d_playerList = p_playerList;
    }

    /**
     * It consists of the message to be added in the log.
     *
     * @param p_messageLog Set the Log Message within the Object.
     * @param p_logType Type of Log Message that is to be Added
     */
    public void logUpdate(String p_messageLog, String p_logType){
        d_logEntryBuffer.currentLog(p_messageLog, p_logType);
    }

    /**
     * This getter method fetches the latest Log in current GameState.
     *
     * @return The recent Log Message
     */
    public String getLatestLog(){
        return d_logEntryBuffer.getD_messageLog();
    }

    /**
     * Setter method to set the Boolean load map variable.
     */
    public void setD_checkLoadCommand() {
        this.d_checkLoadCommand = true;
    }

    /**
     * Getter method to check if the load command is used.
     *
     * @return boolean value
     */
    public boolean getD_checkLoadCommand(){
        return this.d_checkLoadCommand;
    }

    /**
     * Returns max number of turns allowed in tournament.
     *
     * @return int number of turns
     */
    public int getD_maxCountOfTurns() {

        return d_maxCountOfTurns;
    }

    /**
     * Sets max number of turns allowed in tournament.
     *
     * @param d_maxCountOfTurns number of turns
     */
    public void setD_maxCountOfTurns(int d_maxCountOfTurns) {
        this.d_maxCountOfTurns = d_maxCountOfTurns;
    }

    /**
     * Gets count of turns left at any stage of tournament.
     *
     * @return int number of remaining turns
     */
    public int getD_countOfRemainingTurns() {
        return d_countOfRemainingTurns;
    }

    /**
     * Sets count of turns left at any stage of tournament.
     *
     * @param d_countOfRemainingTurns number of remaining turns
     */
    public void setD_countOfRemainingTurns(int d_countOfRemainingTurns) {
        this.d_countOfRemainingTurns = d_countOfRemainingTurns;
    }

    /**
     * Adds the Failed Player in GameState.
     *
     * @param p_player player instance to remove
     */
    public void removePlayer(Player p_player){
        d_listOfPlayersFailed.add(p_player);
    }

    /**
     * Retrieves the list of players failed.
     *
     * @return List of Players that lost game.
     */
    public List<Player> getD_listOfPlayersFailed() {
        return d_listOfPlayersFailed;
    }

    /**
     * Sets the winning player object.
     *
     * @param p_player winner player object
     */
    public void setD_winningPlayer(Player p_player){
        d_winningPlayer = p_player;
    }

    /**
     * Returns the winning player object.
     *
     * @return returns winning player
     */
    public Player getD_winningPlayer(){
        return d_winningPlayer;
    }

}
