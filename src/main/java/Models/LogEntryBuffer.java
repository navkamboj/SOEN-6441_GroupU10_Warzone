package Models;

import Views.LogWriter;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.TimeZone;

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
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

        switch(p_logType.toLowerCase()){
            case "phase":
                d_messageLog = f.format(c.getTime()) + " " + System.lineSeparator() + "=====" + p_messageToUpdate + "=====" + System.lineSeparator() + System.lineSeparator();
                break;
            case "command":
                d_messageLog = f.format(c.getTime()) + " " + System.lineSeparator() + "The Entered Command : " + p_messageToUpdate + System.lineSeparator();
                break;
            case "order":
                d_messageLog = f.format(c.getTime()) + " " + System.lineSeparator() + "The Issued Order : " + p_messageToUpdate + System.lineSeparator();
                break;
            case "effect":
                d_messageLog = f.format(c.getTime()) + " " + "Log : " + p_messageToUpdate + System.lineSeparator();
                break;
            case "start":
            case "end":
                d_messageLog = f.format(c.getTime()) + " " + p_messageToUpdate + System.lineSeparator();
                break;
        }
        setChanged();
        notifyObservers();
    }
}
