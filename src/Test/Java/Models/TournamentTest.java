package Models;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import Controller.GameEngine;
import Exceptions.InvalidMap;
import Utils.Command;
import Exceptions.InvalidCommand;

import static org.junit.Assert.*;

/**
 * This class is used to test the Tournament game play.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class TournamentTest {
    /**
     * First Player.
     */
    Player d_playerName1;

    /**
     * Second Player.
     */
    Player d_playerName2;

    /**
     * current Game State.
     */
    GameState d_gameState;

    /**
     * Setup For testing Tournament mode.
     *
     * @throws InvalidMap Invalid Map
     */
    @Before
    public void setup() throws InvalidMap {
        d_gameState = new GameState();
        d_playerName1 = new Player("Nihal");
        d_playerName1.setStrategy(new RandomPlayer());
        d_playerName2 = new Player("Pranjlesh");
        d_playerName2.setStrategy(new RandomPlayer());

        d_gameState.setD_playerList(Arrays.asList(d_playerName1, d_playerName2));
    }

    /**
     * Validate tournament command in case of invalid map arguments passed.
     *
     * @throws InvalidCommand invalid command passed
     * @throws InvalidMap     invalid map name passed
     */
    @Test
    public void testMapArgumentsInvalidity() throws InvalidMap, InvalidCommand {
        Tournament l_tournamentMode = new Tournament();
        assertTrue(l_tournamentMode.parseTournamentCommand(d_gameState, "M",
                "test.map canada.map", new GameEngine()));
    }

    /**
     * Validate tournament command in case of invalid player arguments passed.
     *
     * @throws InvalidCommand invalid command passed
     * @throws InvalidMap     invalid map name passed
     */
    @Test
    public void testPlayerArgumentInvalidity() throws InvalidMap, InvalidCommand {
        Tournament l_tournamentMode = new Tournament();
        assertFalse(l_tournamentMode.parseTournamentCommand(d_gameState, "P",
                "Aggressive Nihal", new GameEngine()));
    }

    /**
     * Validate tournament command in case of invalid Game arguments passed.
     *
     * @throws InvalidCommand invalid command passed
     * @throws InvalidMap     invalid map name passed
     */
    @Test
    public void testGameArgumentInvalidity() throws InvalidMap, InvalidCommand {
        Tournament l_tournamentMode = new Tournament();
        assertFalse(l_tournamentMode.parseTournamentCommand(d_gameState, "G",
                "8", new GameEngine()));
    }

    /**
     * Validate tournament command in case of invalid turns arguments passed.
     *
     * @throws InvalidCommand invalid command passed
     * @throws InvalidMap     invalid map name passed
     */
    @Test
    public void testTurnsArgumentInvalidity() throws InvalidMap, InvalidCommand {
        Tournament l_tournamentMode = new Tournament();
        assertFalse(l_tournamentMode.parseTournamentCommand(d_gameState, "D",
                "80", new GameEngine()));
    }

    /**
     * Validate if valid tournament command is passed then is it working properly or not.
     *
     * @throws InvalidCommand invalid command passed
     * @throws InvalidMap     invalid map name passed
     */
    @Test
    public void testTournamentValidity() throws InvalidMap, InvalidCommand {
        StartUpPhase l_newStartUpPhase = new StartUpPhase(new GameEngine(), d_gameState);
        Tournament l_tournamentMode = new Tournament();
        GameEngine l_gameEngine = new GameEngine();
        l_tournamentMode.parseTournamentCommand(d_gameState, "M", "test.map canada.map", l_gameEngine);
        l_tournamentMode.parseTournamentCommand(d_gameState, "P", "Benevolent Random", l_gameEngine);
        l_tournamentMode.parseTournamentCommand(d_gameState, "G", "3", l_gameEngine);
        l_tournamentMode.parseTournamentCommand(d_gameState, "D", "15", l_gameEngine);

        assertEquals(l_tournamentMode.getD_gameStates().size(), 6);
        assertEquals(l_tournamentMode.getD_gameStates().get(0).getD_map().getD_mapFile(), "test.map");
        assertEquals(l_tournamentMode.getD_gameStates().get(1).getD_map().getD_mapFile(), "canada.map");

        assertEquals(l_tournamentMode.getD_gameStates().get(0).getD_playerList().size(), 2);
        assertEquals(l_tournamentMode.getD_gameStates().get(0).getD_maxCountOfTurns(), 15);

    }
}