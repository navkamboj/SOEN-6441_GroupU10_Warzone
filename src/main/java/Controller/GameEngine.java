package Controller;

import java.io.IOException;
import java.util.Scanner;

public class GameEngine {

    public static void main(String[] args) {

        GameEngine l_game = new GameEngine();
        l_game.initGame();
    }

    public void handleCommand(String p_enteredCommand) throws IOException {

        String l_command = "", l_rootCommand = "";

        switch (l_rootCommand) {
            case "editmap" : {
                //editmap functionality
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
