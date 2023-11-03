package Views;

import Constants.GameConstants;
import Exceptions.InvalidMap;

import java.util.List;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Player;
import Models.Map;
import Utils.CommonUtil;
import org.davidmoten.text.utils.WordWrap;

/**
 * This is MapView class for displaying and accessing maps.
 *
 * @author Yatish Chutani
 * @version 1.0.0
 */
public class MapView {

    /**
     * List of players
     */
    List<Player> d_players;

    /**
     * GameState object
     */
    GameState d_gameState;

    /**
     * Map object
     */
    Map d_map;

    /**
     * List of countries
     */
    List<Country> d_countries;

    /**
     * List of the continents
     */
    List<Continent> d_continents;

    /**
     * This resets the Color ANSI Code.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * A parameterized constructor to initialize the MapView.
     *
     * @param p_gameState the current GameState.
     */
    public MapView(GameState p_gameState){
        d_gameState = p_gameState;
        d_map = p_gameState.getD_map();
        d_countries = d_map.getD_countries();
        d_continents = d_map.getD_continents();
    }

    /**
     * a parameterized constructor to initialize the MapView with Players.
     *
     * @param p_gameState the current GameState
     * @param p_players the list of Player Objects
     */
    public MapView(GameState p_gameState, List<Player> p_players){
        d_gameState = p_gameState;
        d_players = p_players;
        d_map = p_gameState.getD_map();
        d_countries = d_map.getD_countries();
        d_continents = d_map.getD_continents();
    }

    /**
     * It retrieves the Colored String.
     *
     * @param p_color the color to be altered to.
     * @param p_givenString String whose color is to be altered.
     * @return colored string.
     */
    private String getColorfulString(String p_color, String p_givenString){
        if(p_color == null) return p_givenString;

        return p_color + p_givenString + ANSI_RESET;
    }

    /**
     * It retrieves the Center String for Heading.
     *
     * @param p_width it is the defined width while formatting.
     * @param p_givenString the string that is to be retrieved.
     */
    private void getCenterString(int p_width, String p_givenString){
        String l_centerString =String.format("%-" + p_width + "s", String.format("%-" + (p_givenString.length() + (p_width - p_givenString.length())/2) +"s", p_givenString));

        System.out.format(l_centerString + "\n");
    }

    /**
     * It retrieves the Separator for heading.
     *
     */
    private void retrieveSeparator(){
        StringBuilder l_separator = new StringBuilder();

        for(int i = 0; i < GameConstants.CONSOLE_WIDTH - 2; i++){
            l_separator.append("-");
        }
        System.out.format("+%s+%n", l_separator.toString());
    }

    /**
     * It retrieves the continent Name with formatted centered string and separator.
     *
     * @param p_nameContinent Continent Name to be rendered.
     */
    private void retrieveNameContinent(String p_nameContinent){
        String l_nameContinent =p_nameContinent + " ( " + GameConstants.CONTROL_VALUE + " : " + d_gameState.getD_map().getContinent(p_nameContinent).getD_continentValue() + " ) ";

        retrieveSeparator();
        if(d_players == null){
            l_nameContinent = getColorfulString(retrieveColorContinent(p_nameContinent), l_nameContinent);
        }
        getCenterString(GameConstants.CONSOLE_WIDTH, l_nameContinent);
        retrieveSeparator();
    }

    /**
     * It retrieves the Country Name as Formatted.
     *
     * @param p_index the index value of Countries.
     * @param p_nameCountry the name of the country to be retrieved.
     * @return Returns the Formatted String
     */
    private String retrieveFormattedNameOfCountry(int p_index, String p_nameCountry){
        String l_indexString = String.format("%02d. %s", p_index, p_nameCountry);

        if(d_players != null){
            String l_armies = " ( " + GameConstants.ARMIES + " : " + retrieveArmiesOfCountries(p_nameCountry) + " ) ";
            l_indexString = String.format("%02d. %s", p_index, p_nameCountry, l_armies);
        }
        return getColorfulString(retrieveColorCountry(p_nameCountry), String.format("%-30s", l_indexString));
    }

    /**
     * It retrieves the Neighbor Countries in Formatted Settings.
     *
     * @param p_nameCountry the name of the country to be retrieved.
     * @param p_nghCountries a list that retrieves the neighbor countries.
     */
    private void retrieveFormattedNeighborNameOfCountry(String p_nameCountry, List<Country> p_nghCountries){
        StringBuilder l_commaDistinguishedCountries = new StringBuilder();

        for(int i = 0; i < p_nghCountries.size(); i++){
            l_commaDistinguishedCountries.append(p_nghCountries.get(i).getD_countryName());
            if(i < p_nghCountries.size() - 1){
                l_commaDistinguishedCountries.append(" , ");
            }
        }
        String l_neighborCountry = GameConstants.CONNECTIVITY + " : " + WordWrap.from(l_commaDistinguishedCountries.toString()).maxWidth(GameConstants.CONSOLE_WIDTH).wrap();
        System.out.println(getColorfulString(retrieveColorCountry(p_nameCountry), l_neighborCountry));
        System.out.println();
    }

