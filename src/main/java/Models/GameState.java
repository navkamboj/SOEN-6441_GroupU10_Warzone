package Models;

import java.util.List;

/**
 * Model class to manage the GameState functions
 *
 * @author Navjot Kamboj
 * @version 1.0.0
 */
public class GameState {
    /**
     * List of players
     */
    List<Player> d_playerList;

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
}
