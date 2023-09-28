package Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This is the entry point for the Game and keeps track of the current Game State.
 */
public class GameEngine {

    /**
     * The main method responsible for accepting commands from users and redirecting
     * them to their respective logical flow.
     *
     * @param p_args the program doesn't use default command line arguments
     */
    public static void main(String[] p_args) {
        GameEngine l_game = new GameEngine();
        l_game.initGame();
    }

    /**
     * Handles the commands.
     *
     * @param p_enteredCommand
     * @throws IOException
     */
    public void handleCommand(String p_enteredCommand) throws IOException {

        String l_command = "", l_rootCommand = "";

        switch (l_rootCommand) {
            case "editmap" : {
                break;
            }
            case "editcontinent" : {
                //editcontinent functionality
            }
            case "savemap" : {
                //savemap functionality
            }
            case "loadmap" : {
                //loadmap functionality
            }
            case "validatemap" : {
                //validatemap func
            }
            case "editcountry" : {
                //editcountry func
            }
            case "editneighbor" : {
                //editneighbor func
            }
            case "gameplayer" : {
                //gameplayer func
            }
            case "assigncountries" : {
                //assigncountries func
            }
            case "showmap" : {
                //showmap func
            }
            case "exit" : {
                System.out.println("Exit Command Entered");
                System.exit(0);
                break;
            }
            default : {
                System.out.println("Invalid Command");
                break;
            }
        }
    }

    /**
     * This method initiates the CommandLineInterface to accept commands from the user and maps them to their respective
     * action handler.
     */
    public void initGame() {
        Scanner l_scannerObject = new Scanner(System.in);

        while(true) {
            try {
                System.out.println("Enter the game commands or enter 'exit' to quit the game\n");
                String l_enteredCommand = l_scannerObject.nextLine();

                handleCommand(l_enteredCommand);
            }
            catch (IOException l_ioException) {
                l_ioException.printStackTrace();
            }
        }
    }
}
