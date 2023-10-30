package Models;

import java.util.List;
import Models.LogEntryBuffer;

/**
 * Model class to manage the GameState functions
 *
 * @author Navjot Kamboj, Yatish Chutani
 * @version 1.0.0
 */
public class GameState {
    /**
     * List of players
     */
    List<Player> d_playerList;

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
}
