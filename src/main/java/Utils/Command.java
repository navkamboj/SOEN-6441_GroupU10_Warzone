package Utils;

import java.util.*;

/**
 *  Class takes input commands from user.
 *
 *  @author Harsh Tank
 *  @version 1.0.0
 */
public class Command {
    /**
     * d_store_command stores the command that player types.
     */
    public String d_store_command;

    /**
     * Constructor Sets the values of the data member.
     *
     * @param p_enter_command stores the input command entered by player
     */
    public Command(String p_enter_command){
        this.d_store_command = p_enter_command.trim().replaceAll(" +", " ");
    }

    /**
     * Getter method to return base command.
     *
     * @return string the base command input entered by user
     */
    public String getBaseCommand(){
        return d_store_command.split(" ")[0];
    }

}
