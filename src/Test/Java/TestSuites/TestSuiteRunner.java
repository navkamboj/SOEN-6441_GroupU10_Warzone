package TestSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suit for running all test suites involved in this game project
 * @version 2.0.0
 * @author Nihal Galani
 */
@RunWith(Suite.class)
@SuiteClasses({ CommandControllerTestSuite.class, MainGameTestSuite.class, MapTestSuite.class })
public class TestSuiteRunner {
}