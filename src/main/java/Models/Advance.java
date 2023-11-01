package Models;
import java.util.*;

import Utils.CommonUtil;
import Services.PlayerService;
public class Advance implements Order {
    Player d_playerName;
    String d_logOfOrderExecution;
    String d_targetCountry;
    String d_sourceCountry;
    Integer d_countOfArmies;

    /**
     * Parameterized constructor to initialize all data members required to execute an order
     *
     * @param d_playerName player name that provides orders
     * @param d_sourceCountry source country that attacks
     * @param d_targetCountry target country that gets attacked
     * @param d_countOfArmies number of armies to be deployed
     */
    public Advance(Player d_playerName, String d_sourceCountry, String d_targetCountry,
                   Integer d_countOfArmies) {
        this.d_playerName = d_playerName;
        this.d_sourceCountry = d_sourceCountry;
        this.d_targetCountry = d_targetCountry;
        this.d_countOfArmies = d_countOfArmies;
    }

    /**
     * Finds and return the player that holds the target country.
     *
     * @param p_gameState current state of the game
     * @return target country player
     */
    private Player getPlayerHoldingTheTargetCountry(GameState p_gameState) {
        Player l_playerHoldingTargetCountry = null;
        for (Player l_temp_player : p_gameState.getD_playerList()) {
            String l_cont = l_temp_player.getNamesOfCountries().stream()
                    .filter(l_country -> l_country.equalsIgnoreCase(this.d_targetCountry)).findFirst().orElse(null);
            if (!CommonUtil.isEmpty(l_cont)) {
                l_playerHoldingTargetCountry = l_temp_player;
            }
        }
        return l_playerHoldingTargetCountry;
    }

    /**
     * Attack if territory belongs to other player or just move if belongs to same player.
     *
     * @param p_targetCountry name of country where armies to be deployed
     */
    public void deployArmiesOnTargetCountry(Country p_targetCountry) {
        Integer l_CntOfArmies = p_targetCountry.getD_numberOfArmies() + this.d_countOfArmies;
        p_targetCountry.setD_numberOfArmies(l_CntOfArmies);
    }

    /**
     * Displays and set the log of execution of orders
     *
     * @param p_logOfOrderExecution String log
     * @param p_logType different types of log i.e. default or error
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
     * returns log of orders executed till now.
     */
    @Override
    public String logOfOrderExecution() {
        return this.d_logOfOrderExecution;
    }

    /**
     * Modify Continents holdings after an attack.
     *
     * @param p_playerHoldingSourceCountry player owning source country
     * @param p_playerHoldingTargetCountry player owning target country
     * @param p_gameState             current state of the game
     */
    private void modifyContinents(Player p_playerHoldingSourceCountry, Player p_playerHoldingTargetCountry,
                                  GameState p_gameState) {
        System.out.println("Modifying continents of players associated in battle...");
        List<Player> l_playersList = new ArrayList<>();
        p_playerHoldingSourceCountry.setD_ownedContinents(new ArrayList<>());
        p_playerHoldingTargetCountry.setD_ownedContinents(new ArrayList<>());
        l_playersList.add(p_playerHoldingSourceCountry);
        l_playersList.add(p_playerHoldingTargetCountry);

        PlayerService l_playerService = new PlayerService();
        l_playerService.assignmentOfContinents(l_playersList, p_gameState.getD_map().getD_continents());
    }

    /**
     * Occupy Target Country when defeated.
     *
     * @param p_gameState Current state of the game
     * @param p_playerHoldingTargetCountry player holding the target country
     * @param p_targetCountry target country
     */
    private void occupyTargetCountry(GameState p_gameState, Player p_playerHoldingTargetCountry, Country p_targetCountry) {
        p_targetCountry.setD_numberOfArmies(d_countOfArmies);
        p_playerHoldingTargetCountry.getD_ownedCountries().remove(p_targetCountry);
        this.d_playerName.getD_ownedCountries().add(p_targetCountry);
        this.setD_logOfOrderExecution(
                "Player : " + this.d_playerName.getD_playerName() + " is assigned with Country : "
                        + p_targetCountry.getD_countryName() + " and armies : " + p_targetCountry.getD_numberOfArmies(),
                "default");
        p_gameState.logUpdate(logOfOrderExecution(), "effect");
        this.modifyContinents(this.d_playerName, p_playerHoldingTargetCountry, p_gameState);
    }


    /**
     * Implemented the execute method of order interface.
     *
     * @param p_gameState current state of the game
     */

    @Override
    public void execute(GameState p_gameState) {
        if (isValid(p_gameState)) {
            Player l_playerOfTargetCountry = getPlayerHoldingTheTargetCountry(p_gameState);
            Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountry);
            Country l_sourceCountry = p_gameState.getD_map().getCountryByName(d_sourceCountry);
            Integer l_sourceArmiesToUpdate = l_sourceCountry.getD_numberOfArmies() - this.d_countOfArmies;
            l_sourceCountry.setD_numberOfArmies(l_sourceArmiesToUpdate);

            if (l_playerOfTargetCountry.getD_playerName().equalsIgnoreCase(this.d_playerName.getD_playerName())) {
                deployArmiesOnTargetCountry(l_targetCountry);
            } else if (l_targetCountry.getD_numberOfArmies() == 0) {
                occupyTargetCountry(p_gameState, l_playerOfTargetCountry, l_targetCountry);
                this.d_playerName.assignCard();
            } else {
                produceOrderResult(p_gameState, l_playerOfTargetCountry, l_targetCountry, l_sourceCountry);
            }
        } else {
            p_gameState.logUpdate(orderExecutionLog(), "effect");
        }
    }


}
