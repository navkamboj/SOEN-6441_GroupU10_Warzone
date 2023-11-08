package Views;

import Models.LogEntryBuffer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Observer;
import java.util.Observable;

/**
 * The class updates the log file containing entries obtained from the LogEntryBuffer Class.
 *
 * @author Yatish Chutani
 * @version 2.0.0
 */
public class LogWriter implements Observer {

    /**
     * The Updated Observable Object of LogEntry Buffer.
     */
    LogEntryBuffer d_logEntryBuffer;

    /**
     * This method Writes the updated LogEntryBuffer Object into Log file.
     *
     * @param p_observable The Object of LogEntryBuffer.
     * @param p_object The Object
     */
    @Override
    public void update(Observable p_observable, Object p_object){
        d_logEntryBuffer = (LogEntryBuffer) p_observable;
        File l_logfile = new File("LogFile.txt");
        String l_messageLog = d_logEntryBuffer.getD_messageLog();

        try {
            if(l_messageLog.equals("Starting the Game......" + System.lineSeparator() + System.lineSeparator())) {
                Files.newBufferedWriter(Paths.get("LogFile.txt"), StandardOpenOption.TRUNCATE_EXISTING).write(" ");
            }
            Files.write(Paths.get("LogFile.txt"), l_messageLog.getBytes(StandardCharsets.US_ASCII), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch (Exception l_e){
            l_e.printStackTrace();
        }
    }
}
