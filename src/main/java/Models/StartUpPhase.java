package Models;

import Constants.GameConstants;
import Controller.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;
import Utils.ExceptionLogHandler;
import Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implementation of Startup Phase class for GamePlay using State Pattern.
 *
 * @author Navjot Kamboj
 * @version 2.0.0
 */
public class StartUpPhase extends Phase {

    public StartUpPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }

    @Override
    protected void doEditMap(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        List<java.util.Map<String, String>> l_listOfOperations = p_command.getParametersAndOperations();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITMAP);
        } else {
            for (Map<String, String> l_map : l_listOfOperations) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)) {
                    d_mapService.mapModify(d_gameState, l_map.get(GameConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITMAP);
                }
            }
        }
    }

    @Override
    protected void doEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
    //body
    }

    @Override
    protected void doLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        List<Map<String, String>> l_operations_list = p_command.getParametersAndOperations();
        boolean l_flagValidate = false;

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_LOADMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)) {
                    // Loads the map if it is valid or resets the game state
                    Models.Map l_mapToLoad = d_mapService.mapLoad(d_gameState,
                            l_map.get(GameConstants.ARGUMENTS));
                    if (l_mapToLoad.Validate()) {
                        l_flagValidate = true;
                        d_gameState.setD_checkLoadCommand();
                        d_gameEngine.setD_logGameEngine(l_map.get(GameConstants.ARGUMENTS) + " has been loaded to start the game", "effect");
                    } else {
                        d_mapService.mapReset(d_gameState, l_map.get(GameConstants.ARGUMENTS));
                    }
                    if (!l_flagValidate) {
                        d_mapService.mapReset(d_gameState, l_map.get(GameConstants.ARGUMENTS));
                    }
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_LOADMAP);
                }
            }
        }
    }

    @Override
    protected void doSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_logGameEngine("There is no map to save yet !!, Run `editmap` command first", "effect");
            return;
        }
        List<Map<String, String>> l_listOfOperations = p_command.getParametersAndOperations();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (null == l_listOfOperations || l_listOfOperations.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_SAVEMAP);
        } else {
            for (Map<String, String> l_map : l_listOfOperations) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)) {
                    boolean l_fileStatusForUpdate = d_mapService.mapSave(d_gameState,
                            l_map.get(GameConstants.ARGUMENTS));
                    if (l_fileStatusForUpdate) {
                        d_gameEngine.setD_logGameEngine("All necessary changes have been made in the map file", "effect");
                    } else
                        System.out.println(d_gameState.getD_errorMessage());
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_SAVEMAP);
                }
            }
        }
    }

    @Override
    protected void doValidateMap(Command p_command, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_logGameEngine("There is no map to validate yet !!, Run `loadmap` & `editmap` command first", "effect");
            return;
        }

        List<Map<String, String>> l_listOfOperations = p_command.getParametersAndOperations();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (null == l_listOfOperations || l_listOfOperations.isEmpty()) {
            Models.Map l_currentMap = d_gameState.getD_map();
            if (l_currentMap == null) {
                throw new InvalidMap(GameConstants.INVALID_MAP_EMPTY);
            } else {
                if (l_currentMap.Validate()) {
                    d_gameEngine.setD_logGameEngine(GameConstants.VALID_MAP, "effect");
                } else {
                    throw new InvalidMap("Failed to Validate this map!");
                }
            }
        } else {
            throw new InvalidCommand(GameConstants.INVALID_MAP_EMPTY);
        }
    }

    @Override
    protected void doShowMap(Command p_command, Player p_player) {
        MapView l_mapViewInstance = new MapView(d_gameState);
        l_mapViewInstance.showMap();
    }

    @Override
    protected void doEditCountry(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
    //body
    }

    @Override
    protected void doEditNeighbour(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
    //body
    }

    @Override
    protected void doAssignCountries(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
    //body
    }

    @Override
    protected void createGamePlayers(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
    //body
    }

    @Override
    protected void doCreateDeploy(String p_command, Player p_player) throws IOException {
    //body
    }

    @Override
    protected void doAdvanceOrder(String p_command, Player p_player) throws IOException {
    //body
    }

    @Override
    protected void doCardHandle(String p_enteredCommand, Player p_player) throws IOException {
        //body
    }

    @Override
    public void initPhase() {
        Scanner l_scanner = new Scanner(System.in);

        while (d_gameEngine.getD_PresentPhase() instanceof StartUpPhase) {
            try {
                System.out.println("Enter Commands to play the game else type 'exit' to quit");
                String l_enteredCommand = l_scanner.nextLine();

                handleCommand(l_enteredCommand);
            } catch (InvalidCommand | InvalidMap | IOException l_exception) {
                d_gameEngine.setD_logGameEngine(l_exception.getMessage(), "effect");
            }
        }
    }

}
