package Models;

import Constants.GameConstants;
import Controller.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.GameService;
import Utils.Command;
import Utils.ExceptionLogHandler;
import Views.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implementation of Issue Order Phase class for GamePlay using State Pattern.
 *
 * @author Navjot Kamboj
 * @version 2.0.0
 */
public class IssueOrderPhase extends Phase {

    /**
     * Constructor of class to initialize current game engine value
     *
     * @param p_gameEngine instant to update state
     * @param p_gameState  instant for game state
     */
    public IssueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }

    /**
     * Asks for order commands from user.
     *
     * @param p_player player for which commands are to be issued
     * @throws InvalidCommand exception if command is invalid
     * @throws IOException    indicates failure in I/O operation
     * @throws InvalidMap     indicates failure in using the invalid map
     */
    public void inputOrder(Player p_player) throws InvalidCommand, IOException, InvalidMap {

        String l_enteredCommand = p_player.getPlayerOrder(d_gameState);
        if(l_enteredCommand == null) return;

        d_gameState.logUpdate("(Player: " + p_player.getD_playerName() + ") " + l_enteredCommand, "order");
        handleCommand(l_enteredCommand, p_player);
    }

    /**
     * Method to accept Orders from players
     */
    protected void issueOrders(boolean p_isTournamentMode) {
        // Issues order for every player
        do {
            for (Player l_player : d_gameState.getD_playerList()) {
                if(l_player.getD_ownedCountries().size()==0){
                    l_player.setD_moreOrders(false);
                }
                if (l_player.getD_moreOrders() && !l_player.getD_playerName().equals("Neutral")) {
                    try {
                        l_player.issue_order(this);
                        l_player.checkForMoreOrders(p_isTournamentMode);
                    } catch (InvalidCommand | IOException | InvalidMap l_exception) {
                        d_gameEngine.setD_logGameEngine(l_exception.getMessage(), "effect");
                    }
                }
            }
        } while (d_playerService.checkForMoreOrders(d_gameState.getD_playerList()));

        d_gameEngine.setOrderExecutionPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditMap(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doValidateMap(Command p_command, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doShowMap(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();

        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditCountry(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditNeighbour(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAssignCountries(Command p_command, Player p_player, boolean p_isTournamentMode, GameState p_gameState) throws InvalidCommand, IOException, InvalidMap {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createGamePlayers(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
        outputStateInvalidCommand();
        inputOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doCreateDeploy(String p_command, Player p_player) throws IOException {
        p_player.deployOrder(p_command);
        d_gameState.logUpdate(p_player.getD_playerLog(), "effect");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAdvanceOrder(String p_command, Player p_player) throws IOException {
        p_player.createAdvanceOrder(p_command, d_gameState);
        d_gameState.logUpdate(p_player.getD_playerLog(), "effect");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doCardHandle(String p_enteredCommand, Player p_player) throws IOException {
        if (p_player.getD_cardsOwned().contains(p_enteredCommand.split(" ")[0])) {
            p_player.cardCommandsHandler(p_enteredCommand, d_gameState);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initPhase(boolean p_isTournamentMode) {
        while (d_gameEngine.getD_PresentPhase() instanceof IssueOrderPhase) {
            issueOrders(p_isTournamentMode);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoadGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
        inputOrder(p_player);
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
                String l_fileName = l_mapFile.get(GameConstants.ARGUMENTS);
                GameService.saveGame(this, l_fileName);
                d_gameEngine.setD_logGameEngine("Game is saved Successfully to "+l_fileName, "effect");

            } else {
                throw new InvalidCommand(GameConstants.INVALID_COMMAND_SAVEGAME);
            }
        }
    }

    @Override
    protected void tournamentGameMode(Command p_command) throws InvalidCommand, InvalidMap {
    }
}
