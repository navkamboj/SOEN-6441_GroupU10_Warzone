package Models;

/**
 * The execute and validation of Airlift is handle by this class.
 * Airlift card can airlift number of armies from source country to target country.
 *
 * @author Nihal Galani
 * @version 2.0.0
 */
public class Airlift implements Card{

    /**
     * card is owned by this player.
     */
    Player d_playerName;

    /**
     * Armies taken from this country.
     */
    String d_countryNameOfSource;

    /**
     *Armies are drop to this country.
     */
    String d_countryNameOfTarget;

    /**
     * Total number of Airlifted armies
     */
    Integer d_airliftedArmies;

    /**
     *Keep record of order execution log
     */
    String d_orderLog;

    /**
     * Constructor receives parameters to initialize the variables.
     *
     * @param p_countryNameOfSource armies are taken from this country.
     * @param p_countryNameOfTarget armies are drop to this country.
     * @param p_airliftedArmies total number of airlifted armies.
     * @param p_playerName card owned by this player.
     */
    public Airlift(String p_countryNameOfSource,String p_countryNameOfTarget,Integer p_airliftedArmies,Player p_playerName){
        this.d_countryNameOfSource=p_countryNameOfSource;
        this.d_countryNameOfTarget=p_countryNameOfTarget;
        this.d_airliftedArmies=p_airliftedArmies;
        this.d_playerName=p_playerName;
    }

    /**
     * Order execution method of Airlift order
     * @param p_gameState current game state
     */
    @Override
    public void execute(GameState p_gameState) {
        if (isValid(p_gameState)) {
            Country l_originCountry = p_gameState.getD_map().getCountryByName(d_countryNameOfSource);
            Country l_destinationCountry = p_gameState.getD_map().getCountryByName(d_countryNameOfTarget);

            Integer l_changeDestinationArmies = l_destinationCountry.getD_numberOfArmies() + this.d_airliftedArmies;
            Integer l_changeOriginArmies = l_originCountry.getD_numberOfArmies() - this.d_airliftedArmies;

            l_originCountry.setD_numberOfArmies(l_changeOriginArmies);
            l_destinationCountry.setD_numberOfArmies(l_changeDestinationArmies);

            d_playerName.removeCard("airlift");

            this.setD_logOfOrderExecution("Airlift operation from " + d_countryNameOfSource + " to " + d_countryNameOfTarget + " is successfully done", "default");
            p_gameState.logUpdate(d_orderLog, "effect");
        } else {
            this.setD_logOfOrderExecution("Execution of this Airlift command cannot be possible", "error");
            p_gameState.logUpdate(d_orderLog, "effect");
        }
    }

    /**
     *Before the execution this method is used to validate the orders.
     * @param p_gameState current game state
     * @return boolean value of false and if it's valid it will return true.
     */
    @Override
    public boolean isValid(GameState p_gameState){
        Country l_originCountry=d_playerName.getD_ownedCountries().stream()
                .filter(l_player->l_player.getD_countryName().equalsIgnoreCase(this.d_countryNameOfSource.toString()))
                .findFirst().orElse(null);
        if(l_originCountry==null){
            this.setD_logOfOrderExecution(this.currentlyExecutedOrder()+"will not going to be execute because "+this.d_countryNameOfSource+" given in the" +
                    "card does not belong to "+d_playerName,"error");
            p_gameState.logUpdate(logOfOrderExecution(),"effect");
            return false;
        }

        Country l_destionationCountry=d_playerName.getD_ownedCountries().stream().
                filter(l_player->l_player.getD_countryName().equalsIgnoreCase(this.d_countryNameOfTarget.toString())).
                findFirst().orElse(null);
        if(l_destionationCountry==null){
            this.setD_logOfOrderExecution(this.currentlyExecutedOrder()+"will not going to be execute because "+this.d_countryNameOfTarget+" given in the " +
                    "card does not belong to "+d_playerName,"error");
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
            return false;
        }

        if(this.d_airliftedArmies > l_originCountry.getD_numberOfArmies()){
            this.setD_logOfOrderExecution(this.currentlyExecutedOrder()+"will not going to be execute because total armies given in card order is " +
                    "greater than armies available to source country: "+this.d_countryNameOfSource,"error");
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
            return false;
        }
        return true;
    }

    /**
     *This method is used to print the order
     */
    @Override
    public void printTheOrder(){
        this.d_orderLog = "----------This Airlift order issued by player "+d_playerName.getD_playerName()+"----------"+System.lineSeparator()
                +" will move "+this.d_airliftedArmies+" armies from "+this.d_countryNameOfSource+" country to "+this.d_countryNameOfTarget;
        System.out.println(System.lineSeparator()+this.d_orderLog);
    }

    /**
     * Log of execution order.
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
     * Gives Airlift order which is being executed.
     *
     * @return Airlift order command
     */

    private String currentlyExecutedOrder() {
        return "Order of Airlift " + "airlift" + " " + this.d_countryNameOfSource + " " + this.d_countryNameOfTarget + " "
                + this.d_airliftedArmies+" ";
    }

    /**
     * This method is used to validate that origin country and destination country is present in the map or not
     *
     * @param p_gameState current game state
     * @return boolean true if source country and target country is present in the map otherwise it will return false.
     */
    @Override
    public Boolean validateOrder(GameState p_gameState){
        Country l_originCountry=p_gameState.getD_map().getCountryByName(d_countryNameOfSource);
        Country l_destionationCountry=p_gameState.getD_map().getCountryByName(d_countryNameOfTarget);
        if(l_originCountry==null){
            this.setD_logOfOrderExecution("Source country doesn't exist on the map", "error");
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
            return false;
        }
        if(l_destionationCountry==null){
            this.setD_logOfOrderExecution("destination country doesn't exist on the map", "error");
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
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
        return "Airlift";
    }
}