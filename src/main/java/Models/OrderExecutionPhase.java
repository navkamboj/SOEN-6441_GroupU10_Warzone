package Models;

import Controller.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;
import Utils.CommonUtil;
import Views.MapView;

import java.io.IOException;
import java.util.Scanner;

/**
 * Order Execution Phase implementation for the game using State Pattern.
 *
 * @author Pranjalesh Ghansiyal
 * @version 2.0.0
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAssignCountries(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap {
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAdvanceOrder(String p_command, Player p_player) {
        outputStateInvalidCommand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initPhase() {
        while (d_gameEngine.getD_PresentPhase() instanceof OrderExecutionPhase) {
            executeOrders();

            MapView l_mapView =new MapView(d_gameState);
            l_mapView.showMap();

            if (this.checkForGameEnd(d_gameState))
                break;

            while (!CommonUtil.isEmptyCollection(d_gameState.getD_playerList())) {
                System.out.println("Press Y/y if you want to continue to the next turn or press exit.");
                Scanner l_scanner = new Scanner(System.in);

                String l_continue = l_scanner.nextLine();

                if (l_continue.equalsIgnoreCase("exit")) {
                    System.exit(0);
                } else if (l_continue.equalsIgnoreCase("Y")) {
                    d_playerService.armiesAssign(d_gameState);
                    d_gameEngine.setIssueOrderPhase();
                } else {
                    System.out.println("Invalid Input.");
                }

            }
        }
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
        for (Player l_player : p_gameState.getD_playerList()) {
            if (l_player.getD_ownedCountries().size() == l_totalCountries) {
                d_gameEngine.setD_logGameEngine("Player : " + l_player.getD_playerName()
                        + " Wins the Game by conquering all the countries. Exiting Game .....", "end");
                return true;
            }
        }
        return false;
    }
}
