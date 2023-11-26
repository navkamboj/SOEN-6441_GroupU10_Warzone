package Utils;

import Models.GameState;

import java.io.Serializable;

/**
 * A class for logging uncaught exceptions not handled by try/catch blocks
 *
 * @author Yatish Chutani
 * @version 2.0.0
 */
public class ExceptionLogHandler implements Thread.UncaughtExceptionHandler, Serializable {

    /**
     * Exception Log belongs to this GameState.
     */
    GameState d_gameState;

    /**
     * A Parameterized Constructor to set the Object of GameState.
     *
     * @param p_gameState The Current GameState
     */
    public ExceptionLogHandler(GameState p_gameState){
        d_gameState = p_gameState;
    }

    /**
     * The method updates the GameState Log.
     *
     * @param p_t The Thread of Exception.
     * @param p_e The Throwable Instance of Exception
     */
    @Override
    public void uncaughtException(Thread p_t, Throwable p_e){
        d_gameState.logUpdate(p_e.getMessage(), "effect");
    }
}
