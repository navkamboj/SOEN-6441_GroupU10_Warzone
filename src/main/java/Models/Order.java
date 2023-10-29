package Models;

/**
 * This model class manages the orders given by the players.
 *
 * @author Pranjalesh Ghansiyal
 * @version 1.0.0
 */
public class Order {
    /**
     * order actions.
     */
    String d_orderAction;

    /**
     * name of the target country.
     */
    String d_targetCountry;

    /**
     * name of the source country.
     */
    String d_sourceCountry;

    /**
     * number of armies to be deployed.
     */

    Integer d_armiesToDeploy;

    /**
     * default constructor.
     */
    public Order() {
    }

    /**
     * parameterized constructor.
     *
     * @param p_orderAction order action
     * @param p_targetCountry name of the target country
     * @param p_armiesToDeploy number of armies to be deployed
     */
    public Order(String p_orderAction, String p_targetCountry, Integer p_armiesToDeploy) {
        this.d_orderAction = p_orderAction;
        this.d_targetCountry = p_targetCountry;
        this.d_armiesToDeploy = p_armiesToDeploy;
    }

    /**
     * getter method to get the target country name.
     * @return target country name
     */
    public String getD_targetCountry() {
        return d_targetCountry;
    }

    /**
     * setter method to set the target country name.
     *
     * @param p_targetCountry target country name
     */
    public void setD_targetCountry(String p_targetCountry) {
        this.d_targetCountry = p_targetCountry;
    }

    /**
     * getter method to get the number of armies to be deployed.
     *
     * @return number of armies to be deployed
     */
    public Integer getD_armiesToDeploy() {
        return d_armiesToDeploy;
    }

    /**
     * setter method to set the number of armies to be deployed.
     *
     * @param p_armiesToDeploy number of armies to be deployed
     */
    public void setD_armiesToDeploy(Integer p_armiesToDeploy) {
        this.d_armiesToDeploy = p_armiesToDeploy;
    }

    /**
     * Acts upon the order object and make necessary changes in the game state.
     *
     * @param p_gameState current state of the game
     * @param p_player player whose order is being executed
     */
    public void execute(GameState p_gameState, Player p_player) {
        switch (this.d_orderAction) {
            case "deploy": {
                if (this.verifyDeployCountryOrder(p_player, this)) {
                    this.executeDeployOrder(this, p_gameState, p_player);
                    System.out.println("\nSuccessfully executed the given order : " + this.getD_armiesToDeploy()
                            + " armies have been deployed to the country : " + this.getD_targetCountry());
                }
                else {
                    System.out.println("\nOrder has not been executed as target country provided in the deploy command"
                            + " does not belong to player : " + p_player.getD_playerName());
                }
                break;
            }
            default: {
                System.out.println("Order was not executed because an invalid command was provided.");
            }
        }
    }

    /**
     * Verify if the country given for deploy command belongs to a player's country or not.
     *
     * @param p_player player whose order is being executed.
     * @param p_order order which is being executed.
     * @return true/false
     */
    public boolean verifyDeployCountryOrder(Player p_player, Order p_order) {
        Country l_country = p_player.getD_ownedCountries().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(p_order.getD_targetCountry())).findFirst()
                .orElse(null);
        return l_country != null;
    }

    /**
     * Executes deploy order and updates game state with latest map.
     *
     * @param p_order order which is being executed
     * @param p_gameState current state of the game
     * @param p_player player whose order is being executed
     */
    public void executeDeployOrder(Order p_order, GameState p_gameState, Player p_player) {
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            if (l_country.getD_countryName().equalsIgnoreCase(p_order.getD_targetCountry())) {
                Integer l_armiesToUpdate = l_country.getD_numberOfArmies() == null ? p_order.getD_armiesToDeploy()
                        : l_country.getD_numberOfArmies() + p_order.getD_armiesToDeploy();
                l_country.setD_numberOfArmies(l_armiesToUpdate);
            }
        }
    }
 }
