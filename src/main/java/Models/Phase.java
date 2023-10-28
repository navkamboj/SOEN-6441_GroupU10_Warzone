package Models;

import Controller.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.MapService;
import Services.PlayerService;
import Utils.Command;

import java.io.IOException;

/**
 * This abstract class declares abstract methods for each game phase
 */
public abstract class Phase {
    /**
     * d_gameState stores the information about current GameState.
     */
    GameState d_gameState;

    /**
     * d_gameEngine stores the information about current Gameplay.
     */
    GameEngine d_gameEngine;

    /**
     * Creating object of PlayerService to issue orders and edit players
     */
    PlayerService d_playerService = new PlayerService();

    /**
     * Creating object of MapService to handle load, read, parse, edit, and save map functionalities.
     */
    MapService d_mapService = new MapService();

    /**
     * Flag to check if map is loaded
     */
    boolean l_isMapLoaded;

    /**
     * Constructor of class to initialize current game engine value
     * @param p_gameEngine instant to update state
     * @param p_gameState instant for game state
     */
    public Phase(GameEngine p_gameEngine, GameState p_gameState){
        d_gameEngine = p_gameEngine;
        d_gameState = p_gameState;
    }

    /**
     * Getter method to retrieve the current game state
     * @return d_gameState : the current game state
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * Setter method to set the current game state
     * @param p_gameState
     */
    public void setD_gameState(GameState p_gameState) {
        d_gameState = p_gameState;
    }

    /**
     * Method to handle all the commands related to manipulation of state
     * @param p_enterCommand refers to command entered by the user in CLI
     * @param p_player refers to the player object
     * @throws InvalidMap exception to handle invalid map
     * @throws InvalidCommand exception to handle invalid command
     * @throws IOException exception to handle invalid I/O
     */
    public void handleCommand(String p_enterCommand, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_enterCommand, p_player);
    }

    /**
     * Method to handle all the commands related to manipulation of state
     * @param p_enterCommand refers to command entered by the user in CLI
     * @throws InvalidMap exception to handle invalid map
     * @throws InvalidCommand exception to handle invalid command
     * @throws IOException exception to handle invalid I/O
     */
    public void handleCommand(String p_enterCommand) throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_enterCommand, null);
    }

    /**
     * Declaration of function to check <strong>editmap</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player instance of Player Object
     * @throws IOException exception to handle invalid I/O
     * @throws InvalidMap exception to handle invalid map
     * @throws InvalidCommand exception to handle invalid command
     */
    protected abstract void doEditMap(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap;

    /**
     * Declaration of function to check <strong>editcontinent</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player instance of Player Object
     * @throws IOException    exception to handle invalid I/O
     * @throws InvalidCommand exception to handle invalid command
     * @throws InvalidMap     exception to handle invalid map
     */
    protected abstract void doEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap;

    private void commandHandler(String p_enteredCommand, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        Command l_command = new Command(p_enteredCommand);
        String l_baseCommand = l_command.getBaseCommand();
        l_isMapLoaded = d_gameState.getD_map() != null;


        switch (l_baseCommand) {
            case "editmap": {
                doEditMap(l_command, p_player);
                break;
            }
            case "editcontinent": {
                doEditContinent(l_command, p_player);
                break;
            }

        }
    }



}
