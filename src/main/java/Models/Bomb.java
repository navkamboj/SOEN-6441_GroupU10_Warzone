package Models;

import Utils.CommonUtil;

import java.io.Serializable;

/**
 * When player use this Bomb card on target country then target
 * country will lose half of their armies.
 *
 * @author Nihal Galani
 * @version 2.0.0
 */
public class Bomb implements Card, Serializable {

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
    public Bomb(Player p_playerName,String p_countryNameOfTarget){
        this.d_playerName=p_playerName;
        this.d_countryNameOfTarget=p_countryNameOfTarget;
    }

    /**
     * This method is used to execute Bomb card on target country after validation and
     * make changes to the armies of target country.
     *
     * @param p_gameState current game state
     */
    @Override
    public void execute(GameState p_gameState){
        if(isValid(p_gameState)){
            Country l_countryNameOfTarget= p_gameState.getD_map().getCountryByName(d_countryNameOfTarget);
            Integer l_targetCountryArmies = l_countryNameOfTarget.getD_numberOfArmies()==0?1:l_countryNameOfTarget.getD_numberOfArmies();

            Integer l_armiesAfterBomb=(int)Math.floor(l_targetCountryArmies/2);
            l_countryNameOfTarget.setD_numberOfArmies(l_armiesAfterBomb);

            d_playerName.removeCard("Bomb");
            this.setD_logOfOrderExecution("Bomb card is applied by player name "+this.d_playerName.getD_playerName()+" with armies "
                    +l_targetCountryArmies+" on Country Name: "
                    +l_countryNameOfTarget.getD_countryName()+" and after Bomb card implementation new armies will be "+l_armiesAfterBomb,"default");
            p_gameState.logUpdate(logOfOrderExecution(),"effect");
        }
    }

    /**
     * This method used to check that target country is owned by the player who used Bomb card and also
     * used to check that is their any pact between target country's owner and the one who uses a Bomb card.
     *
     * @param p_gameState current game state
     * @return Boolean true if it's valid otherwise it will return false.
     */
    @Override
    public boolean isValid(GameState p_gameState){
        Country l_playerCountry=d_playerName.getD_ownedCountries().stream()
                .filter(l_player->l_player.getD_countryName().equalsIgnoreCase(this.d_countryNameOfTarget)).findFirst()
                .orElse(null);


        //Player cannot attack their own territory
        if(!CommonUtil.isNull(l_playerCountry)){
            this.setD_logOfOrderExecution(this.currentlyExecutedOrder()+"will not going to be execute because "+this.d_countryNameOfTarget+" given in the" +
                    "card belong to "+this.d_playerName.getD_playerName() +"reason behind this :- Player can not Bomb their own territory","error");
            p_gameState.logUpdate(logOfOrderExecution(),"effect");
            return false;
        }

        //Player can not Bomb on the territory with whom they make a pact
        if(!d_playerName.negotiationValidation(this.d_countryNameOfTarget)){
            this.setD_logOfOrderExecution(this.currentlyExecutedOrder()+" will not going to be execute because "+this.d_playerName.getD_playerName()+" has a negotiation pact " +
                    "with target territory: "+this.d_countryNameOfTarget+"'s player","error");
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
        this.d_orderLog = "----------This Bomb order issued by player "+d_playerName.getD_playerName()+"----------"+System.lineSeparator()
                +" creates a Bomb order on a Country name "+this.d_countryNameOfTarget+" and after execution of order target country will lose half of their armies.";
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
     * Gives Bomb order which is being executed.
     *
     * @return Bomb order command
     */

    private String currentlyExecutedOrder() {
        return "Order of Bomb:-" + "Bomb" + " " + this.d_countryNameOfTarget;
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