package Constants;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to initialise all the constants that are going to be used throughout the code base.
 *
 * @version 1.0.0
 */
public class ApplicationConstants {
    public static final String SRC_MAIN_RESOURCES = "src/main/resources";
    public static final String INVALID_COMMAND_ERROR_DEPLOY_ORDER = "Invalid command. Please submit your command in the format of : deploy countryID <CountryName> <num> (until every augmentation has been deployed)";
    public static final String INVALID_COMMAND_ERROR_EDITMAP = "Invalid command. Please submit your command in the Format of : editmap filename";
    public static final String INVALID_CMD_ERR_FOR_EDITCONTINENT = "Invalid command. Please submit your command in the Format of : editcontinent -add continentID continentvalue -remove continentID";
    public static final String INVALID_COMMAND_ERROR_SAVEMAP = "Invalid command. Please submit your command in the Format of : savemap filename";
    public static final String INVALID_COMMAND_ERROR_LOADMAP = "Invalid command. Please submit your command in the Format of : loadmap filename";
    public static final String INVALID_COMMAND_ERROR_VALIDATEMAP = "Invalid command!Please load a valid map for verification.";
    public static final String INVALID_MAP_ERROR_EMPTY = "A valid map is not present. Please load a valid map to perform a check.";
    public static final String VALID_MAP = "The loaded map is valid!";
    public static final int CONSOLE_WIDTH = 80;
    public static final String ARGUMENTS = "arguments";
    public static final String OPERATION = "operation";
    public static final String CONTROL_VALUE = "Control Value";
    public static final String CONNECTIVITY = "Connections";
    public static final String ARMIES = "Armies";
    public static final String CONTINENTS = "[continents]";
    public static final String COUNTRIES = "[countries]";
    public static final String BORDERS = "[borders]";
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final List<String> COLORS_LIST = Arrays.asList(RED_BOLD, GREEN_BOLD, YELLOW_BOLD, BLUE_BOLD, PURPLE_BOLD, CYAN_BOLD);
}
