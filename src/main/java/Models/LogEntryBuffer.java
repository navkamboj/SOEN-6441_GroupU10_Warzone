package Models;

import Views.LogWriter;
import java.util.Observable;

public class LogEntryBuffer extends Observable{

    String d_messageLog;

    public LogEntryBuffer(){
        LogWriter l_writerLog = new LogWriter();
        this.addObserver(l_writerLog);
    }

    public String getD_messageLog(){
        return d_messageLog;
    }

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
            case "effect":
                d_messageLog = "Log : " + p_messageToUpdate + System.lineSeparator();
            case "start":
            case "end":
                d_messageLog = p_messageToUpdate + System.lineSeparator();
                break;
        }
        setChanged();
        notifyObservers();
    }
}
