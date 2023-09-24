package Models;

/**
 * This model class manages the orders given by the players.
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
     * getter method to get order actions.
     *
     * @return order action
     */
    public String getD_orderAction() {
        return d_orderAction;
    }

    /**
     * setter method to set the order actions.
     *
     * @param p_orderAction order action
     */
    public void setD_orderAction(String p_orderAction) {
        this.d_orderAction = p_orderAction;
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
     * getter method to get the source country name.
     *
     * @return source country name
     */
    public String getD_sourceCountry() {
        return d_sourceCountry;
    }

    /**
     * setter method to set the source country name.
     *
     * @param p_sourceCountry source country name
     */
    public void setD_sourceCountry(String p_sourceCountry) {
        this.d_sourceCountry = p_sourceCountry;
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
 }
