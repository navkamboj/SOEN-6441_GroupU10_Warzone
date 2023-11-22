package Constants;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to initialise all the constants that are going to be used throughout the code base.
 *
 * @version 1.0.0
 */
public class GameConstants {
    public static final String SRC_MAIN_RESOURCES = "src/main/resources";
    public static final String INVALID_COMMAND_DEPLOY_ORDER = "Invalid command. Please submit your command in the format of : deploy countryID <CountryName> <num> (until every army has been deployed)";
    public static final String INVALID_COMMAND_EDITCOUNTRY = "Invalid command. Please submit your command in Appropriate Format like : editcountry -add countrytID continentID -remove countryID";
    public static final String INVALID_COMMAND_EDITNEIGHBOUR = "Invalid command. Please submit your command in Appropriate Format like : editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID";
    public static final String INVALID_COMMAND_EDITMAP = "Invalid command. Please submit your command in the Format of : editmap filename";
    public static final String INVALID_COMMAND_EDITCONTINENT = "Invalid command. Please submit your command in the Format of : editcontinent -add continentID continentvalue -remove continentID";
    public static final String INVALID_COMMAND_SAVEMAP = "Invalid command. Please submit your command in the Format of : savemap filename";
    public static final String INVALID_COMMAND_LOADMAP = "Invalid command. Please submit your command in the Format of : loadmap filename";
    public static final String INVALID_COMMAND_VALIDATEMAP = "Invalid command!Please load a valid map for verification.";
    public static final String INVALID_COMMAND_GAMEPLAYER = "Invalid command. Please submit your command in the Format of : gameplayer -add playername -remove playername";
    public static final String INVALID_COMMAND_ASSIGNCOUNTRIES = "Invalid command! Please provide command in Format of : assigncountries";
    public static final String INVALID_MAP_EMPTY = "A valid map is not present. Please load a valid map to perform a check.";
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
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final List<String> CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");
    public static final int SIZE = CARDS.size();
    public static final List<String> COLORS_LIST = Arrays.asList(RED_BOLD, GREEN_BOLD, YELLOW_BOLD, BLUE_BOLD, PURPLE_BOLD, CYAN_BOLD);
    public static final String CONQUEST_TERRITORIES = "[Territories]";
    public static final String CONQUEST_CONTINENTS = "[Continents]";
}
