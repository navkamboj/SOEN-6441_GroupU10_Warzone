package TestSuites;

import Controllers.GameEngineTest;
import Models.OrderExecutionPhaseTest;
import Utils.CommandTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * Test suite for testing StartUp phase and OrderExecution Phase
 * @version 3.0.0
 * @author Nihal Galani
 *
 */
@RunWith(Suite.class)
@SuiteClasses({CommandTest.class,GameEngineTest.class, OrderExecutionPhaseTest.class })
public class CommandControllerTestSuite {   
}  