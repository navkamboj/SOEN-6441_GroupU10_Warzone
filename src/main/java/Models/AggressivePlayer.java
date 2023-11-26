package Models;

import java.util.ArrayList;
import java.util.Random;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.List;
import java.util.Collections;


/**
 * This is the class of Aggressive Player,this player gathers all his armies and
 * attacks from his the strongest territory and maximize his force on one country for winning purpose.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class AggressivePlayer extends PlayerBehaviorStrategy{
    /**
     * Deploy order countryList.
     */
    ArrayList<Country> d_deployCountryList = new ArrayList<Country>();

    /**
     * Create a new order
     *
     * @param p_player    Player class object
     * @param p_gameState Game state class object
     *
     * @return order in the form of string
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) {
        System.out.println("Order is created for Player : " + p_player.getD_playerName());
        String l_createCommand;
        Random l_randomNumber = new Random();
        if (!checkDeploymentOfArmies(p_player)) {
            if(p_player.getD_noOfAllocatedArmies()>0) {
                l_createCommand = createOrderDeploy(p_player, p_gameState);
            }else{
                l_createCommand = createOrderAdvance(p_player, p_gameState);
            }
        } else {
            if(p_player.getD_cardsOwned().size()>0){
                System.out.println("Enter card logic");
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
                        if(p_player.getD_cardsOwned().size() == 1) {
                            System.out.println("Cards!");
                            l_createCommand = createOrderCard(p_player, p_gameState, p_player.getD_cardsOwned().get(0));
                            break;
                        } else {
                            int l_randomIndex = l_randomNumber.nextInt(p_player.getD_cardsOwned().size());
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
        Random l_random = new Random();
        // process of getting strong country and deploy
        Country l_mostPowerfulCountry = getMostPowerfulCountry(p_player, p_gameState);
        d_deployCountryList.add(l_mostPowerfulCountry);
        int l_numberOfArmiesToDeploy = 1;
        if (p_player.getD_noOfAllocatedArmies()>1) {
            l_numberOfArmiesToDeploy = l_random.nextInt(p_player.getD_noOfAllocatedArmies() - 1) + 1;
        }
        return String.format("deploy %s %d", l_mostPowerfulCountry.getD_countryName(), l_numberOfArmiesToDeploy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderAdvance(Player p_player, GameState p_gameState) {
        //armies are moved from neighbors country to source country to maximize number of armies
        Country l_arbitrarySourceCountry = getArbitraryCountry(d_deployCountryList);
        moveArmiesFromItsNeighbors(p_player, l_arbitrarySourceCountry, p_gameState);

        Random l_random = new Random();
        Country l_arbitraryTargetCountry = p_gameState.getD_map()
                .getCountry(l_arbitrarySourceCountry.d_neighborCountryIDs
                        .get(l_random.nextInt(l_arbitrarySourceCountry.getD_neighborCountryIDs().size())));

        int l_dispatchArmies = l_arbitrarySourceCountry.getD_numberOfArmies() > 1 ? l_arbitrarySourceCountry.getD_numberOfArmies() : 1;

        // Strongest country will attack
        return "advance " + l_arbitrarySourceCountry.getD_countryName() + " " + l_arbitraryTargetCountry.getD_countryName()
                + " " + l_dispatchArmies;
    }

    /**
     *Armies will move from neighbor to source country for maximization of forces.
     *
     * @param p_player              Player
     * @param p_arbitrarySourceCountry Source country
     * @param p_gameState           Game state
     */
    public void moveArmiesFromItsNeighbors(Player p_player, Country p_arbitrarySourceCountry, GameState p_gameState) {
        List<Integer> l_neighboringCountryIds = p_arbitrarySourceCountry.getD_neighborCountryIDs();
        List<Country> l_neighborList = new ArrayList<Country>();
        for (int l_index = 0; l_index < l_neighboringCountryIds.size(); l_index++) {
            Country l_countryName = p_gameState.getD_map()
                    .getCountry(p_arbitrarySourceCountry.getD_neighborCountryIDs().get(l_index));
            // if neighbor belong to player then it will be added to the list
            if (p_player.getD_ownedCountries().contains(l_countryName)) {
                l_neighborList.add(l_countryName);
            }
        }

        int l_moveArmies = 0;
        // armies are moved from neighbor to source country
        for (Country l_country : l_neighborList) {
            l_moveArmies += p_arbitrarySourceCountry.getD_numberOfArmies() > 0
                    ? p_arbitrarySourceCountry.getD_numberOfArmies() + (l_country.getD_numberOfArmies())
                    : (l_country.getD_numberOfArmies());
        }
        p_arbitrarySourceCountry.setD_numberOfArmies(l_moveArmies);
    }

    /**
     * Arbitrary country will be return by this method.
     *
     * @param p_countryList list of countries
     * @return return country
     */
    private Country getArbitraryCountry(List<Country> p_countryList) {
        Random l_random = new Random();
        return p_countryList.get(l_random.nextInt(p_countryList.size()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderCard(Player p_player, GameState p_gameState, String p_nameOfCard) {
        Random l_random = new Random();
        Country l_MostPowerfulSourceCountry = getMostPowerfulCountry(p_player, d_gameState);

        Country l_arbitraryTargetCountry = p_gameState.getD_map()
                .getCountry(l_MostPowerfulSourceCountry.getD_neighborCountryIDs()
                        .get(l_random.nextInt(l_MostPowerfulSourceCountry.getD_neighborCountryIDs().size())));

        int l_armiesToDispatch = l_MostPowerfulSourceCountry.getD_numberOfArmies() > 1 ? l_MostPowerfulSourceCountry.getD_numberOfArmies() : 1;

        switch (p_nameOfCard) {
            case "bomb":
                return "bomb " + l_arbitraryTargetCountry.getD_countryName();
            case "airlift":
                return "airlift " + l_MostPowerfulSourceCountry.getD_countryName() + " "
                        + getArbitraryCountry(p_player.getD_ownedCountries()).getD_countryName() + " " + l_armiesToDispatch;
            case "negotiate":
                return "negotiate" + " " + getArbitraryEnemyPlayer(p_player, p_gameState).getD_playerName();
            case "blockade":
                return "blockade " + l_MostPowerfulSourceCountry.getD_countryName();
        }
        return null;
    }

    /**
     * This method will return Arbitrary enemy player.
     *
     * @param p_player    Player
     * @param p_gameState Game state
     * @return random enemy player
     */
    private Player getArbitraryEnemyPlayer(Player p_player, GameState p_gameState) {
        ArrayList<Player> l_players = new ArrayList<Player>();
        Random l_random = new Random();

        for (Player l_player : p_gameState.getD_playerList()) {
            if (!l_player.equals(p_player))
                l_players.add(p_player);
        }
        return l_players.get(l_random.nextInt(l_players.size()));
    }

    /**
     * Player behavior is return by this method
     *
     * @return Behavior of player as a string
     */
    @Override
    public String getPlayerBehavior() {
        return "Aggressive";
    }

    /**
     * Get the most powerful country
     *
     * @param p_player    Player
     * @param p_gameState Game state
     * @return the most powerful country
     */
    public Country getMostPowerfulCountry(Player p_player, GameState p_gameState) {
        List<Country> l_ownedCountryList = p_player.getD_ownedCountries();
        Country l_CountryName;
        l_CountryName = findMostPowerfulCountry(l_ownedCountryList);
        return l_CountryName;
    }

    /**
     * This method will return country with the highest armies.
     *
     * @param l_listOfCountries List of countries
     * @return strongest country
     */
    public Country findMostPowerfulCountry(List<Country> l_listOfCountries) {
        LinkedHashMap<Country, Integer> l_armiesOfCountry = new LinkedHashMap<Country,Integer>();

        int l_maximumArmies;
        Country l_CountryName = null;
        // Among the owned countries of player return the most powerful country.
        for (Country l_country : l_listOfCountries) {
            l_armiesOfCountry.put(l_country, l_country.getD_numberOfArmies());
        }
        l_maximumArmies = Collections.max(l_armiesOfCountry.values());
        for (Entry<Country, Integer> entry : l_armiesOfCountry.entrySet()) {
            if (entry.getValue().equals(l_maximumArmies)) {
                return entry.getKey();
            }
        }
        return l_CountryName;
    }

    /**
     * check that is it a first turn or not
     *
     * @param p_player object of player class
     * @return boolean
     */
    private Boolean checkDeploymentOfArmies(Player p_player){
        if(!p_player.getD_ownedCountries().stream().anyMatch(l_country -> l_country.getD_numberOfArmies()>0)){
            return false;
        }
        else {
            return true;
        }
    }
}