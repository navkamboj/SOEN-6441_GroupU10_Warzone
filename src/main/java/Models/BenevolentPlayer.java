package Models;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is the Benevolent Player class which will focus on defending and will never attack.
 *
 * @author Pranjalesh Ghansiyal
 * @version 3.0.0
 */
public class BenevolentPlayer extends PlayerBehaviorStrategy{

    /**
     * Deploy order country list
     *
     */
    ArrayList<Country> d_deployCountryList = new ArrayList<Country>();

    /**
     * This creates a new order.
     *
     * @param p_player Player class object
     * @param p_gameState GameState class object
     *
     * @return order object
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) {
        System.out.println("Order is created for Player : " + p_player.getD_playerName());
        String l_createCommand;
        if (!checkDeploymentOfArmies(p_player)) {
            if(p_player.getD_noOfAllocatedArmies()>0) {
                l_createCommand = createOrderDeploy(p_player, p_gameState);
            }else{
                l_createCommand = createOrderAdvance(p_player, p_gameState);
            }
        } else {
            if (p_player.getD_cardsOwned().size() > 0) {
                System.out.println("Enter card logic.");
                int l_switchIndex = (int) (Math.random() * 3) +1;
                switch (l_switchIndex) {
                    case 1:
                        System.out.println("Deploy!");
                        l_createCommand = createOrderDeploy(p_player, p_gameState);
                        break;
                    case 2:
                        System.out.println("Advance!");
                        l_createCommand = createOrderAdvance(p_player, p_gameState);
                        break;
                    case 3:
                        if (p_player.getD_cardsOwned().size() == 1) {
                            System.out.println("Cards!");
                            l_createCommand = createOrderCard(p_player, p_gameState, p_player.getD_cardsOwned().get(0));
                            break;
                        } else {
                            Random l_random = new Random();
                            int l_randomIndex = l_random.nextInt(p_player.getD_cardsOwned().size());
                            l_createCommand = createOrderCard(p_player, p_gameState, p_player.getD_cardsOwned().get(l_randomIndex));
                            break;
                        }
                    default:
                        l_createCommand = createOrderAdvance(p_player, p_gameState);
                        break;
                }
            } else{
                Random l_random = new Random();
                Boolean l_randomBoolean = l_random.nextBoolean();
                if(l_randomBoolean){
                    System.out.println("In the absence of Card Deploy Logic");
                    l_createCommand = createOrderDeploy(p_player, p_gameState);
                }else{
                    System.out.println("In the absence of Card Advance Logic");
                    l_createCommand = createOrderAdvance(p_player, p_gameState);
                }
            }
        }
        return l_createCommand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderDeploy(Player p_player, GameState p_gameState) {
//        if (p_player.getD_noOfAllocatedArmies()>0)
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
     * This method checks if it is first turn.
     *
     * @param p_player player instance
     * @return boolean
     */
    private Boolean checkDeploymentOfArmies(Player p_player){
        if(p_player.getD_ownedCountries().stream().anyMatch(l_country -> l_country.getD_numberOfArmies()>0)){
            return true;
        }
        return false;
    }
}
