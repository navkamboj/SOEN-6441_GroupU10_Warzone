package Models;

/**
 *
 * This Class handles command which is given by the player.
 *
 * @author Nihal Galani
 * @version 2.0.0
 */
public interface Order{
    /**
     * This method will be called when receiver wants to execute the command.
     *
     * @param p_gameState current game state.
     */
    public void execute(GameState p_gameState);

    /**
     *This method validates the command before the executes.
     *
     * @param p_gameState current game state.
     * @return Boolean true if its valid otherwise it will return false.
     */
    public Boolean isValid(GameState p_gameState);

    /**
     *This method is used to print the order information.
     */
    public void printTheOrder();

    /**
     *This method is used to return Log of execution order.
     * @return log in the form of String.
     */
    public String logOfOrderExecution();

    /**
     *
     * @param p_logOfOrderExecution this is a log string
     * @param p_typeOfLog this is a type of log either error or default
     */
    public void setD_logOfOrderExecution(String p_logOfOrderExecution, String p_typeOfLog);

    /**
     *
     * @return String value of order name
     */
    public String orderName();
}