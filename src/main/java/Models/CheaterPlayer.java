package Models;

import java.io.IOException;

/**
 * Class for Cheater player who cheats by making direct attack on its neighboring enemies during issue order phase
 * and doubles the armies on his countries
 *
 * @author Navjot Kamboj
 * @version 3.0.0
 */
public class CheaterPlayer extends PlayerBehaviorStrategy {

    @Override
    public String createOrder(Player p_player, GameState p_gameState) throws IOException {
        return null;
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
     * Method to return behavior of the player
     * @return Cheater as player behavior
     */
    @Override
    public String getPlayerBehavior() {
        return "Cheater";
    }
}
