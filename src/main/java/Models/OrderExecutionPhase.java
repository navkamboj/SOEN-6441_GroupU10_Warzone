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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Order Execution Phase implementation for the game using State Pattern.
 *
 * @author Pranjalesh Ghansiyal
 * @version 3.0.0
 */
public class OrderExecutionPhase extends Phase {
    /**
     * A constructor that init the GameEngine context in Phase class.
     *
     * @param p_gameEngine GameEngine Context
     * @param p_gameState  current Game State
     */
    public OrderExecutionPhase(GameEngine p_gameEngine, GameState p_gameState) {

        super(p_gameEngine, p_gameState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditMap(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doValidateMap(Command p_command, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doShowMap(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditCountry(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEditNeighbour(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
    }

    @Override
    protected void doAssignCountries(Command p_command, Player p_player, boolean p_isTournamentMode, GameState p_gameState) throws InvalidCommand, IOException, InvalidMap {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createGamePlayers(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doCreateDeploy(String p_command, Player p_player) throws IOException {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doCardHandle(String p_enteredCommand, Player p_player) throws IOException {
        outputStateInvalidCommand();
    }

    @Override
    public void initPhase(boolean p_isTournamentMode) {
        executeOrders();

        MapView l_map_view = new MapView(d_gameState);
        l_map_view.showMap();

        if (this.checkForGameEnd(d_gameState))
            return;

        try {
            String l_continue = this.iterateForNextTurn(p_isTournamentMode);
            if (l_continue.equalsIgnoreCase("N") && p_isTournamentMode) {
                d_gameEngine.setD_logGameEngine("Start Up Phase", "phase");
                d_gameEngine.setD_PresentPhase(new StartUpPhase(d_gameEngine, d_gameState));
            } else if (l_continue.equalsIgnoreCase("N") && !p_isTournamentMode) {
                d_gameEngine.setStartUpPhase();

            } else if (l_continue.equalsIgnoreCase("Y")) {
                System.out.println("\n" + d_gameState.getD_countOfRemainingTurns() + " Turns are remaining for this" +
                        " game. Continuing for the next Turn.\n");
                d_playerService.armiesAssign(d_gameState);
                d_gameEngine.setIssueOrderPhase(p_isTournamentMode);
            } else {
                System.out.println("Invalid Input");
            }
        }  catch (IOException l_e) {
            System.out.println("Invalid Input");
        }
    }

    /**
     * Checks if there is a next turn to be played or not.
     *
     * @param isTournamentMode if tournament is being played
     * @return Yes or no based on user input or tournament turns left
     * @throws IOException indicates failure in I/O operation
     */
    private String iterateForNextTurn(boolean isTournamentMode) throws IOException {
        String l_continue;
        if (isTournamentMode) {
            d_gameState.setD_countOfRemainingTurns(d_gameState.getD_countOfRemainingTurns() - 1);
            l_continue = d_gameState.getD_countOfRemainingTurns() == 0 ? "N" : "Y";
        } else {
            System.out.println("Enter Y/y if you want to continue for the next turn or else enter N/n");
            Scanner l_scanner = new Scanner(System.in);
            l_continue = l_scanner.nextLine();
        }
        return l_continue;
    }

    @Override
    protected void doLoadGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        outputStateInvalidCommand();
    }

    @Override
    protected void doSaveGame(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        List<java.util.Map<String, String>> l_operationsList = p_command.getParametersAndOperations();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (l_operationsList == null || l_operationsList.isEmpty()) {
            throw new InvalidCommand(GameConstants.INVALID_COMMAND_SAVEGAME);
        }

        for (Map<String, String> l_map : l_operationsList) {
            if (p_command.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)) {
                String l_filename = l_map.get(GameConstants.ARGUMENTS);
                GameService.saveGame(this, l_filename);
                d_gameEngine.setD_logGameEngine("Game Saved Successfully to "+l_filename, "effect");
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAdvanceOrder(String p_command, Player p_player) {
        outputStateInvalidCommand();
    }

    /**
     * Invokes the order execution logic for all the orders that are not yet executed.
     */
    protected void executeOrders() {
        createNeutralPlayer(d_gameState);
        //Executing the orders
        d_gameEngine.setD_logGameEngine("\nStarting to execute the Orders.......", "start");
        while (d_playerService.existanceOfUnexecutedOrder(d_gameState.getD_playerList())) {
            for (Player l_player : d_gameState.getD_playerList()) {
                Order l_order =l_player.next_order();

                if (l_order != null) {
                    l_order.printTheOrder();
                    d_gameState.logUpdate(l_order.logOfOrderExecution(), "effect");
                    l_order.execute(d_gameState);                }
            }
        }
        d_playerService.resetPlayerFlag(d_gameState.getD_playerList());
    }

    /**
     * Adds a neutral player to the game state.
     *
     * @param p_gameState GameState
     */
    public void createNeutralPlayer(GameState p_gameState) {
        Player l_player = p_gameState.getD_playerList().stream()
                .filter(l_pl -> l_pl.getD_playerName().equalsIgnoreCase("neutral")).findFirst().orElse(null);
        if (CommonUtil.isNull(l_player)) {
            Player l_neutralPlayer = new Player("Neutral");
            l_neutralPlayer.setStrategy(new HumanPlayer());
            l_neutralPlayer.setD_moreOrders(false);
            p_gameState.getD_playerList().add(l_neutralPlayer);
        }
    }

    /**
     * Checks for the end of the game if a single player has conquered all countries of the map.
     *
     * @param p_gameState Current State of the game
     * @return true if game is to end else false
     */
    protected Boolean checkForGameEnd(GameState p_gameState) {
        int l_totalCountries = p_gameState.getD_map().getD_countries().size();
        d_playerService.updatePlayersInGame(p_gameState);
        for (Player l_player : p_gameState.getD_playerList()) {
            if (l_player.getD_ownedCountries().size() == l_totalCountries) {
                d_gameState.setD_winningPlayer(l_player);
                d_gameEngine.setD_logGameEngine("Player : " + l_player.getD_playerName()
                        + " Wins the Game by conquering all the countries. Exiting Game .....", "end");
                return true;
            }
        }
        return false;
    }
}
