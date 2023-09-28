package Exceptions;

/**
 * This class throws Invalid Command Exceptions
 *
 * @author Yatish Chutani
 * @version 1.0.0
 */
public class InvalidCommand extends Exception{

    /**
     * InvalidCommand constructor is used to print message when exception caught is an invalid case command.
     *
     * @param p_message this message is returned when the command is invalid.
     */
    public InvalidCommand(String p_message){
        super(p_message);
    }
}
