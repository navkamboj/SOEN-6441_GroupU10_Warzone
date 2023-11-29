package Services;

import Constants.GameConstants;
import Models.Phase;

import java.io.*;

/**
 * This class is used to load and save game file.
 */
public class GameService {

    /**
     * save current game to specific file after serializing.
     *
     * @param p_currentPhase instance of current game phase
     * @param p_fileName name of the file
     */
    public static void saveGame(Phase p_currentPhase, String p_fileName){
        try {
            FileOutputStream l_saveGameFile =new FileOutputStream(GameConstants.SRC_MAIN_RESOURCES + "/" + p_fileName);
            ObjectOutputStream l_objectStreamSaveGameFile=new ObjectOutputStream(l_saveGameFile);
            l_objectStreamSaveGameFile.writeObject(p_currentPhase);
            l_objectStreamSaveGameFile.flush();
            l_objectStreamSaveGameFile.close();
        } catch (Exception l_exception) {
            l_exception.printStackTrace();
        }
    }

    /**
     * This method will De-serialized the phase that is stored in specific file.
     *
     * @param p_fileName name of the file
     * @return the Phase that is saved in file
     * @throws IOException input output exception when file not found
     * @throws ClassNotFoundException if Phase class not found
     */
    public static Phase loadGame(String p_fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream l_inputStream = new ObjectInputStream(new FileInputStream(GameConstants.SRC_MAIN_RESOURCES + "/" + p_fileName));
        Phase l_phase = (Phase) l_inputStream.readObject();

        l_inputStream.close();
        return l_phase;
    }
}
