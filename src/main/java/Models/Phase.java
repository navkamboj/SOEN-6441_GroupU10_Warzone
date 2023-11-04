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
 *
 * @author Navjot Kamboj, Yatish Chutani
 * @version 2.0.0
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
     *
     * @param p_gameEngine instant to update state
     * @param p_gameState  instant for game state
     */
    public Phase(GameEngine p_gameEngine, GameState p_gameState) {
        d_gameEngine = p_gameEngine;
        d_gameState = p_gameState;
    }

    /**
     * Getter method to retrieve the current game state
     *
     * @return d_gameState : the current game state
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * Setter method to set the current game state
     *
     * @param p_gameState
     */
    public void setD_gameState(GameState p_gameState) {
        d_gameState = p_gameState;
    }

    /**
     * Method to handle all the commands related to manipulation of state
     *
     * @param p_enterCommand refers to command entered by the user in CLI
     * @param p_player       refers to the player object
     * @throws InvalidMap     exception to handle invalid map
     * @throws InvalidCommand exception to handle invalid command
     * @throws IOException    exception to handle invalid I/O
     */
    public void handleCommand(String p_enterCommand, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_enterCommand, p_player);
    }

    /**
     * Method to handle all the commands related to manipulation of state
     *
     * @param p_enterCommand refers to command entered by the user in CLI
     * @throws InvalidMap     exception to handle invalid map
     * @throws InvalidCommand exception to handle invalid command
     * @throws IOException    exception to handle invalid I/O
     */
    public void handleCommand(String p_enterCommand) throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_enterCommand, null);
    }

    /**
     * Declaration of function to check <strong>editmap</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws IOException    exception to handle invalid I/O
     * @throws InvalidMap     exception to handle invalid map
     * @throws InvalidCommand exception to handle invalid command
     */
    protected abstract void doEditMap(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap;

    /**
     * Declaration of function to check <strong>editcontinent</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws IOException    exception to handle invalid I/O
     * @throws InvalidCommand exception to handle invalid command
     * @throws InvalidMap     exception to handle invalid map
     */
    protected abstract void doEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap;

    /**
     * Declaration of function to check <strong>loadmap</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     exception to handle invalid map
     * @throws InvalidCommand exception to handle invalid command
     * @throws IOException    exception to handle invalid I/O
     */
    protected abstract void doLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException;

    /**
     * Declaration of function to check <strong>savemap</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidMap     exception to handle invalid map
     * @throws InvalidCommand exception to handle invalid command
     * @throws IOException    exception to handle invalid I/O
     */
    protected abstract void doSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException;

    /**
     * Declaration of function to check <strong>validatemap</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidCommand exception to handle invalid command
     * @throws InvalidMap     exception to handle invalid map
     * @throws IOException    exception to handle invalid I/O
     */
    protected abstract void doValidateMap(Command p_command, Player p_player) throws InvalidMap, InvalidCommand, IOException;

    /**
     * Declaration of function to check <strong>showmap</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of player object
     * @throws InvalidCommand exception to handle invalid command
     * @throws InvalidMap     exception to handle invalid map
     * @throws IOException    exception to handle invalid I/O
     */
    protected abstract void doShowMap(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap;

    /**
     * Declaration of function to check <strong>editcountry</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidCommand exception to handle invalid command
     * @throws InvalidMap     exception to handle invalid map
     * @throws IOException    exception to handle invalid I/O
     */
    protected abstract void doEditCountry(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException;

    /**
     * Declaration of function to check <strong>editneighbor</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidCommand exception to handle invalid command
     * @throws InvalidMap     exception to handle invalid map
     * @throws IOException    exception to handle invalid I/O
     */
    protected abstract void doEditNeighbour(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException;

    /**
     * Declaration of function to check <strong>assigncountries</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidCommand exception to handle invalid command
     * @throws InvalidMap     exception to handle invalid map
     * @throws IOException    exception to handle invalid I/O
     */
    protected abstract void doAssignCountries(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap;

    /**
     * Declaration of function to check <strong>gameplayer</strong> command
     * Redirects control to model for actual processing.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of Player Object
     * @throws InvalidCommand exception to handle invalid command
     * @throws InvalidMap     exception to handle invalid map
     * @throws IOException    exception to handle invalid I/O
     */
    protected abstract void createGamePlayers(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap;

    /**
     * This method handles the <strong>deploy</strong> order in gameplay.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of player object
     * @throws IOException exception to handle invalid I/O
     */
    protected abstract void doCreateDeploy(String p_command, Player p_player) throws IOException;

    /**
     * Method to handle the <strong>advance order</strong> in gameplay.
     *
     * @param p_command command entered on CLI
     * @param p_player  instance of player object
     * @throws IOException exception to handle invalid I/O
     */
    protected abstract void doAdvanceOrder(String p_command, Player p_player) throws IOException;

    /**
     * Method to handles the <strong>Card</strong> commands such as:
     * <ul>
     * <li>Airlift</li>
     * <li>Blockade</li>
     * <li>Negotiate</li>
     * <li>Bomb</li>
     * </ul>
     *
     * @param p_enteredCommand String of entered Command
     * @param p_player         player instance
     * @throws IOException Io exception
     */
    protected abstract void doCardHandle(String p_enteredCommand, Player p_player) throws IOException;

    /**
     * This is the main method executed on phase change.
     */
    public abstract void initPhase();

    /**
     * This method is used to Log and Print if the command cannot be executed in current phase.
     */
    public void outputStateInvalidCommand(){
        d_gameEngine.setD_logGameEngine("Invalid command for the Current State", "effect");
    }

    private void commandHandler(String p_enteredCommand, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        Command l_command = new Command(p_enteredCommand);
        String l_baseCommand = l_command.getBaseCommand();
        l_isMapLoaded = d_gameState.getD_map() != null;
        d_gameState.logUpdate(l_command.getD_command(), "command");

        switch (l_baseCommand) {
            case "loadmap": {
                doLoadMap(l_command, p_player);
                break;
            }
            case "editmap": {
                doEditMap(l_command, p_player);
                break;
            }
            case "savemap": {
                doSaveMap(l_command, p_player);
                break;
            }
            case "validatemap": {
                doValidateMap(l_command, p_player);
                break;
            }
            case "showmap": {
                doShowMap(l_command, p_player);
                break;
            }
            case "editcountry": {
                doEditCountry(l_command, p_player);
                break;
            }
            case "editcontinent": {
                doEditContinent(l_command, p_player);
                break;
            }
            case "editneighbor": {
                doEditNeighbour(l_command, p_player);
                break;
            }
            case "gameplayer": {
                createGamePlayers(l_command, p_player);
                break;
            }
            case "assigncountries": {
                doAssignCountries(l_command, p_player);
                break;
            }
            case "deploy": {
                doCreateDeploy(p_enteredCommand, p_player);
                break;
            }
            case "advance": {
                doAdvanceOrder(p_enteredCommand, p_player);
                break;
            }
            case "negotiate":
            case "airlift":
            case "blockade":
            case "bomb": {
                doCardHandle(p_enteredCommand, p_player);
                break;
            }
            case "help":{
                d_gameEngine.setD_logGameEngine(
                        "---------- List of user map creation commands from console ---------- \n" +
                                "To add or remove a continent : editcontinent -add continentID continentvalue -remove continentID \n" +
                                "To add or remove a country : editcountry -add countryID continentID -remove countryID \n" +
                                "To add or remove a neighbor to a country : editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID \n\n" +
                                "---------- Map Commands(Edit/Save) ----------- \n" +"To edit map: editmap filename \n" +"To save map: savemap filename \n" +
                                "To show the map: showmap \n" +"To validate map: validatemap \n\n" +"---------- Game Startup Commands ----------- \n"+
                                "To load the map: loadmap filename \n " +
                                "To add the players: gameplayer -add playername -remove playername \n" +"To assign countries to the player: assigncountries \n\n"+
                                "---------- Order Creation Commands ----------\n"+"Deploy order command: deploy countryID numarmies \n" +"Advance order command: advance countrynamefrom countynameto numarmies \n"+
                                "Bomb order command (requires bomb card): bomb countryID \n"+"Blockade order command (required blockade card): blockade countryID \n" +
                                "Airlift order command (requires the airlift card): airlift sourcecountryID targetcountryID numarmies \n" +
                                "Diplomacy order command (requires the diplomacy card): negotiate playerID \n"
                        ,"effect");
                break;
            }
            case "exit": {
                d_gameEngine.setD_logGameEngine("Exit Command Entered, Game Ends!", "effect");
                System.exit(0);
                break;
            }
            default: {
                d_gameEngine.setD_logGameEngine("Invalid Command", "effect");
                break;
            }
        }
    }
}
