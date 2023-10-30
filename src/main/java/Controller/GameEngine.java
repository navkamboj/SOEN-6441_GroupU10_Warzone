package Controller;

import Constants.GameConstants;
import Exceptions.InvalidMap;
import Exceptions.InvalidCommand;
import Models.*;
import Services.MapService;
import Services.PlayerService;
import Utils.Command;
import Utils.CommonUtil;
import Views.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This serves as the starting point for the game and manages the current state of the game.
 *
 * @author Harsh Tank, Pranjalesh Ghansiyal, Yatish Chutani
 * @version 1.0.0
 */
public class GameEngine {

    /**
     * d_gameState Records the data pertaining to the current game.
     */
    GameState d_gameState = new GameState();

    /**
     * d_mapService Used to manage load, read, parse, edit, and save functions of map file.
     */
    MapService d_mapService = new MapService();

    /**
     * Player Service Edit players and issue orders.
     */
    PlayerService d_playerService = new PlayerService();

    /**
     *	Current gameplay phase according to the state pattern.
     */
    Phase d_presentPhase = new StartUpPhase(this, d_gameState);

    /**
     * Method to set the current phase
     *
     * @param p_phase new Phase in Game context
     */
    private void setD_PresentPhase(Phase p_phase){
        d_presentPhase = p_phase;
    }

    /**
     * Getter method to retrieve current phase
     *
     * @return current Phase of Game Context
     */
    public Phase getD_PresentPhase(){
        return d_presentPhase;
    }

    /**
     * The main function responsible for receiving user commands and directing them to their appropriate
     * logical pathways.
     *
     * @param p_args the program doesn't use default command line arguments
     */
    public static void main(String[] p_args) {
        GameEngine l_gameEngine = new GameEngine();

        l_gameEngine.getD_PresentPhase().getD_gameState().logUpdate("Loading the Game ......"+System.lineSeparator(), "start");
        l_gameEngine.setD_logGameEngine("Game Startup Phase", "phase");
        l_gameEngine.getD_PresentPhase().initPhase();
    }


    public void setD_logGameEngine(String p_logGameEngine, String p_logType){}

