package Views;

import Constants.GameConstants;
import Models.Tournament;
import Models.GameState;
import org.davidmoten.text.utils.WordWrap;

import java.util.List;

/**
 * This class represent tournament object view.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class TournamentView {
    /**
     * Tournament object
     */
    Tournament d_tournament;

    /**
     * GameState Objects List from tournament.
     */
    List<GameState> d_gameStateList;

    /**
     * ANSI Color code reset.
     */
    public static final String ANSI_RESETCOLOR = "\u001B[0m";

    /**
     * Constructor for tournament object.
     *
     * @param p_tournament tournament object
     */
    public TournamentView(Tournament p_tournament){
        d_tournament = p_tournament;
        d_gameStateList = d_tournament.getD_gameStates();
    }

    /**
     * Returns the Colored String.
     *
     * @param p_color color changed to.
     * @param p_string string whose color will change.
     * @return colored string.
     */
    private String getNewColoredString(String p_color, String p_string) {
        if(p_color == null)
            return p_string;

        return p_color + p_string + ANSI_RESETCOLOR;
    }

    /**
     * Provide the Center String for Heading.
     *
     * @param p_width   width formatting.
     * @param p_string  String to be provided.
     */
    private void provideCenteredString (int p_width, String p_string) {
        String l_centerPortionOfString = String.format("%-" + p_width  + "s", String.format("%" + (p_string.length() + (p_width - p_string.length()) / 2) + "s", p_string));

        System.out.format(l_centerPortionOfString+"\n");
    }

    /**
     * provide the Separator for heading.
     *
     */
    private void provideSeparator(){
        StringBuilder l_separatorString = new StringBuilder();

        for (int i = 0; i< GameConstants.CONSOLE_WIDTH -2; i++){
            l_separatorString.append("-");
        }
        System.out.format("+%s+%n", l_separatorString.toString());
    }

    /**
     * provide the name of Game number and Map file.
     *
     * @param p_indexOfGame game Index
     * @param p_nameOfTheMap map name
     */
    private void renderMapName(Integer p_indexOfGame, String p_nameOfTheMap){
        String l_stringFormatted = String.format("%s %s %d %s", p_nameOfTheMap, " (Game Number: ",p_indexOfGame, " )" );
        provideSeparator();
        provideCenteredString(GameConstants.CONSOLE_WIDTH, l_stringFormatted);
        provideSeparator();
    }

    /**
     * provide info of each game.
     *
     * @param p_gameState current game state object.
     */
    private void provideGameList(GameState p_gameState){
        String l_winnerName;
        String l_conclusion;
        if(p_gameState.getD_winningPlayer()==null){
            l_winnerName = " ";
            l_conclusion = "Draw!";
        } else{
            System.out.println("Entered Here");
            l_winnerName = getNewColoredString(p_gameState.getD_winningPlayer().getD_color(), p_gameState.getD_winningPlayer().getD_playerName());
            l_conclusion = "Winning Player Strategy: "+ p_gameState.getD_winningPlayer().getD_playerBehaviorStrategy().getPlayerBehavior();
        }
        String l_winnerString = String.format("%s %s", "Winner -> ", l_winnerName);
        StringBuilder l_commaSeparatedPlayers = new StringBuilder();

        for(int i=0; i<p_gameState.getD_listOfPlayersFailed().size(); i++) {
            l_commaSeparatedPlayers.append(getNewColoredString(p_gameState.getD_listOfPlayersFailed().get(i).getD_color(), p_gameState.getD_listOfPlayersFailed().get(i).getD_playerName()));
            if(i<p_gameState.getD_listOfPlayersFailed().size()-1)
                l_commaSeparatedPlayers.append(", ");
        }
        String l_losingPlayers = "Losing Players -> "+ WordWrap.from(l_commaSeparatedPlayers.toString()).maxWidth(GameConstants.CONSOLE_WIDTH).wrap();
        String l_conclusionString = String.format("%s %s", "Game's conclusion-> ", l_conclusion);
        System.out.println(l_winnerString);
        System.out.println(l_losingPlayers);
        System.out.println(l_conclusionString);
    }

    /**
     * provide the View of tournament results.
     */
    public void tournamentView(){
        int l_counter = 0;
        System.out.println();
        if(d_tournament!=null && d_gameStateList!=null){
            for(GameState l_gameState: d_tournament.getD_gameStates()){
                l_counter++;
                renderMapName(l_counter, l_gameState.getD_map().getD_mapFile());
                provideGameList(l_gameState);
            }
        }
    }
}
