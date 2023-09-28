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
     * d_storeCommand stores the command that user entered.
     */
    public String d_storeCommand;

    /**
     * Constructor Assigns the values to the data member.
     *
     * @param p_enterCommand stores the input command entered by user
     */
    public Command(String p_enterCommand) {
        this.d_storeCommand = p_enterCommand.trim().replaceAll(" +", " ");
    }

    /**
     * Getter method to return base command.
     *
     * @return string the base command input entered by user
     */
    public String getBaseCommand(){
        return d_storeCommand.split(" ")[0];
    }

    /**
     * This method identifies the availability of keyword in specified map.
     *
     * @param p_keyword Keyword entered by user
     * @param p_specifiedMap the specific map given by user
     * @return boolean method returns true is keyword is present in map else returns false
     */
    public boolean isKeywordAvailable(String p_keyword, Map<String, String> p_specifiedMap) {
        if(!p_specifiedMap.get(p_keyword).isEmpty() && p_specifiedMap.get(p_keyword) != null
                && p_specifiedMap.containsKey(p_keyword))
            return true;
        return false;
    }

    /**
     * This method executes all the parameters and operations entered by user.
     *
     * @param p_enterParameter operation entered by user
     * @return map method returns operations associated using map
     */
    private Map<String, String> getParametersAndOperationsMap(String p_enterParameter){

        Map<String, String> l_functionMap = new HashMap<String, String>();

        String[] l_slashParameter = p_enterParameter.split(" ");

        String l_args = "";

        l_functionMap.put("operation", l_slashParameter[0]);

        if(l_slashParameter.length >= 2){
            String[] l_args_list = Arrays.copyOfRange(l_slashParameter, 1, l_slashParameter.length);
            l_args = String.join(" ",l_args_list);
        }

        l_functionMap.put("arguments", l_args);

        return l_functionMap;
    }

    /**
     * This method loop through and processes through the list of operations entered by user.
     *
     * @return list method returns the list of mapped operations
     */
    public List<Map<String , String>> getParametersAndOperations() {
        String l_baseCommand = getBaseCommand();
        String l_commandsString =  d_storeCommand.replace(l_baseCommand, "").trim();

        if(l_commandsString.isEmpty() || l_commandsString == null ) {
            return new ArrayList<Map<String , String>>();
        }
        boolean l_isUnNotifiedCommand = !l_commandsString.contains(" ") && !l_commandsString.contains("-");

        if(l_isUnNotifiedCommand){
            l_commandsString = "-filename "+l_commandsString;
        }

        List<Map<String , String>> l_commands_list  = new ArrayList<Map<String,String>>();
        String[] l_commands = l_commandsString.split("-");

        // looping through each command and adding it to the mapped list
        Arrays.stream(l_commands).forEach((cmd) -> {
            if(cmd.length() >= 2) {
                l_commands_list.add(getParametersAndOperationsMap(cmd));
            }
        });

        return l_commands_list;
    }
}
