package Models;

import Views.LogWriter;

import java.io.Serializable;
import java.util.Observable;

/**
 * The class records and stores logs for different phases of the game
 *
 * @author Yatish Chutani
 * @version 2.0.0
 */
public class LogEntryBuffer extends Observable implements Serializable {

    /**
     * The Log Message that will be recorded.
     */
    String d_messageLog;

    /**
     * It initializes the class instance by incorporating a LogWriter Observer object.
     */
    public LogEntryBuffer(){
        LogWriter l_writerLog = new LogWriter();
        this.addObserver(l_writerLog);
    }

    /**
     * A Getter method for the Log Message.
     *
     * @return The Log Message
     */
    public String getD_messageLog(){
        return d_messageLog;
    }

    /**
     * Creates the log message and informs the observer objects.
     *
     * @param p_messageToUpdate The Log Message to Set
     * @param p_logType The Type of Log : Order, Command, Phase or Effect
     */
    public void currentLog(String p_messageToUpdate, String p_logType){

        switch(p_logType.toLowerCase()){
            case "phase":
                d_messageLog = System.lineSeparator() + "=====" + p_messageToUpdate + "=====" + System.lineSeparator() + System.lineSeparator();
                break;
            case "command":
                d_messageLog = System.lineSeparator() + "The Entered Command : " + p_messageToUpdate + System.lineSeparator();
                break;
            case "order":
                d_messageLog = System.lineSeparator() + "The Issued Order : " + p_messageToUpdate + System.lineSeparator();
                break;
            case "effect":
                d_messageLog = "Log : " + p_messageToUpdate + System.lineSeparator();
                break;
            case "start":
            case "end":
                d_messageLog = p_messageToUpdate + System.lineSeparator();
                break;
        }
        setChanged();
        notifyObservers();
    }
}
