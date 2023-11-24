package Models;

import Services.PlayerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

/**
 * Class for Cheater player who cheats by making direct attack on its neighboring enemies during issue order phase
 * and doubles the armies on his countries
 *
 * @author Navjot Kamboj
 * @version 3.0.0
 */
public class CheaterPlayer extends PlayerBehaviorStrategy {

    /**
     *
     * Method to return a random country owned by player.
     *
     * @param p_countriesList list of countries owned by player
     * @return a random country from the given list
     */
    private Country getCountryRandomly(List<Country> p_countriesList){
        Random l_randomNumber = new Random();
        return p_countriesList.get(l_randomNumber.nextInt(p_countriesList.size()));
    }

    /**
     * Method to retrieve rivals of the player.
     *
     * @param p_player cheater player
     * @param p_country player country
     * @return list of rivals
     */
    private ArrayList<Integer> getRivals(Player p_player, Country p_country){
        ArrayList<Integer> l_rivalNeighbors = new ArrayList<Integer>();

        for(Integer l_countryID : p_country.getD_neighborCountryIDs()){
            if(!p_player.getCountryIDs().contains(l_countryID))
                l_rivalNeighbors.add(l_countryID);
        }
        return l_rivalNeighbors;
    }

    /**
     * Conquer all rivals that are neighbor to the country which is owned by player
     *
     * @param p_player Player class object
     * @param p_gameState GameState class object
     */
    private void conquerNeighboringRivals(Player p_player, GameState p_gameState){
        List<Country> l_countriesOwnedByPlayer = p_player.getD_ownedCountries();

        for(Country l_country : l_countriesOwnedByPlayer) {
            ArrayList<Integer> l_countryRivals = getRivals(p_player, l_country);

            for(Integer l_rivalID: l_countryRivals) {
                Map l_loadedMap =  p_gameState.getD_map();
                Player l_enemyCountryOwner = this.getOwnerOfCountry(p_gameState, l_rivalID);
                Country l_enemyCountry = l_loadedMap.getCountryByID(l_rivalID);
                this.conquerTargetCountry(p_gameState, l_enemyCountryOwner ,p_player, l_enemyCountry);

                String l_logMessage = "Cheater Player: " + p_player.getD_playerName() +
                        " Now owns " + l_enemyCountry.getD_countryName();

                p_gameState.logUpdate(l_logMessage, "effect");
            }

        }
    }

    /**
     * This method is used to create a new order.
     * @param p_player Player class object
     * @param p_gameState GameState class object
     *
     * @return Order object of order class
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) throws IOException {
        if(p_player.getD_noOfAllocatedArmies() != 0) {
            while(p_player.getD_noOfAllocatedArmies() > 0) {
                Random l_randomNumber = new Random();
                Country l_randomCountry = getCountryRandomly(p_player.getD_ownedCountries());
                int l_armiesToBeDeployed = l_randomNumber.nextInt(p_player.getD_noOfAllocatedArmies()) + 1;

                l_randomCountry.setD_numberOfArmies(l_armiesToBeDeployed);
                p_player.setD_noOfAllocatedArmies(p_player.getD_noOfAllocatedArmies() - l_armiesToBeDeployed);

                String l_logMessage = "Cheater Player: " + p_player.getD_playerName() +
                        " assigned " + l_armiesToBeDeployed +
                        " armies to  " + l_randomCountry.getD_countryName();

                p_gameState.logUpdate(l_logMessage, "effect");
            }
    }
        try {
            conquerNeighboringRivals(p_player, p_gameState);
        } catch (ConcurrentModificationException l_e){
        }

       // doubleArmyOnRivalsNeighboredCountries(p_player, p_gameState);
        // p_player.checkForMoreOrders(true);
        return null;
    }



    /**
     * @param p_gameState Current Game State
     * @param p_countryID ID of the country whose neighbor is to be found
     * @return Country's Owner
     */

    private Player getOwnerOfCountry(GameState p_gameState, Integer p_countryID){
        List<Player> l_playersList = p_gameState. getD_playerList();
        Player l_owner = null;

        for(Player l_player: l_playersList){
            List<Integer> l_ownedCountries = l_player.getCountryIDs();
            if(l_ownedCountries.contains(p_countryID)){
                l_owner = l_player;
                break;
            }
        }

        return l_owner;
    }

    /**
     * Conquers Target Country in case target country has no armies
     *
     * @param p_gameState             Current state of the game
     * @param p_cheater player owning source country
     * @param p_targetPlayer player owning the target country
     * @param p_targetCountry         target country of the battle
     */
    private void conquerTargetCountry(GameState p_gameState, Player p_targetPlayer, Player p_cheater, Country p_targetCountry) {
        p_targetPlayer.getD_ownedCountries().remove(p_targetCountry);
        p_cheater.getD_ownedCountries().add(p_targetCountry);

        //this.updateContinents(p_cheaterPlayer, p_targetPlayer, p_gameState);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderDeploy(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderAdvance(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrderCard(Player p_player, GameState p_gameState, String p_cardName) {
        return null;
    }

    /**
     * Method to return behavior of the player
     * @return Cheater as player behavior
     */
    @Override
    public String getPlayerBehavior() {
        return "Cheater";
    }
}
