package Controller;

import Constants.GameConstants;
import Exceptions.InvalidMap;
import Exceptions.InvalidCommand;
import Models.GameState;
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
 * This is the entry point for the Game and keeps track of the current Game State.
 */
public class GameEngine {

    /**
     * d_gameState stores the information about current GamePlay.
     */
    GameState d_gameState = new GameState();

    /**
     * d_mapService instance is used to handle load, read, parse, edit, and save map file.
     */
    MapService d_mapService = new MapService();

    /**
     * Player Service instance to edit players and issue orders.
     */
    PlayerService d_playerService = new PlayerService();

    /**
     * getD_gameState is a getter method to get current game state.
     *
     * @return the current game state
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * The main method responsible for accepting commands from users and redirecting
     * them to their respective logical flow.
     *
     * @param p_args the program doesn't use default command line arguments
     */
    public static void main(String[] p_args) {
        GameEngine l_game = new GameEngine();
        l_game.initGame();
    }

    /**
     * Handles the commands.
     *
     * @param p_enteredCommand
     * @throws IOException
     */
    public void handleCommand(String p_enteredCommand) throws IOException, InvalidMap, InvalidCommand {

        Command l_command = new Command(p_enteredCommand);
        String l_baseCommand = l_command.getBaseCommand();
        boolean l_isMapLoaded = d_gameState.getD_map() != null;

        switch (l_baseCommand) {
            case "editmap" : {
                executeMapEdit(l_command);
                break;
            }
            case "editcontinent" : {
                if (!l_isMapLoaded) {
                    System.out.println("Can not Edit Continent, please execute `editmap` first");
                    break;
                }
                executeContinentEdit(l_command);
                break;
            }
            case "savemap" : {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to execute save operation, Please `editmap` first");
                    break;
                }

                executeMapSave(l_command);
                break;
            }
            case "loadmap" : {
                executeMapLoad(l_command);
                break;
            }
            case "validatemap" : {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to execute validate operation, Please `loadmap` & `editmap` first");
                    break;
                }
                executeMapValidation(l_command);
                break;
            }
            case "editcountry" : {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to execute Edit Country operation, please execute `editmap` first");
                    break;
                }
                executeCountryEdit(l_command);
                break;
            }
            case "editneighbor" : {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to execute Edit Neighbors operation, please execute `editmap` first");
                    break;
                }
                executeNeighbourEdit(l_command);
                break;
            }
            case "gameplayer" : {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to execute Game Player operation, please execute `loadmap` first" +
                            " before adding game players");
                    break;
                }
                modifyPlayers(l_command);
                break;
            }
            case "assigncountries" : {
                //assigncountries func
            }
            case "showmap" : {
                MapView l_mapView = new MapView(d_gameState);
                l_mapView.showMap();
                break;
            }
            case "exit" : {
                System.out.println("Exit Command Entered");
                System.exit(0);
                break;
            }
            default : {
                System.out.println("Invalid Command");
                break;
            }
        }
    }

    /**
     * This function triggers the CommandLineInterface to receive user commands and links them to their corresponding
     * action handlers.
     */
    public void initGame() {
        Scanner l_scannerObject = new Scanner(System.in);
        while(true) {
            try {
                System.out.println("Enter the game commands or enter 'exit' to quit the game\n");
                String l_enteredCommand = l_scannerObject.nextLine();

                handleCommand(l_enteredCommand);
            }
            catch (IOException l_ioException) {
                l_ioException.printStackTrace();
            } catch (InvalidMap l_invalidMapException) {
                l_invalidMapException.printStackTrace();
            } catch (InvalidCommand l_invalidCommandException) {
                l_invalidCommandException.printStackTrace();
            }
        }
    }
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
     * Validation for the "loadmap" command includes confirming the presence of the required arguments and then
     * directing control to the model for the actual processing.
     *
     * @param p_command command entered by the user
     * @throws InvalidCommand indicates when command is invalid
     */
    private void executeMapLoad(Command p_command) throws InvalidCommand {
        List<Map<String, String>> l_operations_list = p_command.getParametersAndOperations();

        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_LOADMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)) {
                    try {

                        // Loads the map if it is valid otherwise resets the game state
                        Models.Map l_mapToLoad = d_mapService.mapLoad(d_gameState,
                                l_map.get(GameConstants.ARGUMENTS));
                        if (l_mapToLoad.Validate()) {
                            System.out.println("Map has been successfully loaded. \n");
                        } else {
                            d_mapService.mapReset(d_gameState);
                        }
                    } catch (InvalidMap l_e) {
                        d_mapService.mapReset(d_gameState);
                    }
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_LOADMAP);
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

}
