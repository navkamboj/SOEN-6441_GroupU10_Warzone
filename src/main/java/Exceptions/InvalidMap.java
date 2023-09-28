package Exceptions;

/**
 * This class throws Invalid Map Exceptions
 *
 * @author Yatish Chutani
 * @version 1.0.0
 */
public class InvalidMap extends Exception {

    /**
     * InvalidMap constructor prints message when the  exception is caught in an Invalid Map case
     *
     * @param p_message this message is returned when the map is invalid.
     */
    public InvalidMap(String p_message) {
        super(p_message);
    }
}
