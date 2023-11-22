package Models;

import java.io.IOException;
import java.io.Serializable;

public abstract class PlayerBehaviorStrategy implements Serializable {

    /**
     * Player class object
     */
    Player d_player;

    /**
     * Gamestate class object
     */
    GameState d_gameState;

    /**
     * Creates new order for Aggressive, Benevolent, Cheater and Random Players.
     *
     * @param p_player Player class object
     * @param p_gameState GameState class object
     *
     * @return Order class object
     * @throws IOException Exception
     */
    public abstract String createOrder(Player p_player, GameState p_gameState) throws IOException ;

    /**
     * Strategy defined deploy orders.
     *
     * @param p_player Player who gives card orders
     * @param p_gameState Current game state
     * @return String representing order
     */
    public abstract String createOrderDeploy(Player p_player, GameState p_gameState);

    /**
     * Strategy defined advance orders.
     *
     * @param p_player Player who gives card orders
     * @param p_gameState Current game state
     * @return String representing order
     */
    public abstract String createOrderAdvance(Player p_player, GameState p_gameState);

    /**
     * Strategy defined card orders.
     *
     * @param p_player Player who gives card orders
     * @param p_gameState Current game state
     * @param p_cardName Name of card to create order for
     * @return String representing order
     */
    public abstract String createOrderCard(Player p_player, GameState p_gameState, String p_cardName);

    /**
     * Returns the player behavior.
     *
     * @return String player behavior
     */
    public abstract String getPlayerBehavior();

}
