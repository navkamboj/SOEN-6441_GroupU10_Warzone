package Models;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Map;
import java.util.List;

import Constants.GameConstants;
import Controller.GameEngine;
import Services.MapService;
import Utils.Command;
import Exceptions.InvalidMap;
import Exceptions.InvalidCommand;

/**
 *This class handles Tournament Gameplay.
 * @version 3.0.0
 * @author Nihal Galani
 */
public class Tournament implements Serializable{

    /**
     * Object of Map service.
     */
    MapService d_mapService = new MapService();

    /**
     * Tournament's game stats.
     */
    List<GameState> d_gameStates = new ArrayList<GameState>();

    /**
     * This method will return list of game state.
     *
     * @return game state list
     */
    public List<GameState> getD_gameStates() {
        return d_gameStates;
    }

    /**
     * This method will set game states.
     *
     * @param d_gameStates game state list.
     */
    public void setD_gameStates(List<GameState> d_gameStates)
    {
        this.d_gameStates = d_gameStates;
    }

    /**
     * This method will parse tournament command into tournament object.
     *
     * @param p_gameState  current game state.
     * @param p_operationName  operation available in the command.
     * @param p_argumentType   argument available in the command
     * @param p_gameEngine game engine
     * @return boolean returns true if parsing is successful otherwise it will return false.
     * @throws InvalidMap    if map is invalid then returns true
     * @throws InvalidCommand if command is logically or syntactically invalid
     */
    public boolean parseTournamentCommand(GameState p_gameState, String p_operationName, String p_argumentType,
                                          GameEngine p_gameEngine) throws InvalidMap, InvalidCommand {

        if (p_operationName.equalsIgnoreCase("M")) {
            return mapArgumentParsing(p_argumentType, p_gameEngine);
        }
        if (p_operationName.equalsIgnoreCase("P")) {
            return strategyArgumentParsing(p_gameState, p_argumentType, p_gameEngine);
        }
        if (p_operationName.equalsIgnoreCase("G")) {
            return NoOfGameArgumentParsing(p_argumentType, p_gameEngine);
        }
        if (p_operationName.equalsIgnoreCase("D")) {
            return noOfTurnsArgumentParsing(p_argumentType, p_gameEngine);
        }

        throw new InvalidCommand(GameConstants.INVALID_COMMAND_TOURNAMENT_MODE);
    }

    /**
     * Number of turns in tournament command will be parses to an object
     *
     * @param p_argumentType  no of turns
     * @param p_gameEngine game engine
     * @return true if parsing is successful otherwise it will return false.
     */
    private boolean noOfTurnsArgumentParsing(String p_argumentType, GameEngine p_gameEngine) {
        int l_maximumNumberOfTurns = Integer.parseInt(p_argumentType.split(" ")[0]);
        if (l_maximumNumberOfTurns >= 10 && l_maximumNumberOfTurns <= 50) {
            for (GameState l_gameState : d_gameStates) {
                l_gameState.setD_maxCountOfTurns(l_maximumNumberOfTurns);
                l_gameState.setD_countOfRemainingTurns(l_maximumNumberOfTurns);
            }
            return true;
        } else {
            p_gameEngine.setD_logGameEngine(
                    "Invalid number of turn is entered by user in the command, Range of turns :- 10<=number of turns<=50",
                    "effect");
            return false;
        }
    }

