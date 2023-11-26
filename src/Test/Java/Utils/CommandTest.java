package Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * This class is used to validate functionality of Command class methods.
 *
 * @version 3.0.0
 * @author Nihal Galani
 */

public class CommandTest{
    /**
     * Testing if the command entered is correct or not.
     */
    @Test
    public void testValidCommandGetRootCommand(){
        Command l_commandTest = new Command("editcontinent -add continentID continentvalue");
        String l_rootCommandAnswer = l_commandTest.getBaseCommand();

        assertEquals("editcontinent",l_rootCommandAnswer);
    }

    /**
     * Testing if the command entered is correct or not.
     */
    @Test
    public void testInValidCommandGetRootCommand(){
        Command l_commandTest = new Command("");
        String l_rootCommandAnswer = l_commandTest.getBaseCommand();

        assertEquals("", l_rootCommandAnswer);
    }

    /**
     * Testing the single word commands.
     */
    @Test
    public void testSingleWordGetRootCommand(){
        Command l_commandTest = new Command("validatemap");
        String l_rootCommandAnswer = l_commandTest.getBaseCommand();

        assertEquals("validatemap", l_rootCommandAnswer);
    }

    /**
     * testing the no flag commands.
     */
    @Test
    public void testNoFlagCommandGgetRootCommand(){
        Command l_commandTest = new Command("loadmap abc.txt");
        String l_rootCommandAnswer = l_commandTest.getBaseCommand();

        assertEquals("loadmap", l_rootCommandAnswer);
    }


    /**
     * testing if more than one space between the command and its parameter is accepted or not.
     */
    @Test
    public void testSingleCommandWithExtraSpacesGetOperationsAndArguments(){
        Command l_commandTest = new Command("editcontinent      -remove continentID");
        List<Map<String , String>> l_actualValues = l_commandTest.getParametersAndOperations();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommand2 = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedValues.add(l_expectedCommand2);

        assertEquals(l_expectedValues, l_actualValues);
    }

    /**
     * testing the single operation commands
     */
    @Test
    public void testSingleCommandGetOperationsAndArguments(){
        Command l_commandTest = new Command("editcontinent -remove continentID");
        List<Map<String , String>> l_actualValues = l_commandTest.getParametersAndOperations();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommand2 = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedValues.add(l_expectedCommand2);

        assertEquals(l_expectedValues, l_actualValues);
    }

    /**
     * testing the multiple commands in a single line
     */
    @Test
    public void testMultiCommandGetOperationsAndArguments(){
        Command l_commandTest = new Command("editcontinent -add continentID continentValue  -remove continentID");
        List<Map<String , String>> l_actualValues = l_commandTest.getParametersAndOperations();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommand1 = new HashMap<String, String>() {{
            put("arguments", "continentID continentValue");
            put("operation", "add");
        }};
        Map<String, String> l_expectedCommand2 = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedValues.add(l_expectedCommand1);
        l_expectedValues.add(l_expectedCommand2);

        assertEquals(l_expectedValues, l_actualValues);
    }

    /**
     * testing if more than one spaces in between the command and its parameters is acceptable or not
     */
    @Test
    public void testNoFlagCommandWithExtraSpacesGetOperationsAndArguments(){
        Command l_commandTest = new Command("loadmap         abc.txt");
        List<Map<String , String>> l_actualValues = l_commandTest.getParametersAndOperations();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommand1 = new HashMap<String, String>() {{
            put("arguments", "abc.txt");
            put("operation", "filename");
        }};
        l_expectedValues.add(l_expectedCommand1);

        assertEquals(l_expectedValues, l_actualValues);
    }

    /**
     * testing the command.
     */
    @Test
    public void testNoFlagCommandGetOperationsAndArguments(){
        Command l_commandTest = new Command("loadmap abc.txt");
        List<Map<String , String>> l_actualValues = l_commandTest.getParametersAndOperations();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommand1 = new HashMap<String, String>() {{
            put("arguments", "abc.txt");
            put("operation", "filename");
        }};
        l_expectedValues.add(l_expectedCommand1);

        assertEquals(l_expectedValues, l_actualValues);
    }
}