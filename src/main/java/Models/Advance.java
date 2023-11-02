package Models;
import java.util.*;

import Utils.CommonUtil;
import Services.PlayerService;

/**
 * Implemented Command pattern.
 *
 * @author Harsh Tank
 * @version 2.0.0
 */
public class Advance implements Order {
    Player d_playerName;
    String d_logOfOrderExecution;
    String d_targetCountry;
    String d_sourceCountry;
    Integer d_countOfArmies;

    /**
     * Parameterized constructor to initialize all data members required to execute an order
     *
     * @param p_playerName player name that provides orders
     * @param p_sourceCountry source country that attacks
     * @param p_targetCountry target country that gets attacked
     * @param p_countOfArmies number of armies to be deployed
     */
    public Advance(Player p_playerName, String p_sourceCountry, String p_targetCountry,
                   Integer p_countOfArmies) {
        this.d_playerName = p_playerName;
        this.d_sourceCountry = p_sourceCountry;
        this.d_targetCountry = p_targetCountry;
        this.d_countOfArmies = p_countOfArmies;
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
     * returns random integer between a given range.
     *
     * @param p_min minimum limit
     * @param p_max maximum limit
     * @return int random number
     */
    private static int getRandomNumber(int p_max, int p_min) {
        return ((int) (Math.random() * (p_max - p_min))) + p_min;
    }


    /**
     * Based on winning probability, generates armies
     *
     * @param p_count count of armies to be generated
     * @param p_armyFor armies for defender or attacker
     * @return List random army units based on probability
     */
    private List<Integer> generateRandomUnitsOfArmy(int p_count, String p_armyFor) {
        List<Integer> l_tempArmies = new ArrayList<>();
        Double l_probability = "attacker".equalsIgnoreCase(p_armyFor) ? 0.6 : 0.7;
        for (int l_i = 0; l_i < p_count; l_i++) {
            int l_randomNumber = getRandomNumber(10, 1);
            Integer l_armyUnit = (int) Math.round(l_randomNumber * l_probability);
            l_tempArmies.add(l_armyUnit);
        }
        return l_tempArmies;
    }

    /**
     * Execute Remaining armies and deciding the holdings of territories
     *
     * @param p_attackerRemainingArmies    remaining attacking armies from battle
     * @param p_defenderRemainingArmies    remaining defending armies from battle
     * @param p_sourceCountry         source country
     * @param p_targetCountry         target country
     * @param p_playerHoldingTargetCountry player owning the target country
     */
    public void handleRemainingArmies(Integer p_attackerRemainingArmies, Integer p_defenderRemainingArmies,
                                      Country p_sourceCountry, Country p_targetCountry, Player p_playerHoldingTargetCountry) {
        if (p_defenderRemainingArmies == 0) {
            p_playerHoldingTargetCountry.getD_ownedCountries().remove(p_targetCountry);
            p_targetCountry.setD_numberOfArmies(p_attackerRemainingArmies);
            this.d_playerName.getD_ownedCountries().add(p_targetCountry);
            this.setD_logOfOrderExecution(
                    "Player : " + this.d_playerName.getD_playerName() + " is assigned with Country : "
                            + p_targetCountry.getD_countryName() + " and armies : " + p_targetCountry.getD_numberOfArmies(),
                    "default");

            this.d_playerName.assignCard();
        } else {
            p_targetCountry.setD_numberOfArmies(p_defenderRemainingArmies);

            Integer l_sourceArmiesToUpdate = p_sourceCountry.getD_numberOfArmies() + p_attackerRemainingArmies;
            p_sourceCountry.setD_numberOfArmies(l_sourceArmiesToUpdate);
            String l_country1 = "Country : " + p_targetCountry.getD_countryName() + " is left with "

                    + p_targetCountry.getD_numberOfArmies() + " armies and is still controlled by player : "
                    + p_playerHoldingTargetCountry.getD_playerName();
            String l_country2 = "Country : " + p_sourceCountry.getD_countryName() + " is left with "
                    + p_sourceCountry.getD_numberOfArmies() + " armies and is still controlled by player : "
                    + this.d_playerName.getD_playerName();
            this.setD_logOfOrderExecution(l_country1 + System.lineSeparator() + l_country2, "default");
        }
    }

    /**
     * Give the recent executed advance order.
     *
     * @return advance order command
     */
    private String recentOrder() {
        return "Advance Order : " + "advance" + " " + this.d_sourceCountry + " " + this.d_targetCountry + " "
                + this.d_countOfArmies;
    }

    /**
     * Display info related to order.
     */
    @Override
    public void printTheOrder() {
        this.d_logOfOrderExecution = "\n---------- Advance order issued by player " + this.d_playerName.getD_playerName()
                + " ----------\n" + System.lineSeparator() + "Move " + this.d_countOfArmies + " armies from "
                + this.d_sourceCountry + " to " + this.d_targetCountry;
        System.out.println(System.lineSeparator() + this.d_logOfOrderExecution);
    }

    /**
     * Validates whether country given for deploy belongs to players countries or
     * not.
     *
     * @return boolean if given advance command is valid or not.
     */

    @Override
    public boolean isValid(GameState p_gameState) {
        Country l_country = d_playerName.getD_ownedCountries().stream()
                .filter(l_pl -> l_pl.getD_countryName().equalsIgnoreCase(this.d_sourceCountry.toString()))
                .findFirst().orElse(null);
        if (l_country == null) {
            this.setD_logOfOrderExecution(this.recentOrder() + " is not executed since Source country : "
                    + this.d_sourceCountry + " given in advance command does not belongs to the player : "
                    + d_playerName.getD_playerName(), "error");
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
            return false;
        }
        if (this.d_countOfArmies > l_country.getD_numberOfArmies()) {
            this.setD_logOfOrderExecution(this.recentOrder()
                    + " is not executed as armies given in advance order exceeds armies of source country : "
                    + this.d_sourceCountry, "error");
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
            return false;
        }
        if (this.d_countOfArmies == l_country.getD_numberOfArmies()) {
            this.setD_logOfOrderExecution(this.recentOrder() + " is not executed as source country : "
                            + this.d_sourceCountry + " has " + l_country.getD_numberOfArmies()
                            + " army units and all of those cannot be given advance order, atleast one army unit has to retain the territory.",
                    "error");
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
            return false;
        }
        if(!d_playerName.negotiationValidation(this.d_targetCountry)){
            this.setD_logOfOrderExecution(this.recentOrder() + " is not executed as "+ d_playerName.getD_playerName()+ " has negotiation pact with the target country's player!", "error");
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
            return false;
        }
        return true;
    }


    /**
     * generate the result of battle between attacker and defender
     *
     * @param p_sourceCountry source country from which armies have to be moved
     * @param p_targetCountry target country to which armies have been moved
     * @param p_armiesOfAttacker random army count of attacker
     * @param p_armiesOfDefender random army count of defender
     * @param p_playerHoldingTargetCountry player holding the target country
     */
    private void generateBattleResult(Country p_sourceCountry, Country p_targetCountry, List<Integer> p_armiesOfAttacker,
                                     List<Integer> p_armiesOfDefender, Player p_playerHoldingTargetCountry) {
        Integer l_attackerRemainingArmies = this.d_countOfArmies > p_targetCountry.getD_numberOfArmies()
                ? this.d_countOfArmies - p_targetCountry.getD_numberOfArmies()
                : 0;
        Integer l_defenderRemainingArmies = this.d_countOfArmies < p_targetCountry.getD_numberOfArmies()
                ? p_targetCountry.getD_numberOfArmies() - this.d_countOfArmies
                : 0;
        for (int l_i = 0; l_i < p_armiesOfAttacker.size(); l_i++) {
            if (p_armiesOfAttacker.get(l_i) > p_armiesOfDefender.get(l_i)) {
                l_attackerRemainingArmies++;
            } else {
                l_defenderRemainingArmies++;
            }
        }
        this.handleRemainingArmies(l_attackerRemainingArmies, l_defenderRemainingArmies, p_sourceCountry, p_targetCountry,
                p_playerHoldingTargetCountry);
    }


    /**
     * generate result after advance order.
     *
     * @param p_gameState current state of the game
     * @param p_playerHoldingTargetCountry player holding the target country
     * @param p_targetCountry target country given in order
     * @param p_sourceCountry source country given in order
     */
    private void generateOrderResult(GameState p_gameState, Player p_playerHoldingTargetCountry, Country p_targetCountry,
                                    Country p_sourceCountry) {
        Integer l_armiesInAttack = this.d_countOfArmies < p_targetCountry.getD_numberOfArmies()
                ? this.d_countOfArmies
                : p_targetCountry.getD_numberOfArmies();

        List<Integer> l_armiesOfAttacker = generateRandomUnitsOfArmy(l_armiesInAttack, "attacker");
        List<Integer> l_armiesOfDefender = generateRandomUnitsOfArmy(l_armiesInAttack, "defender");
        this.generateBattleResult(p_sourceCountry, p_targetCountry, l_armiesOfAttacker, l_armiesOfDefender,
                p_playerHoldingTargetCountry);

        p_gameState.logUpdate(logOfOrderExecution(), "effect");
        this.modifyContinents(this.d_playerName, p_playerHoldingTargetCountry, p_gameState);
    }

    /**
     * Retrieve order name.
     */
    @Override
    public String orderName() {
        return "advance";
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
                generateOrderResult(p_gameState, l_playerOfTargetCountry, l_targetCountry, l_sourceCountry);
            }
        } else {
            p_gameState.logUpdate(logOfOrderExecution(), "effect");
        }
    }
}
