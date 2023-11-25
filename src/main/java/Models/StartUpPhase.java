package Models;

import Constants.GameConstants;
import Controller.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.GameService;
import Utils.Command;
import Utils.CommonUtil;
import Utils.ExceptionLogHandler;
import Views.MapView;
import Views.TournamentView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implementation of Startup Phase class for GamePlay using State Pattern.
 *
 * @author Navjot Kamboj, Pranjalesh Ghansiyal
 * @version 2.0.0
 */
public class StartUpPhase extends Phase {

    /**
     * This is a constructor that init the GameEngine context in Phase class.
     *
     * @param p_gameEngine GameEngine Context
     * @param p_gameState current Game State
     */
    public StartUpPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoadGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        List<java.util.Map<String, String>> l_operations = p_command.getParametersAndOperations();

        if (l_operations == null || l_operations.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_LOADGAME);
        }

        for (Map<String, String> l_mapFile : l_operations) {
            if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_mapFile)) {
                String l_filename = l_mapFile.get(GameConstants.ARGUMENTS);

                try{
                    Phase l_gamePhase= GameService.loadGame(l_filename);
                    this.d_gameEngine.setD_logGameEngine("Map has been loaded ! You can play the game..", "effect");
                    this.d_gameEngine.loadPhase(l_gamePhase);
                } catch (ClassNotFoundException l_exception) {
                    l_exception.printStackTrace();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSaveGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        List<java.util.Map<String, String>> l_operations = p_command.getParametersAndOperations();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (l_operations == null || l_operations.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_SAVEGAME);
        }

        for (Map<String, String> l_mapFile : l_operations) {
            if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_mapFile)) {
                String l_filename = l_mapFile.get(GameConstants.ARGUMENTS);
                GameService.saveGame(this, l_filename);
                d_gameEngine.setD_logGameEngine("Gameplay has been saved Successfully to "+l_filename, "effect");
            } else {
                throw new InvalidCommand(GameConstants.INVALID_COMMAND_SAVEGAME);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tournamentGameMode(Command p_command) throws InvalidCommand, InvalidMap, IOException {

        if (d_gameState.getD_playerList() != null && d_gameState.getD_playerList().size() > 1) {
            List<Map<String, String>> l_operationsList = p_command.getParametersAndOperations();
            boolean l_successfulParsing = false;
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
            if (CommonUtil.isCollectionEmpty(l_operationsList)
                    && !d_tournament.validateTournamentArguments(l_operationsList, p_command)) {
                throw new InvalidCommand(GameConstants.INVALID_COMMAND_TOURNAMENT_MODE);
            } else {
                for (Map<String, String> l_map : l_operationsList) {
                    if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)
                            && p_command.isKeywordAvailable(GameConstants.OPERATION, l_map)) {
                        l_successfulParsing = d_tournament.parseTournamentCommand(d_gameState,
                                l_map.get(GameConstants.OPERATION), l_map.get(GameConstants.ARGUMENTS),
                                d_gameEngine);
                        if (!l_successfulParsing)
                            break;

                    } else {
                        throw new InvalidCommand(GameConstants.INVALID_COMMAND_TOURNAMENT_MODE);
                    }
                }
            }
            if (l_successfulParsing) {
                for (GameState l_gameState : d_tournament.getD_gameStates()) {
                    d_gameEngine.setD_logGameEngine(
                            "\nStarting New Game on map : " + l_gameState.getD_map().getD_mapFile()
                                    + " ........\n", "effect");
                    doAssignCountries(new Command("assigncountries"),
                            null, true, l_gameState);

                    d_gameEngine.setD_logGameEngine(
                            "\nGame Completed on the map : " + l_gameState.getD_map().getD_mapFile()
                                    + " .........\n", "effect");
                }
                d_gameEngine.setD_logGameEngine("---------------- Tournament Completed ----------------",
                        "effect");
                TournamentView l_tournamentView = new TournamentView(d_tournament);
                l_tournamentView.tournamentView();
                d_tournament = new Tournament();
            }
        } else {
            d_gameEngine.setD_logGameEngine("Please add 2 or more players in the game to begin.",
                    "effect");
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_logGameEngine("Can not do edit continent at this stage, please perform" +
                    " `editmap` command first.", "effect");
            return;
        }

        List<Map<String, String>> l_list_of_operations = p_command.getParametersAndOperations();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (l_list_of_operations == null || l_list_of_operations.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITCONTINENT);
        } else {
            for (Map<String, String> l_map : l_list_of_operations) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)
                        && p_command.isKeywordAvailable(GameConstants.OPERATION, l_map)) {
                    d_mapService.editFunctionality(d_gameState, l_map.get(GameConstants.ARGUMENTS),
                            l_map.get(GameConstants.OPERATION), 1);
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITCONTINENT);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
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
                    // Loads a valid map or Resets the gameState
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_VALIDATEMAP);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doShowMap(Command p_command, Player p_player) {
        MapView l_mapViewInstance = new MapView(d_gameState);
        l_mapViewInstance.showMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditCountry(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_logGameEngine("Can not do edit country at this stage, please perform" +
                    " `editmap` command first.", "effect");
        }

        List<Map<String, String>> l_list_of_operations = p_command.getParametersAndOperations();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (l_list_of_operations == null || l_list_of_operations.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_map : l_list_of_operations) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)
                        && p_command.isKeywordAvailable(GameConstants.OPERATION, l_map)) {
                    d_mapService.editFunctionality(d_gameState, l_map.get(GameConstants.ARGUMENTS),
                            l_map.get(GameConstants.OPERATION), 2);
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITCOUNTRY);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditNeighbour(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_logGameEngine("Can not do edit neighbor at this stage, please perform" +
                    " `editmap` command first.", "effect");
        }

        List<Map<String, String>> l_list_of_operations = p_command.getParametersAndOperations();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (l_list_of_operations == null || l_list_of_operations.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITNEIGHBOUR);
        } else {
            for (Map<String, String> l_map : l_list_of_operations) {
                if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)
                        && p_command.isKeywordAvailable(GameConstants.OPERATION, l_map)) {
                    d_mapService.editFunctionality(d_gameState, l_map.get(GameConstants.ARGUMENTS),
                            l_map.get(GameConstants.OPERATION), 3);
                } else {
                    throw new InvalidCommand(GameConstants.INVALID_COMMAND_EDITNEIGHBOUR);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected void doAssignCountries(Command p_command, Player p_player,boolean p_isTournamentMode,
                                     GameState p_gameState) throws InvalidCommand, IOException, InvalidMap {
        if (p_gameState.getD_checkLoadCommand()) {
        List<Map<String, String>> l_list_of_operations= p_command.getParametersAndOperations();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (CommonUtil.isEmptyCollection(l_list_of_operations)|| p_isTournamentMode) {
            d_gameEngine.setD_gameState(p_gameState);
            d_gameEngine.setD_isTournamentMode(p_isTournamentMode);

            if(d_playerService.countryAssign(d_gameState)){
            d_playerService.colorAssign(d_gameState);
            d_playerService.armiesAssign(d_gameState);
            d_gameEngine.setIssueOrderPhase(p_isTournamentMode);
        }
        } else {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_ASSIGNCOUNTRIES);
        }
    }else {
            d_gameEngine.setD_logGameEngine("Please load a valid map first using the loadmap command!", "effect");
        }
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected void createGamePlayers(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {

        List<Map<String, String>> l_list_of_operations = p_command.getParametersAndOperations();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (CommonUtil.isCollectionEmpty(l_list_of_operations)) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_GAMEPLAYER);
        } else {
                for (Map<String, String> l_map : l_list_of_operations) {
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
     * {@inheritDoc}
     */
    @Override
    protected void doCreateDeploy(String p_command, Player p_player) throws IOException {
        outputStateInvalidCommand();    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAdvanceOrder(String p_command, Player p_player) throws IOException {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doCardHandle(String p_enteredCommand, Player p_player) throws IOException {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initPhase(boolean p_isTournamentMode) {
        Scanner l_scanner = new Scanner(System.in);

        while (d_gameEngine.getD_PresentPhase() instanceof StartUpPhase) {
            try {
                System.out.println("Enter Commands to play the game / Type 'help' for assistance / Type 'exit' to quit");
                String l_enteredCommand = l_scanner.nextLine();

                handleCommand(l_enteredCommand);
            } catch (InvalidCommand | InvalidMap | IOException l_exception) {
                d_gameEngine.setD_logGameEngine(l_exception.getMessage(), "effect");
            }
        }
    }

}
