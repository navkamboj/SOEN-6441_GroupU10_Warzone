package Models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class for Random Player who plays random moves by deploying his armies randomly
 * Moves armies in his own territories randomly
 * Attacks random neighboring countries
 *
 * @author Navjot Kamboj
 * @version 3.0.0
 */

public class RandomPlayer extends PlayerBehaviorStrategy{

    /**
     * List containing deploy order countries.
     */
    ArrayList<Country> d_deployOrderCountries = new ArrayList<Country>();

    /**
     * Method to create a new order.
     *
     * @param p_player    Player class object
     * @param p_gameState GameState class object
     *
     * @return Order object of order class
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) throws IOException {
        System.out.println("Creating order for : " + p_player.getD_playerName());
        String l_commandEntered;
        if (!verifyIfArmiesDeployed(p_player)) {
            if(p_player.getD_noOfAllocatedArmies()>0) {
                l_commandEntered = createOrderDeploy(p_player, p_gameState);
            }else{
                l_commandEntered = createOrderAdvance(p_player, p_gameState);
            }
        } else {
            if(p_player.getD_cardsOwned().size()>0){
                int l_index = (int) (Math.random() * 3) +1;
                switch (l_index) {
                    case 1:
                        l_commandEntered = createOrderDeploy(p_player, p_gameState);
                        break;
                    case 2:
                        l_commandEntered = createOrderAdvance(p_player, p_gameState);
                        break;
                    case 3:
                        if (p_player.getD_cardsOwned().size() == 1) {
                            l_commandEntered = createOrderCard(p_player, p_gameState, p_player.getD_cardsOwned().get(0));
                            break;
                        } else {
                            Random l_random = new Random();
                            int l_randomIndex = l_random.nextInt(p_player.getD_cardsOwned().size());
                            l_commandEntered = createOrderCard(p_player, p_gameState, p_player.getD_cardsOwned().get(l_randomIndex));
                            break;
                        }
                    default:
                        l_commandEntered = createOrderAdvance(p_player, p_gameState);
                        break;
                }
            } else{
                Random l_random = new Random();
                Boolean l_randomBoolean = l_random.nextBoolean();
                if(l_randomBoolean){
                    l_commandEntered = createOrderDeploy(p_player, p_gameState);
                }else{
                    l_commandEntered = createOrderAdvance(p_player, p_gameState);
                }
            }
        }
        return l_commandEntered;
    }

    @Override
    public String createOrderDeploy(Player p_player, GameState p_gameState) {
        return null;
    }

    @Override
    public String createOrderAdvance(Player p_player, GameState p_gameState) {
        return null;
    }

    @Override
    public String createOrderCard(Player p_player, GameState p_gameState, String p_cardName) {
        return null;
    }

    @Override
    public String getPlayerBehavior() {
        return null;
    }

    /**
     * Check if it is first turn of the player.
     *
     * @param p_player player instance
     * @return boolean
     */
    private Boolean verifyIfArmiesDeployed(Player p_player){
        if(p_player.getD_ownedCountries().stream().anyMatch(l_country -> l_country.getD_numberOfArmies()>0)){
            return true;
        }
        return false;
    }
}