    /**
     * It retrieves the color of Country according to the Player.
     *
     * @param p_nameCountry the name of the country to be retrieved.
     * @return the color of the country.
     */
    private String retrieveColorCountry(String p_nameCountry){
        if(retrieveCountryHolder(p_nameCountry) != null){
            return retrieveCountryHolder(p_nameCountry).getD_color();
        }
        else{
            return null;
        }
    }

    /**
     * It retrieves the color of continent according to the Player.
     *
     * @param p_nameContinent the name of the continent to be retrieved.
     * @return the color of the continent.
     */
    private String retrieveColorContinent(String p_nameContinent){
        if(retrieveContinentHolder(p_nameContinent) != null){
            return retrieveContinentHolder(p_nameContinent).getD_color();
        }
        else{
            return null;
        }
    }

    /**
     * It retrieves the holder of a country.
     *
     * @param p_nameCountry the name of the country to be retrieved.
     * @return the player object.
     */
    private Player retrieveCountryHolder(String p_nameCountry){
        if(d_players != null){
            for(Player p : d_players){
                if(p.getNamesOfCountries().contains(p_nameCountry)){
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * It retrieved the holder of the continent.
     *
     * @param p_nameContinent the name of the continent to be retrieved.
     * @return the player object.
     */
    private Player retrieveContinentHolder(String p_nameContinent){
        if(d_players !=null){
            for(Player p : d_players){
                if(!CommonUtil.isNull(p.getNamesOfContinents()) && p.getNamesOfContinents().contains(p_nameContinent)){
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * It retrieves the Player Information in formatted settings.
     *
     * @param p_index the index value of the Player.
     * @param p_player the player object.
     */
    private void retrieveInfoOfPlayer(Integer p_index, Player p_player){
        String l_infoOfPlayer = String.format("%02d. %-8s %s", p_index, p_player.getD_playerName(), " -> " + getColorfulString(p_player.getD_color(), "COLOR"));
        System.out.println(l_infoOfPlayer);
    }

    /**
     * It retrieves the players in Formatted Settings.
     *
     */
    private void retrievePlayers(){
        int l_counter = 0;

        retrieveSeparator();
        getCenterString(GameConstants.CONSOLE_WIDTH, "GAME PLAYERS");
        retrieveSeparator();

        for(Player p : d_players){
            l_counter++;
            retrieveInfoOfPlayer(l_counter, p);
            renderCardsOwnedByPlayers(p);
        }
    }

    /**
     * It retrieves the number of armies assigned for a country.
     *
     * @param p_nameCountry the name of the country to be retrieved.
     * @return the number of armies.
     */
    private Integer retrieveArmiesOfCountries(String p_nameCountry){
        Integer l_armies = d_gameState.getD_map().getCountryByName(p_nameCountry).getD_numberOfArmies();

        if(l_armies == null)
            return 0;
        return l_armies;
    }

    /**
     * This method displays the list of continents and countries present in the .map files along with current state of the game.
     */
    public void showMap(){
        if(d_players != null){
            retrievePlayers();
        }

        if(!CommonUtil.isNull(d_continents)){
            d_continents.forEach(l_continent ->{
                retrieveNameContinent(l_continent.getD_continentName());

                List<Country> l_countriesOfContinents = l_continent.getD_countries();
                final int[] l_indexCountry ={1};

                if(!CommonUtil.isEmptyCollection(l_countriesOfContinents)){
                    l_countriesOfContinents.forEach(l_country -> {
                        String l_formattedNameOfCountry = retrieveFormattedNameOfCountry(l_indexCountry[0]++, l_country.getD_countryName());
                        System.out.println(l_formattedNameOfCountry);
                        try{
                            List<Country> l_nghCountries = d_map.getNeighborCountry(l_country);
                            retrieveFormattedNeighborNameOfCountry(l_country.getD_countryName(), l_nghCountries);
                        }catch(InvalidMap l_invalidMap){
                            System.out.println(l_invalidMap.getMessage());
                        }
                    });
                }
                else{
                    System.out.println("There are no countries available in the continent!!");
                }
            });
        }
        else{
            System.out.println("There are no continents to show!!");
        }
    }

    /**
     * Method to render the number of cards owned by the player.
     *
     * @param p_player Player Instance
     */
    private void renderCardsOwnedByPlayers(Player p_player){
        StringBuilder l_cards = new StringBuilder();

        for(int i=0; i<p_player.getD_cardsOwned().size(); i++) {
            l_cards.append(p_player.getD_cardsOwned().get(i));
            if(i<p_player.getD_cardsOwned().size()-1)
                l_cards.append(", ");
        }

        String l_cardsOwnedByPlayer = "Cards Owned : "+ WordWrap.from(l_cards.toString()).maxWidth(GameConstants.CONSOLE_WIDTH).wrap();
        System.out.println(getColorfulString(p_player.getD_color(),l_cardsOwnedByPlayer));
        System.out.println();
    }

}
