package Models;

public class Deploy implements Order{
    Player d_playerName;
    String d_logOfOrderExecution;
    String d_targetCountry;
    Integer d_countOfArmies;

    /**
     * Parameterized constructor initialize all data members necessary to implement the order.
     *
     * @param p_playerName Name of player that created the order
     * @param p_targetCountry country that gets new armies
     * @param p_countOfArmies count of armies
     */
    public Deploy(Player p_playerName, String p_targetCountry, Integer p_countOfArmies) {
        this.d_targetCountry = p_targetCountry;
        this.d_playerName = p_playerName;
        this.d_countOfArmies = p_countOfArmies;
    }

    /**
     * Display and Sets the log of order execution.
     *
     * @param p_logOfOrderExecution String to be set as log
     * @param p_logType log type : default or error
     */
    public void setD_logOfOrderExecution(String p_logOfOrderExecution, String p_logType) {
        this.d_logOfOrderExecution = p_logOfOrderExecution;
        if (p_logType.equals("error")) {
            System.err.println(p_logOfOrderExecution);
        } else {
            System.out.println(p_logOfOrderExecution);
        }
    }
    /**
     * Gets order execution log.
     */
    @Override
    public String logOfOrderExecution() {
        return d_logOfOrderExecution;
    }

    /**
     * Overriding the execute method of order interface to deploy order.
     *
     * @param p_gameState current game state.
     */
    @Override
    public void execute(GameState p_gameState) {

        if (isValid(p_gameState)) {
            for (Country l_country : p_gameState.getD_map().getD_countries()) {
                if (l_country.getD_countryName().equalsIgnoreCase(this.d_targetCountry)) {
                    Integer l_armiesToUpdate = l_country.getD_numberOfArmies() == null ? this.d_countOfArmies
                            : l_country.getD_numberOfArmies() + this.d_countOfArmies;
                    l_country.setD_numberOfArmies(l_armiesToUpdate);
                    this.setD_logOfOrderExecution(+l_armiesToUpdate
                                    + " armies have been deployed on country : " + l_country.getD_countryName(),
                            "default");
                }
            }

        } else {
            this.setD_logOfOrderExecution("Deploy Order = " + "deploy" + " " + this.d_targetCountry + " "
                    + this.d_countOfArmies + " is not executed because Target country: "
                    + this.d_targetCountry + " given in deploy command isn't occupied by the player : "
                    + d_playerName.getD_playerName(), "error");
            d_playerName.setD_noOfAllocatedArmies(
                    d_playerName.getD_noOfAllocatedArmies() + this.d_countOfArmies);
        }
        p_gameState.logUpdate(logOfOrderExecution(), "effect");
    }


}
