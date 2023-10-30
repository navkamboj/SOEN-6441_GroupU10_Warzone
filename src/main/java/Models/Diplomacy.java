package Models;

import Services.PlayerService;

/**
 * This class is used to handle Diplomacy command
 */
public class Diplomacy implements Card{

    /**
     *Negotiate command is issued by this player.
     */
    Player d_playerName;

    /**
     *This variable holds the Player name with whom player want to negotiate
     */
    String d_targetPlayerName;

    /**
     * This variable holds the log about information related to order.
     */
    String d_orderLog;

    /**
     *Constructor receives parameters to initialize the variables.
     *
     * @param p_playerName card owned by this player.
     * @param p_targetPlayerName with whom player want to negotiate.
     */
    public Diplomacy(String p_targetPlayerName,Player p_playerName){
        this.d_targetPlayerName=p_targetPlayerName;
        this.d_playerName=p_playerName;
    }

    public void execute(GameState p_gameState){
        PlayerService l_serviceForThePlayer = new PlayerService();
        Player l_targetPlayerName = l_serviceForThePlayer.findPlayerByName(d_targetPlayerName,p_gameState);
        l_targetPlayerName.addPlayerNegotiation(d_playerName);
        d_playerName.addPlayerNegotiation(d_targetPlayerName);

        d_playerName.removeCard("negotiate");

        this.setD_logOfOrderExecution("Negotiation is approached by  "+this.d_playerName.getD_playerName()+" with player "
                +this.d_targetPlayerName+" is successfully done","default");
        p_gameState.logUpdate(logOfOrderExecution(),"effect");
    }

    /**
     *
     * @param p_gameState current game state
     * @return boolean true
     */
    public Boolean valid(GameState p_gameState){
        return true;
    }

    /**
     *This method is used to print the order
     */
    @Override
    public void printTheOrder(){
        this.d_orderLog = "----------This Diplomacy order issued by player "+d_playerName.getD_playerName()+"----------"+System.lineSeparator()
                +" makes a negotiate request to player "+this.d_targetPlayerName;
        System.out.println(System.lineSeparator()+this.d_orderLog);
    }

    /**
     *Log of execution order.
     *
     * @return This method will return Logs in the form of string.
     */
    @Override
    public String logOfOrderExecution(){
        return this.d_orderLog;
    }

    /**
     *
     * @param p_logOfOrderExecution this is a log string
     * @param p_typeOfLog this is a type of log either error or default
     */
    @Override
    public void setD_logOfOrderExecution(String p_logOfOrderExecution, String p_typeOfLog){
        this.d_orderLog=p_logOfOrderExecution;
        if(p_typeOfLog.equals("error")){
            System.err.println(p_logOfOrderExecution);
        }
        else{
            System.out.println(p_logOfOrderExecution);
        }
    }

    /**
     * Gives Diplomacy order which is being executed.
     *
     * @return Diplomacy order command
     */

    private String currentlyExecutedOrder() {
        return "Order of Diplomacy " + "negotiate" + " " + this.d_targetPlayerName;
    }

    /**
     *
     * @param p_gameState current game state
     * @return boolean value true if target player is available otherwise it will return false
     */
    @Override
    public Boolean validateOrder(GameState p_gameState){
        PlayerService l_playerService = new PlayerService();
        Player l_targetPlayerName =l_playerService.findPlayerByName(d_targetPlayerName,p_gameState);
        if(!p_gameState.getD_playerList().contains(l_targetPlayerName)){
            this.setD_logOfOrderExecution("target player with whom player wants to negotiate doesn't exists.", "error");
            return false;
        }
        return true;
    }

    /**
     *
     * @return String value of order name
     */
    @Override
    public String orderName(){
        return "diplomacy";
    }

}
