package TestSuites;

import Models.MapTest;
import Services.MapServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suit for running command such as load map, edit,map validations
 * country, continents and save map
 * @version 2.0.0
 * @author Nihal Galani
 */
@RunWith(Suite.class)
@SuiteClasses({ MapTest.class, MapServiceTest.class })
public class MapTestSuite {
}