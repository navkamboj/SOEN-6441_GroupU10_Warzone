package Models;

import Utils.CommonUtil;

/**
 * This blockade class is used to neutral one of the territories of the player and triplet the number of armies on that territory.
 *
 * @author Nihal Galani
 * @version 2.0.0
 */
public class Blockade implements Card{
    /**
     *Card is owned by this player.
     */
    Player d_playerName;

    /**
     *This variable holds the target country name.
     */
    String d_countryNameOfTarget;

    /**
     * This variable holds the log about information related to order.
     */
    String d_orderLog;

    /**
     *Constructor receives parameters to initialize the variables.
     *
     * @param p_playerName card owned by this player.
     * @param p_countryNameOfTarget target country.
     */
    public Blockade(Player p_playerName,String p_countryNameOfTarget){
        this.d_playerName=p_playerName;
        this.d_countryNameOfTarget=p_countryNameOfTarget;
    }

    /**
     *Order execution method of Blockade order
     *
     * @param p_gameState current game state
     */
    @Override
    public void execute(GameState p_gameState){
        if(valid(p_gameState)){
            Country l_countryNameOfTarget = p_gameState.getD_map().getCountryByName(d_countryNameOfTarget);
            Integer l_targetCountryArmies = l_countryNameOfTarget.getD_numberOfArmies()==0?1:l_countryNameOfTarget.getD_numberOfArmies();
            l_countryNameOfTarget.setD_numberOfArmies(3*l_targetCountryArmies);

            //Change country to a neutral country from the status of owned by the player
            d_playerName.getD_ownedCountries().remove(l_countryNameOfTarget);

            Player l_player = p_gameState.getD_playerList().stream().
                    filter(l_players->l_players.getD_playerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);

            //neutral country is assigned to currently available neutral player
            if(!CommonUtil.isNull(l_player)){
                l_player.getD_ownedCountries().add(l_countryNameOfTarget);
                System.out.println("Neutral territory named as "+l_countryNameOfTarget.getD_countryName()+" is " +
                        "assigned to neutral player's owned country list");
            }

            d_playerName.removeCard("blockade");
            this.setD_logOfOrderExecution("Defensive blockade is applied by player name "+this.d_playerName.getD_playerName()+" with total "
                    +l_countryNameOfTarget.getD_numberOfArmies()+" armies on Country Name: "
                    +l_countryNameOfTarget.getD_countryName(),"default");
            p_gameState.logUpdate(logOfOrderExecution(),"effect");
        }
    }

    /**
     * This method is used to validate that is target country is owned by the player or not.
     *
     * @param p_gameState current game state
     * @return returns true if target country belongs to player otherwise it will return false.
     */
    @Override
    public Boolean valid(GameState p_gameState){

        //This method will check that target country is owned by player or not.

        Country l_targetCountry = d_playerName.getD_ownedCountries().stream()
                .filter(l_player->l_player.getD_countryName().equalsIgnoreCase(this.d_countryNameOfTarget)).findFirst().orElse(null);

        if(CommonUtil.isNull(l_targetCountry)){
            this.setD_logOfOrderExecution(this.currentlyExecutedOrder()+"will not going to be execute because "+this.d_countryNameOfTarget+" given in the" +
                    "card does not belong to "+this.d_playerName,"error");
            p_gameState.logUpdate(logOfOrderExecution(),"effect");
            return false;
        }
        return true;
    }

    /**
     *This method is used to print the order
     */
    @Override
    public void printTheOrder(){
        this.d_orderLog = "----------This Blockade order issued by player "+d_playerName.getD_playerName()+"----------"+System.lineSeparator()
                +" creates a blockade with armies on country "+this.d_countryNameOfTarget;
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
     * Gives Blockade order which is being executed.
     *
     * @return Blockade order command
     */

    private String currentlyExecutedOrder() {
        return "Order of Blockade " + "Blockade" + " " + this.d_countryNameOfTarget;
    }

    /**
     *
     * @param p_gameState current game state
     * @return boolean value true if target country exist on the map otherwise it will return false
     */
    @Override
    public Boolean validateOrder(GameState p_gameState){
        Country l_countryNameOfTarget = p_gameState.getD_map().getCountryByName(d_countryNameOfTarget);
        if(l_countryNameOfTarget==null){
            this.setD_logOfOrderExecution("Target country doesn't exist on the map", "error");
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
        return "Blockade";
    }
}