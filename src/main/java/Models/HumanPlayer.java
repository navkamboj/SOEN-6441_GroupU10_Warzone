package Models;

import java.io.IOException;
import java.util.Scanner;

/**
 * This is the Human Player class which operates with user orders to make decisions.
 *
 * @author Pranjalesh Ghansiyal
 * @version 3.0.0
 */
public class HumanPlayer extends PlayerBehaviorStrategy {

    /**
     * This method generates a new order.
     *
     * @param p_player Player class object
     * @param p_gameState GameState class object
     *
     * @return
     * @throws IOException
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) throws IOException {

        Scanner l_scanner = new Scanner(System.in);
        System.out.println("\nPlease enter a command to issue an order for player: " + p_player.getD_playerName()
                + " or use `showmap` command to view current map with the state of the game.");
        String l_enteredCommand = l_scanner.nextLine();
        return l_enteredCommand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderDeploy(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderAdvance(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderCard(Player p_player, GameState p_gameState, String p_cardName) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlayerBehavior() {
        return "Human";
    }
}