    /**
     * Number of games in tournament command will be parsed to an object.
     *
     * @param p_argumentType  number of games
     * @param p_gameEngine game engine
     * @return true if parsing is successful otherwise it will return false.
     * @throws InvalidMap if map is invalid then returns true
     */
    private boolean NoOfGameArgumentParsing(String p_argumentType, GameEngine p_gameEngine) throws InvalidMap {
        int l_numberOfGames = Integer.parseInt(p_argumentType.split(" ")[0]);

        if (l_numberOfGames >= 1 && l_numberOfGames <= 5) {
            List<GameState> l_extraGameStates = new ArrayList<>();

            for (int l_gameNumber = 0; l_gameNumber < l_numberOfGames - 1; l_gameNumber++) {
                for (GameState l_gameState : d_gameStates) {
                    GameState l_additionOfGameState = new GameState();
                    Models.Map l_loadedMap = d_mapService.mapLoad(l_additionOfGameState,
                            l_gameState.getD_map().getD_mapFile());
                    l_loadedMap.setD_mapFile(l_gameState.getD_map().getD_mapFile());

                    List<Player> l_playersToCopy = getPlayerListForGameState(l_gameState.getD_playerList());
                    l_additionOfGameState.setD_playerList(l_playersToCopy);

                    l_additionOfGameState.setD_checkLoadCommand();
                    l_extraGameStates.add(l_additionOfGameState);
                }
            }
            d_gameStates.addAll(l_extraGameStates);
            return true;
        } else {
            p_gameEngine.setD_logGameEngine(
                    "Invalid number of games are entered by user in command, Range of games :- 1<=number of games<=5",
                    "effect");
            return false;
        }
    }

    /**
     * This method will return player list to add in each game state.
     *
     * @param p_playersList list of players to be looked from
     * @return PlayerList
     */
    private List<Player> getPlayerListForGameState(List<Player> p_playersList) {
        List<Player> p_playersToAdd = new ArrayList<>();
        for (Player l_player : p_playersList) {
            Player l_playerName = new Player(l_player.getD_playerName());

            if (l_player.getD_playerBehaviorStrategy() instanceof AggressivePlayer)
                l_playerName.setStrategy(new AggressivePlayer());
            else if (l_player.getD_playerBehaviorStrategy() instanceof RandomPlayer)
                l_playerName.setStrategy(new RandomPlayer());
            else if (l_player.getD_playerBehaviorStrategy() instanceof BenevolentPlayer)
                l_playerName.setStrategy(new BenevolentPlayer());
            else if (l_player.getD_playerBehaviorStrategy() instanceof CheaterPlayer)
                l_playerName.setStrategy(new CheaterPlayer());

            p_playersToAdd.add(l_player);
        }
        return p_playersToAdd;
    }

    /**
     * strategy argument is parsed into tournament object.
     *
     * @param p_gameState  current Game state
     * @param p_argumentType   strategy arguments available in game
     * @param p_gameEngine object of game engine class
     * @return true strategy information is successfully parsed otherwise it will return false.
     */
    private boolean strategyArgumentParsing(GameState p_gameState, String p_argumentType, GameEngine p_gameEngine) {
        String[] l_playerStrategyList = p_argumentType.split(" ");
        int l_playerStrategiesSize = l_playerStrategyList.length;
        List<Player> l_playerListInTheCurrentGame = new ArrayList<>();
        List<String> l_specialStrategies = new ArrayList<>();

        for (String l_strategy : l_playerStrategyList) {
            if(l_specialStrategies.contains(l_strategy)) {
                p_gameEngine.setD_logGameEngine(
                        "Repetitive strategy : " + l_strategy + " given. Kindly provide set of unique strategies.",
                        "effect");
                return false;
            }
            l_specialStrategies.add(l_strategy);
            if (!GameConstants.TOURNAMENT_PLAYER_BEHAVIORS.contains(l_strategy)) {
                p_gameEngine.setD_logGameEngine(
                        "Invalid Strategy.Only Aggressive, Benevolent, Random, Cheater strategies are allowed.",
                        "effect");
                return false;
            }
        }
        if (l_playerStrategiesSize >= 2 && l_playerStrategiesSize <= 4) {
            setTournamentPlayers(p_gameEngine, l_playerStrategyList, p_gameState.getD_playerList(), l_playerListInTheCurrentGame);
        } else {
            p_gameEngine.setD_logGameEngine(
                    "Invalid number of strategies are entered by user in the command, Range of strategies :- 2<=strategy<=4",
                    "effect");
            return false;
        }
        if (l_playerListInTheCurrentGame.size() < 2) {
            p_gameEngine.setD_logGameEngine(
                    "There has to be at least 2 or more non human players eligible to play the tournament.", "effect");
            return false;
        }
        for (GameState l_gameState : d_gameStates) {
            l_gameState.setD_playerList(getPlayerListForGameState(l_playerListInTheCurrentGame));
        }
        return true;
    }

