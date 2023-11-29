package Models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
                Random l_randomNum = new Random();
                Boolean l_randomBool = l_randomNum.nextBoolean();
                if(l_randomBool){
                    l_commandEntered = createOrderDeploy(p_player, p_gameState);
                }else{
                    l_commandEntered = createOrderAdvance(p_player, p_gameState);
                }
            }
        }
        return l_commandEntered;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderDeploy(Player p_player, GameState p_gameState) {
        if (p_player.getD_noOfAllocatedArmies()>0) {
            Random l_randomNum = new Random();
            System.out.println(p_player.getD_ownedCountries().size());
            Country l_randomCountry = getRandomCountry(p_player.getD_ownedCountries());
            d_deployOrderCountries.add(l_randomCountry);
            int l_armiesToBeDeployed = 1;
            if (p_player.getD_noOfAllocatedArmies()>1) {
                l_armiesToBeDeployed = l_randomNum.nextInt(p_player.getD_noOfAllocatedArmies() - 1) + 1;
            }
            return String.format("deploy %s %d", l_randomCountry.getD_countryName(), l_armiesToBeDeployed);
        } else {
            return createOrderAdvance(p_player,p_gameState);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderAdvance(Player p_player, GameState p_gameState) {
        int l_armiesToBeSent;
        Random l_random = new Random();
        Country l_randomOwnCountry = getRandomCountry(d_deployOrderCountries);
        int l_randomIndex = l_random.nextInt(l_randomOwnCountry.getD_neighborCountryIDs().size());
        Country l_randomNeighbor;
        if (l_randomOwnCountry.getD_neighborCountryIDs().size()>1) {
            l_randomNeighbor = p_gameState.getD_map().getCountry(l_randomOwnCountry.getD_neighborCountryIDs().get(l_randomIndex));
        } else {
            l_randomNeighbor = p_gameState.getD_map().getCountry(l_randomOwnCountry.getD_neighborCountryIDs().get(0));
        }

        if (l_randomOwnCountry.getD_numberOfArmies()>1) {
            l_armiesToBeSent = l_random.nextInt(l_randomOwnCountry.getD_numberOfArmies() - 1) + 1;
        } else {
            l_armiesToBeSent = 1;
        }
        return "advance "+l_randomOwnCountry.getD_countryName()+" "+l_randomNeighbor.getD_countryName()+" "+ l_armiesToBeSent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderCard(Player p_player, GameState p_gameState, String p_cardName) {
        int l_armiesToBeSent;
        Random l_random = new Random();
        Country l_randomOwnCountry = getRandomCountry(p_player.getD_ownedCountries());

        Country l_randomNeighbour = p_gameState.getD_map().getCountry(l_randomOwnCountry.getD_neighborCountryIDs().get(l_random.nextInt(l_randomOwnCountry.getD_neighborCountryIDs().size())));
        Player l_randomPlayer = getRandomPlayer(p_player, p_gameState);

        if (l_randomOwnCountry.getD_numberOfArmies()>1) {
            l_armiesToBeSent = l_random.nextInt(l_randomOwnCountry.getD_numberOfArmies() - 1) + 1;
        } else {
            l_armiesToBeSent = 1;
        }
        switch(p_cardName){
            case "bomb":
                return "bomb "+ l_randomNeighbour.getD_countryName();
            case "blockade":
                return "blockade "+ l_randomOwnCountry.getD_countryName();
            case "airlift":
                return "airlift "+ l_randomOwnCountry.getD_countryName()+" "+getRandomCountry(p_player.getD_ownedCountries()).getD_countryName()+" "+l_armiesToBeSent;
            case "negotiate":
                return "negotiate"+" "+l_randomPlayer.getD_playerName();
        }
        return null;
    }

    /**
     * Method to return behavior of the player
     * @return Random as player behavior
     */
    @Override
    public String getPlayerBehavior() {
        return "Random";
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

    /**
     *
     * Method returning a random country owned by player.
     *
     * @param p_countriesList countries owned by player
     * @return a random country the given list
     */
    private Country getRandomCountry(List<Country> p_countriesList){
        Random l_randomNum = new Random();
        return p_countriesList.get(l_randomNum.nextInt(p_countriesList.size()));
    }

    /**
     * Method to select a random player for negotiation
     *
     * @param p_player player object
     * @param p_gameState current game state.
     * @return  A player object
     */
    private Player getRandomPlayer(Player p_player, GameState p_gameState){
        ArrayList<Player> l_listOfPlayers = new ArrayList<Player>();
        Random l_randomNum = new Random();

        for(Player l_player : p_gameState.getD_playerList()){
            if(!l_player.equals(p_player))
                l_listOfPlayers.add(p_player);
        }
        return l_listOfPlayers.get(l_randomNum.nextInt(l_listOfPlayers.size()));
    }
}
