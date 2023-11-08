package Models;

/**
 * This interface handles all the card that player can have
 *
 * @author Nihal Galani
 * @version 2.0.0
 */
public interface Card extends Order{
    /**
     * This method is used to pre-validate card type order.
     * @param p_gameState current game state
     * @return boolean true if it's valid otherwise it will return false
     */
    public Boolean validateOrder(GameState p_gameState);
}