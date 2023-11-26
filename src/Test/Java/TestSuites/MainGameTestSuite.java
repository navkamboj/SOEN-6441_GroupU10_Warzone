package TestSuites;

import Models.*;
import Services.PlayerServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for testing player services of adding players, assigning armies and countries
 * @version 3.0.0
 * @author Nihal Galani
 */
@RunWith(Suite.class)
@SuiteClasses({ PlayerTest.class, PlayerServiceTest.class, AdvanceTest.class, AirliftTest.class,
		BlockadeTest.class, BombTest.class,DeployTest.class, DiplomacyTest.class,RandomPlayerTest.class,AggressivePlayerTest.class,
		BenevolentPlayerTest.class,TournamentTest.class,CheaterPlayerTest.class})
public class MainGameTestSuite {
}