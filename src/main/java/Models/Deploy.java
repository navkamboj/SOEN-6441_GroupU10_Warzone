package Models;

import java.io.Serializable;

/**
 * Implemented Command pattern.
 *
 * @author Harsh Tank
 * @version 2.0.0
 *
 */

public class Deploy implements Order, Serializable {

    /**
     * Player Initiator.
     */
    Player d_playerName;

    /**
     * Sets the Log containing Information about orders.
     */
    String d_logOfOrderExecution;

    /**
     * name of the target country.
     */
    String d_targetCountry;

    /**
     * number of armies to be placed.
     */
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
     * Display deploy Order.
     */
    @Override
    public void printTheOrder() {
        this.d_logOfOrderExecution = "\n---------- Deploy order given by player " + this.d_playerName.getD_playerName()+" ----------\n"+System.lineSeparator()+"Deploy " + this.d_countOfArmies + " armies to " + this.d_targetCountry;
        System.out.println(this.d_logOfOrderExecution);
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


    /**
     * Checks if deploying country is owned by player or not
     */
    @Override
    public boolean isValid(GameState p_gameState) {
        Country l_tempCountry = d_playerName.getD_ownedCountries().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_targetCountry.toString()))
                .findFirst().orElse(null);
        return l_tempCountry != null;
    }

    /**
     * Retrieves name of order.
     *
     * @return String
     */
    @Override
    public String orderName() {
        return "deploy";
    }

}
