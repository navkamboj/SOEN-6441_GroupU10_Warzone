package Utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import Constants.ApplicationConstants;

/**
 * Common Utility class that checks all null/empty strings, objects and collections.
 *
 * @author Yatish Chutani
 * @version 1.0.0
 */
public class CommonUtil {

    /**
     * Method for checking if the string is empty or not.
     *
     * @param p_str to check the string
     * @return boolean true if String is empty else false
     */
    public static boolean isEmpty(String p_str){
        return(p_str == null || p_str.trim().isEmpty());
    }

    /**
     * Method for checking if the string is non-empty or not.
     *
     * @param p_str to check the string
     * @return boolean true if string is not empty else false
     */
    public static boolean isNotEmpty(String p_str){
        return !isEmpty(p_str);
    }

    /**
     * Method for checking if the object is null or not.
     *
     * @param p_object to check the object
     * @return true if object is null else false
     */
    public static boolean isNull(Object p_object){
        return (p_object == null);
    }

    /**
     * Method for checking if the map is empty or not.
     *
     * @param p_map to check the Map
     * @return true if map is empty else false
     */
    public static boolean isEmptyMap(Map<?, ?> p_map){
        return(p_map == null || p_map.isEmpty());
    }

    /**
     * Method for checking if the collection is empty or not
     *
     * @param p_collection to check the collection
     * @return true if collection is empty else false
     */
    public static boolean isEmptyCollection(Collection<?> p_collection){
        return(p_collection == null || p_collection.isEmpty());
    }

    /**
     * Used for generating an absolute file path from the given map file.
     *
     * @param p_fileName filename to map it with the file path
     * @return string map file along with its path
     */
    public static String getMapFilePath(String p_fileName){
        String l_absolutePath =new File("").getAbsolutePath();
        return l_absolutePath + File.separator + ApplicationConstants.SRC_MAIN_RESOURCES + File.separator + p_fileName;
    }
}