    /**
     * Validation of the "editmap" command involves checking for the necessary arguments and directing control to the
     * model for actual execution.
     *
     * @param p_command command entered by the user on CLI
     * @throws IOException indicates when failure in I/O operation
     * @throws InvalidCommand indicates when command is invalid
     */
    public void executeMapEdit(Command p_command) throws IOException, InvalidCommand {
        List<Map<String, String>> l_operations_list = p_command.getParametersAndOperations();

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)) {
                    d_mapService.mapModify(d_gameState, l_map.get(GameConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITMAP);
                }
            }
        }
    }

    /**
     * Validation of the "editcontinent" command entails verifying the presence of essential arguments and directing
     * control to the model for the actual processing.
     *
     * @param p_command command entered by the user
     * @throws IOException indicates failure in I/O operation
     * @throws InvalidCommand indicates an invalid command
     * @throws InvalidMap indicates an invalid map
     */
    public void executeContinentEdit(Command p_command) throws IOException, InvalidCommand, InvalidMap {
        List<Map<String, String>> l_operations_list = p_command.getParametersAndOperations();

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITCONTINENT);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)
                        && p_command.isKeywordAvailable(GameConstants.OPERATION, l_map)) {
                    d_mapService.modifyContinent(d_gameState, l_map.get(GameConstants.ARGUMENTS),
                            l_map.get(GameConstants.OPERATION));
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITCONTINENT);
                }
            }
        }
    }

    /**
     * Validation of the "savemap" command involves verifying the presence of the necessary arguments and then
     * redirecting control to the model for the actual processing.
     *
     * @param p_command command entered by the user.
     * @throws InvalidMap indicates when map is invalid
     * @throws InvalidCommand indicates when command is invalid
     */
    public void executeMapSave(Command p_command) throws InvalidCommand, InvalidMap {
        List<Map<String, String>> l_operations_list = p_command.getParametersAndOperations();

        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_SAVEMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)) {
                    boolean l_fileUpdateStatus = d_mapService.mapSave(d_gameState,
                            l_map.get(GameConstants.ARGUMENTS));
                    if (l_fileUpdateStatus)
                        System.out.println("Necessary changes have been made to the map file.");
                    else
                        System.out.println(d_gameState.getD_errorMessage());
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_SAVEMAP);
                }
            }
        }
    }

    /**
     * Validation of the "validatemap" command involves checking for the necessary arguments and then directing
     * control to the model for the actual processing.
     *
     * @param p_command command entered by the user
     * @throws InvalidCommand indicates when command is invalid
     * @throws InvalidMap indicates when map is invalid
     */
    private void executeMapValidation(Command p_command) throws InvalidMap, InvalidCommand {
        List<Map<String, String>> l_operations_list = p_command.getParametersAndOperations();
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            Models.Map l_currentMap = d_gameState.getD_map();
            if (l_currentMap == null) {
                throw new InvalidMap(GameConstants.INVALID_MAP_EMPTY);
            } else {
                if (l_currentMap.Validate()) {
                    System.out.println(GameConstants.VALID_MAP);
                } else {
                    System.out.println("Failed! Map could not be validated.");
                }
            }
        } else {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_VALIDATEMAP);
        }
    }

    /**
     * Validation of "editneighbor" command facilitates the functionality to check for necessary argument,
     * not null checks and transfer control to model for further computation.
     *
     * @param p_entered_command command input entered by player
     * @throws InvalidCommand throws exception if command is invalid
     * @throws InvalidMap throws exception if map is invalid
     */
    public void executeNeighbourEdit(Command p_entered_command) throws InvalidCommand, InvalidMap {
        List<Map<String, String>> l_operation_records = p_entered_command.getParametersAndOperations();
        if (l_operation_records.isEmpty() == true || l_operation_records == null) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITNEIGHBOUR);
        } else {
            for (Map<String, String> l_temp_map : l_operation_records) {
                if (p_entered_command.isKeywordAvailable(GameConstants.OPERATION, l_temp_map)
                        && p_entered_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_temp_map)) {
                    d_mapService.modifyNeighbor(d_gameState, l_temp_map.get(GameConstants.OPERATION),
                            l_temp_map.get(GameConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITNEIGHBOUR);
                }
            }
        }
    }

    /**
     * Validation of "editcountry" command facilitates the functionality to check for necessary argument,
     * not null checks and transfer control to model for further computation.
     *
     * @param p_entered_command command input entered by player
     * @throws InvalidCommand throws exception if command is invalid
     * @throws InvalidMap throws exception if map is invalid
     */
    public void executeCountryEdit(Command p_entered_command) throws InvalidMap,InvalidCommand {
        List<Map<String, String>> l_operation_records = p_entered_command.getParametersAndOperations();

        if (l_operation_records.isEmpty() == true || l_operation_records == null) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_temp_map : l_operation_records) {
                if (p_entered_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_temp_map)
                        && p_entered_command.isKeywordAvailable(GameConstants.OPERATION, l_temp_map)) {
                    d_mapService.modifyCountry(d_gameState, l_temp_map.get(GameConstants.OPERATION),
                            l_temp_map.get(GameConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITCOUNTRY);
                }
            }
        }
    }

    /**
     * Validation of the "gameplayer" command includes checking for the necessary arguments and then directing
     * control to the model for the addition or removal of players.
     *
     * @param p_command command entered by the user.
     * @throws InvalidCommand throws exception if command is invalid.
     */
    private void modifyPlayers(Command p_command) throws InvalidCommand {
        List<Map<String, String>> l_operations_list = p_command.getParametersAndOperations();
        if (CommonUtil.isEmptyCollection(l_operations_list)) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_GAMEPLAYER);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)
                        && p_command.isKeywordAvailable(GameConstants.OPERATION, l_map)) {
                    d_playerService.playerListUpdation(d_gameState, l_map.get(GameConstants.OPERATION),
                            l_map.get(GameConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_GAMEPLAYER);
                }
            }
        }
    }
    /**
     * This method updates the present phase to Order Execution Phase as per the State Pattern.
     */
    public void setOrderExecutionPhase(){
        this.setD_logGameEngine("Order Execution Phase", "phase");
        setD_PresentPhase(new OrderExecutionPhase(this, d_gameState));
        getD_PresentPhase().initPhase();
    }

    /**
     * This method updates the present phase to Issue Order Phase as per the State Pattern.
     */
    public void setIssueOrderPhase(){
        this.setD_logGameEngine("Issue Order Phase", "phase");
        setD_PresentPhase(new IssueOrderPhase(this, d_gameState));
        getD_PresentPhase().initPhase();
    }
}
