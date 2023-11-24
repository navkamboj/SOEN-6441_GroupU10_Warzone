package Controller;

import Constants.GameConstants;
import Exceptions.InvalidMap;
import Exceptions.InvalidCommand;
import Models.*;
import Services.MapService;
import Services.PlayerService;
import Utils.Command;
import Utils.CommonUtil;
import Views.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This serves as the starting point for the game and manages the current state of the game.
 *
 * @author Harsh Tank, Pranjalesh Ghansiyal, Yatish Chutani, Navjot Kamboj
 * @version 3.0.0
 */
public class GameEngine {

    /**
     * d_gameState Records the data pertaining to the current game.
     */
    GameState d_gameState = new GameState();

    /**
     *	Current gameplay phase according to the state pattern.
     */
    Phase d_presentPhase = new StartUpPhase(this, d_gameState);

    /**
     * Tournament mode or single game mode.
     */
    static boolean d_isTournamentMode = false;

    /**
     * Sets tournament mode.
     *
     * @param p_isTournamentMode is set true if tournament is being played
     */
    public void setD_isTournamentMode(boolean p_isTournamentMode) {
        GameEngine.d_isTournamentMode = p_isTournamentMode;
    }
    /**
     * Handles load game feature by setting phase from Object stream.
     *
     * @param p_phase new Phase to set in Game context
     */
    public void loadPhase(Phase p_phase){
        d_presentPhase = p_phase;
        d_gameState = p_phase.getD_gameState();
        getD_PresentPhase().initPhase(d_isTournamentMode);
    }

    /**
     *  Method to update the current phase to StartUp Phase as per State Pattern.
     */
    public void setStartUpPhase(){
        this.setD_logGameEngine("Start Up Phase", "phase");
        setD_PresentPhase(new StartUpPhase(this, d_gameState));
        getD_PresentPhase().initPhase(d_isTournamentMode);
    }

    /**
     * Method to set the current phase
     *
     * @param p_phase new Phase in Game context
     */
    private void setD_PresentPhase(Phase p_phase){
        d_presentPhase = p_phase;
    }

    /**
     * Getter method to retrieve current phase
     *
     * @return current Phase of Game Context
     */
    public Phase getD_PresentPhase(){
        return d_presentPhase;
    }

    /**
     * The main function responsible for receiving user commands and directing them to their appropriate
     * logical pathways.
     *
     * @param p_args the program doesn't use default command line arguments
     */
    public static void main(String[] p_args) {
        GameEngine l_gameEngine = new GameEngine();

        l_gameEngine.getD_PresentPhase().getD_gameState().logUpdate("Loading the Game ......"+System.lineSeparator(), "start");
        l_gameEngine.setD_logGameEngine("Game Startup Phase", "phase");
        l_gameEngine.getD_PresentPhase().initPhase(d_isTournamentMode);
    }

    /**
     * This setter method displays and writes GameEngine Logs.
     *
     * @param p_logGameEngine The string of Log message.
     * @param p_logType The type of Log.
     */
    public void setD_logGameEngine(String p_logGameEngine, String p_logType){
        d_presentPhase.getD_gameState().logUpdate(p_logGameEngine, p_logType);
        String l_consoleLogging = p_logType.toLowerCase().equals("phase")
                ? "\n######### " + p_logGameEngine + " #########\n"
                : p_logGameEngine;
        System.out.println(l_consoleLogging);
    }

    /**
     * This method updates the present phase to Order Execution Phase as per the State Pattern.
     */
    public void setOrderExecutionPhase(){
        this.setD_logGameEngine("Order Execution Phase", "phase");
        setD_PresentPhase(new OrderExecutionPhase(this, d_gameState));
        getD_PresentPhase().initPhase(d_isTournamentMode);
    }

    /**
     * This method updates the present phase to Issue Order Phase as per the State Pattern.
     */
    public void setIssueOrderPhase(){
        this.setD_logGameEngine("Issue Order Phase", "phase");
        setD_PresentPhase(new IssueOrderPhase(this, d_gameState));
        getD_PresentPhase().initPhase(d_isTournamentMode);
    }
}
