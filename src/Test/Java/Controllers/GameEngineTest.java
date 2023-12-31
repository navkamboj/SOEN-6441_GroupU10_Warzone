package Controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.GameState;
import Models.Map;
import Models.Phase;
import Models.StartUpPhase;
import Controller.GameEngine;

/**
 * This class is used to test functionality of GameEngineController class functions.
 * @version 2.0.0
 * @author Nihal Galani
 */
public class GameEngineTest {

    /**
     * Map class object
     */
    Map d_map;

    /**
     * GameState class object.
     */
    Phase d_gamestate;

    /**
     * object of GameEngineController class.
     */
    GameEngine d_gameEngine;

    /**
     *
     *  This method GameEngineSetup called every time before the execution of every test case that
     *  are available in this GameEngineTest class file
     *  @throws InvalidMap Invalid Map
     *
     */
    @Before
    public void setup() {
        d_map = new Map();
        d_gameEngine = new GameEngine();
        d_gamestate = d_gameEngine.getD_PresentPhase();
    }

    /**
     * Tests the {@link InvalidCommand } in editmap command.
     *
     * @throws IOException    Exception
     * @throws InvalidCommand Exception
     */
    @Test(expected = InvalidCommand.class)
    public void testInvalidCommandForEditMap() throws IOException, InvalidCommand, InvalidMap {
        d_gamestate.handleCommand("editmap");
    }

    /**
     * Tests the {@link InvalidCommand} in editcontinent command
     *
     * @throws IOException    Exception
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     */
    @Test
    public void testInvalidCommandForEditContinent() throws InvalidCommand, IOException, InvalidMap {
        d_gamestate.handleCommand("editcontinent");
        GameState l_currentState = d_gamestate.getD_gameState();

        assertEquals("Can not do edit continent at this stage, please perform `editmap` command first." + System.lineSeparator(),
                l_currentState.getLatestLog());
    }

    /**
     * Tests the valid editcontinent command
     *
     * @throws IOException    Exception
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     */
    @Test
    public void testValidCommandForEditContinent() throws IOException, InvalidCommand, InvalidMap {
        d_map.setD_mapFile("test.map");
        GameState l_currentState = d_gamestate.getD_gameState();

        l_currentState.setD_map(d_map);
        d_gamestate.setD_gameState(l_currentState);

        d_gamestate.handleCommand("editcontinent -add Asia 15 -add Australia 5");

        l_currentState = d_gamestate.getD_gameState();

        List<Continent> l_continentList = l_currentState.getD_map().getD_continents();
        assertEquals(2, l_continentList.size());
        assertEquals("Asia", l_continentList.get(0).getD_continentName());
        assertEquals("15", l_continentList.get(0).getD_continentValue().toString());
        assertEquals("Australia", l_continentList.get(1).getD_continentName());
        assertEquals("5", l_continentList.get(1).getD_continentValue().toString());

        d_gamestate.handleCommand("editcontinent -remove Australia");

        l_currentState = d_gamestate.getD_gameState();
        l_continentList = l_currentState.getD_map().getD_continents();
        assertEquals(1, l_continentList.size());
    }

    /**
     * Tests the {@link InvalidCommand } in savemap
     *
     * @throws InvalidCommand Exception
     * @throws InvalidMap     Exception
     */
    @Test
    public void testInvalidCommandForSaveMap() throws InvalidCommand, InvalidMap, IOException {
        d_gamestate.handleCommand("savemap");
        GameState l_currentState = d_gamestate.getD_gameState();

        assertEquals("There is no map to save yet !!, Run `editmap` command first" + System.lineSeparator(),
                l_currentState.getLatestLog());

    }

    /**
     * Testing invalid command for assigncountries
     *
     * @throws InvalidCommand Exception
     * @throws IOException    Exception
     */
    @Test(expected = InvalidCommand.class)
    public void testInvalidCommandForAssignCountries() throws IOException, InvalidMap, InvalidCommand {
        d_gamestate.getD_gameState().setD_checkLoadCommand();
        d_gamestate.handleCommand("assigncountries -add india");
    }

    /**
     * Validation of correct start up phase
     */
    @Test
    public void testCorrectStartupPhase() {
        assertTrue(d_gameEngine.getD_PresentPhase() instanceof StartUpPhase);
    }
}
