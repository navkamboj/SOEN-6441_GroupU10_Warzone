package Views;

import java.util.*;
import Constants.ApplicationConstants;
import Exceptions.InvalidMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Player;
import Models.Map;
import Utils.CommonUtil;
import org.davidmoten.text.utils.WordWrap;

public class MapView {
    List<Player> d_players;

    GameState d_gameState;

    Map d_map;

    List<Country> d_countries;

    List<Continent> d_continents;

    public static final String ANSI_RESET = "\u001B[0m";

    public MapView(GameState p_gameState){
        d_gameState = p_gameState;
        d_map = p_gameState.getD_map();
        d_countries = d_map.getD_countries();
        d_continents = d_map.getD_continents();
    }

     public MapView(GameState p_gameState, List<Player> p_players){
         d_gameState = p_gameState;
         d_players = p_players;
         d_map = p_gameState.getD_map();
         d_countries = d_map.getD_countries();
         d_continents = d_map.getD_continents();
     }

     private String getColorfulString(String p_color, String p_givenString){
        if(p_color == null) return p_givenString;

        return p_color + p_givenString + ANSI_RESET;
     }

     private void getCenterString(int p_width, String p_givenString){
        String l_centerString =String.format("%-" + p_width + "s", String.format("%-" + (p_givenString.length() + (p_width - p_givenString.length())/2) +"s", p_givenString));

        System.out.format(l_centerString + "\n");
     }

     private void getSeparator(){
        StringBuilder l_separator = new StringBuilder();

        for(int i = 0; i < ApplicationConstants.CONSOLE_WIDTH - 2; i++){
            l_separator.append("-");
        }
        System.out.format("+%s+%n", l_separator.toString());
     }

     private void getNameContinent(String p_nameContinent){
        String l_nameContinent =p_nameContinent + " ( " + ApplicationConstants.CONTROL_VALUE + " : " + d_gameState.getD_map().getContinent(p_nameContinent).getD_continentValue() + " ) ";

        getSeparator();
        if(d_players == null){
            l_nameContinent = getColorfulString(getColorContinent(p_nameContinent), l_nameContinent);
        }
        getCenterString(ApplicationConstants.CONSOLE_WIDTH, l_nameContinent);
        getSeparator();
     }

     private String getFormattedNameOfCountry(int p_index, String p_nameCountry){
        String l_indexString = String.format("%02d. %s", p_index, p_nameCountry);

        if(d_players != null){
            String l_armies = " ( " + ApplicationConstants.ARMIES + " : " + getArmiesOfCountries(p_nameCountry) + " ) ";
            l_indexString = String.format("%02d. %s", p_index, p_nameCountry, l_armies);
        }
        return getColorfulString(getColorCountry(p_nameCountry), String.format("%-30s", l_indexString));
     }

     private void getFormattedNeighborNameOfCountry(String p_nameCountry, List<Country> p_nghCountries){
        StringBuilder l_commaDistinguishedCountries = new StringBuilder();

        for(int i = 0; i < p_nghCountries.size(); i++){
            l_commaDistinguishedCountries.append(p_nghCountries.get(i).getD_countryName());
            if(i < p_nghCountries.size() - 1){
                l_commaDistinguishedCountries.append(" , ");
            }
        }
        String l_neighborCountry = ApplicationConstants.CONNECTIVITY + " : " + WordWrap.from(l_commaDistinguishedCountries.toString()).maxWidth(ApplicationConstants.CONSOLE_WIDTH).wrap();
        System.out.println(getColorfulString(getColorCountry(p_nameCountry), l_neighborCountry));
        System.out.println();
     }

     private String getColorCountry(String p_nameCountry){
        if(getCountryHolder(p_nameCountry) != null){
            return getCountryHolder(p_nameCountry).getD_color();
        }
        else{
            return null;
        }
     }

     private String getColorContinent(String p_nameContinent){
        if(getContinentHolder(p_nameContinent) != null){
            return getContinentHolder(p_nameContinent).getD_color();
        }
        else{
            return null;
        }
     }

     private Player getCountryHolder(String p_nameCountry){
        if(d_players != null){
            for(Player p : d_players){
                if(p.getNamesOfCountries().contains(p_nameCountry)){
                    return p;
                }
            }
        }
        return null;
     }

     private void getInfoOfPlayer(Integer p_index, Player p_player){
        String l_infoOfPlayer = String.format("%02d. %-8s %s", p_index, p_player.getPlayerName(), " -> " + getColorfulString(p_player.getD_color(), "COLOR"));
        System.out.println(l_infoOfPlayer);
     }

     private void getPlayers(){
        int l_counter = 0;

        getSeparator();
        getCenterString(ApplicationConstants.CONSOLE_WIDTH, "GAME PLAYERS");
        getSeparator();

        for(Player p : d_players){
            l_counter++;
            getInfoOfPlayer(l_counter, p);
        }
     }

     private Player getContinentHolder(String p_nameContinent){
        if(d_players !=null){
            for(Player p : d_players){
                if(!CommonUtil.isNull(p.getNamesOfContinents()) && p.getNamesOfContinents().contains(p_nameContinent)){
                    return p;
                }
            }
        }
        return null;
     }

     private Integer getArmiesOfCountries(String p_nameCountry){
        Integer l_armies = d_gameState.getD_map().getCountryByName(p_nameCountry).getD_numberOfArmies();

        if(l_armies == null)
            return 0;
        return l_armies;
     }

     public void showMap(){
        if(d_players != null){
            getPlayers();
        }

        if(!CommonUtil.isNull(d_continents)){
            d_continents.forEach(l_continent ->{
                getNameContinent(l_continent.getD_continentName());

                List<Country> l_countriesOfContinents = l_continent.getD_countries();
                final int[] l_indexCountry ={1};

                if(!CommonUtil.isEmptyCollection(l_countriesOfContinents)){
                    l_countriesOfContinents.forEach(l_country -> {
                        String l_formattedNameOfCountry = getFormattedNameOfCountry(l_indexCountry[0]++, l_country.getD_countryName());
                        System.out.println(l_formattedNameOfCountry);
                        try{
                            List<Country> l_nghCountries = d_map.getNeighborCountry(l_country);
                            getFormattedNeighborNameOfCountry(l_country.getD_countryName(), l_nghCountries);
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
}