    /**
     * Whoever will going to play the tournament that players information will be sets in this method.
     *
     * @param p_gameEngine             Object of game engine class
     * @param p_listOfPlayerStrategy PlayerStrategyList
     * @param p_players                playerList in current game state
     * @param p_playersInCurrentTheGame   playerList of the tournament
     */
    private void setTournamentPlayers(GameEngine p_gameEngine, String[] p_listOfPlayerStrategy,
                                      List<Player> p_players, List<Player> p_playersInCurrentTheGame) {
        for (String l_strategy : p_listOfPlayerStrategy) {
            for (Player l_player : p_players) {
                if (l_player.getD_playerBehaviorStrategy().getPlayerBehavior().equalsIgnoreCase(l_strategy)) {
                    p_playersInCurrentTheGame.add(l_player);
                    p_gameEngine.setD_logGameEngine("Player:  " + l_player.getD_playerName() + " with strategy: " + l_strategy
                            + " is added in the tournament.", "effect");
                }
            }
        }
    }

    /**
     * Map argument is parsed into tournament object.
     *
     * @param p_argumentType  list of maps information
     * @param p_gameEngine object of game engine class
     * @return boolean returns true if parsing is successful otherwise it will return false.
     * @throws InvalidMap returned if map given in command is invalid
     */
    private boolean mapArgumentParsing(String p_argumentType, GameEngine p_gameEngine) throws InvalidMap {
        String[] l_mapFileList = p_argumentType.split(" ");
        int l_sizeOfMapFile = l_mapFileList.length;

        if (l_sizeOfMapFile >= 1 & l_sizeOfMapFile <= 5) {
            for (String l_mapToLoad : l_mapFileList) {
                GameState l_gameState = new GameState();
                // if the map is valid then load the map otherwise reset the game state
                Models.Map l_loadedMap = d_mapService.mapLoad(l_gameState, l_mapToLoad);
                l_loadedMap.setD_mapFile(l_mapToLoad);
                if (l_loadedMap.Validate()) {
                    l_gameState.setD_checkLoadCommand();
                    p_gameEngine.setD_logGameEngine(l_mapToLoad + " has been loaded", "effect");
                    d_gameStates.add(l_gameState);
                } else {
                    d_mapService.mapReset(l_gameState, l_mapToLoad);
                    return false;
                }
            }
        } else {
            p_gameEngine.setD_logGameEngine("Number of map entered by user is invalid, Range of map :- 1<=map<=5",
                    "effect");
            return false;
        }
        return true;
    }

    /**
     * Validation of Tournament command.
     *
     * @param p_operations command's operation list
     * @param p_commandType        tournament command
     * @return true if command is valid or else false
     */
    public boolean validateTournamentArguments(List<Map<String, String>> p_operations, Command p_commandType) {
        String l_keyForArgument = "";
        if (p_operations.size() != 4)
            return false;

        for (Map<String, String> l_map : p_operations) {
            if (p_commandType.isKeywordAvailable(GameConstants.ARGUMENTS, l_map)
                    && p_commandType.isKeywordAvailable(GameConstants.OPERATION, l_map)) {
                l_keyForArgument.concat(l_map.get(GameConstants.OPERATION));
            }
        }
        if (!l_keyForArgument.equalsIgnoreCase("MPGD"))
            return false;
        return true;
    }
}