package Models;

import Utils.CommonUtil;

import java.util.*;
import java.util.Map;

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
        if (p_player.getD_noOfAllocatedArmies()>0) {
            Country l_weakestCountry = retrieveWeakestCountry(p_player);
            d_deployCountryList.add(l_weakestCountry);

            Random l_random = new Random();
            int l_armiesForDeployment = 1;
            if (p_player.getD_noOfAllocatedArmies()>1) {
                l_armiesForDeployment = l_random.nextInt(p_player.getD_noOfAllocatedArmies() - 1) + 1;
            }
            System.out.println("deploy " + l_weakestCountry.getD_countryName() + " " + l_armiesForDeployment);
            return String.format("deploy %s %d", l_weakestCountry.getD_countryName(), l_armiesForDeployment);
        } else{
            return createOrderAdvance(p_player, p_gameState);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderAdvance(Player p_player, GameState p_gameState) {
        int l_armyToSend;
        Random l_random = new Random();
        Country l_randomStartCountry = getRandomCountry(d_deployCountryList);
        System.out.println("Source country : "+ l_randomStartCountry.getD_countryName());

        Country l_weakestFinalCountry = retrieveWeakestNeighbor(l_randomStartCountry, p_gameState, p_player);
        if(l_weakestFinalCountry == null)
            return null;

        System.out.println("Target Country : "+l_weakestFinalCountry.getD_countryName());
        if (l_randomStartCountry.getD_numberOfArmies() > 1) {
            l_armyToSend = l_random.nextInt(l_randomStartCountry.getD_numberOfArmies() - 1) + 1;
        } else {
            l_armyToSend = 1;
        }

        System.out.println("advance " + l_randomStartCountry.getD_countryName() + " "
                + l_weakestFinalCountry.getD_countryName() + " " + l_armyToSend);
        return "advance " + l_randomStartCountry.getD_countryName() + " " + l_weakestFinalCountry.getD_countryName()
                + " " + l_armyToSend;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderCard(Player p_player, GameState p_gameState, String p_cardName) {
        int l_armyToSend;
        Random l_random = new Random();
        Country l_randomOwnCountry = getRandomCountry(p_player.getD_ownedCountries());

        if (l_randomOwnCountry.getD_numberOfArmies() > 1) {
            l_armyToSend = l_random.nextInt(l_randomOwnCountry.getD_numberOfArmies() - 1) + 1;
        } else {
            l_armyToSend = 1;
        }

        switch (p_cardName) {
            case "bomb":
                System.err.println("Benevolent players don't hurt anyone.");
                return "bomb" + " " + "false";
            case "blockade":
                return "blockade " + l_randomOwnCountry.getD_countryName();
            case "airlift":
                return "airlift " + l_randomOwnCountry.getD_countryName() + " "
                        + getRandomCountry(p_player.getD_ownedCountries()).getD_countryName() + " " + l_armyToSend;
            case "negotiate":
                return "negotiate " + retrieveRandomEnemyPlayer(p_player, p_gameState).getD_playerName();
        }
        return null;
    }

    /**
     * This function returns the benevolent player behavior.
     *
     * @return String player behavior
     */
    @Override
    public String getPlayerBehavior() {
        return "Benevolent";
    }

    /**
     * Retrieves a random enemy player.
     *
     * @param p_player    Player
     * @param p_gameState Gamestate
     * @return Player
     */
    private Player retrieveRandomEnemyPlayer(Player p_player, GameState p_gameState) {
        ArrayList<Player> l_listOfPlayers = new ArrayList<Player>();
        Random l_random = new Random();

        for (Player l_player : p_gameState.getD_playerList()) {
            if (!l_player.equals(p_player))
                l_listOfPlayers.add(p_player);
        }
        return l_listOfPlayers.get(l_random.nextInt(l_listOfPlayers.size()));
    }

    /**
     * Returns the weakest neighbor so the Starting country can advance armies to this weakest country.
     *
     * @param l_randomStartCountry Source country
     * @param p_gameState           GameState
     * @param p_player benevolent player
     * @return weakest neighbor
     */
    public Country retrieveWeakestNeighbor(Country l_randomStartCountry, GameState p_gameState, Player p_player) {
        List<Integer> l_neighborCountryIds = l_randomStartCountry.getD_neighborCountryIDs();
        List<Country> l_listOfNeighbors = new ArrayList<Country>();
        for (int l_index = 0; l_index < l_neighborCountryIds.size(); l_index++) {
            Country l_country = p_gameState.getD_map()
                    .getCountry(l_randomStartCountry.getD_neighborCountryIDs().get(l_index));
            if(p_player.getD_ownedCountries().contains(l_country))
                l_listOfNeighbors.add(l_country);
        }
        if(!CommonUtil.isCollectionEmpty(l_listOfNeighbors))
            return findWeakestCountry(l_listOfNeighbors);

        return null;
    }

    /**
     * Returns a random country.
     *
     * @param p_listOfCountries list of countries
     * @return return country
     */
    private Country getRandomCountry(List<Country> p_listOfCountries) {
        Random l_random = new Random();
        return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
    }

    /**
     * Retrieves the weakest Country where the benevolent player can deploy armies.
     *
     * @param p_player Player
     * @return weakest country
     */
    public Country retrieveWeakestCountry(Player p_player) {
        List<Country> l_playerOwnedCountries = p_player.getD_ownedCountries();
        Country l_country;
        l_country = findWeakestCountry(l_playerOwnedCountries);
        return l_country;
    }

    /**
     * This method finds the weakest country.
     *
     * @param l_listOfCountries list of countries
     * @return weakest country
     */
    public Country findWeakestCountry(List<Country> l_listOfCountries) {
        LinkedHashMap<Country, Integer> l_countryWithArmies = new LinkedHashMap<Country, Integer>();

        int l_smallestCountOfArmies;
        Country l_weakestCountry = null;
        // return the weakest country from player owned countries.
        for (Country l_country : l_listOfCountries) {
            l_countryWithArmies.put(l_country, l_country.getD_numberOfArmies());
        }
        l_smallestCountOfArmies = Collections.min(l_countryWithArmies.values());
        for (Map.Entry<Country, Integer> entry : l_countryWithArmies.entrySet()) {
            if (entry.getValue().equals(l_smallestCountOfArmies)) {
                return entry.getKey();
            }
        }
        return l_weakestCountry;
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
