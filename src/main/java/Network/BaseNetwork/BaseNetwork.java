package Network.BaseNetwork;

import GameObject.Player.PlayerEnum.Color;
import Scene.AllScenes.GameScenes.MainGame.MainGame;

/**
 * Created by Arnob on 11/11/2014.
 * This class contains all common variables for different connections and some general methods.<br/>
 */
public class BaseNetwork {

    public static final int ipPort = 23194;

    public static final int serverSocketTimeout = 60000;    // in millisecond
    public static final int receiveDataTimeout = 30000;     // in millisecond

    public static final int serverListIndex = 0;

    public static final int maxPlayers = Color.totalColor;
    public static final int maxPlayersLessOne = maxPlayers - 1;
    public static final int[] indexListFromPlayerID = new int[maxPlayers];

    public static int totalPlayersLessOne = MainGame.totalPlayers - 1;  // this has been used in several loops to reduce same calculation
    public static int totalHumanPlayersLessOne = MainGame.totalHumanPlayers - 1;    // this has been used in several loops to reduce same calculation

    public static NetworkConnection networkConnection;
    public static boolean thisPC_createdGame;

    public static void generateIndexListFromPlayerID() {
        int thisPlayerColorOrdinal = MainGame.thisPlayerColor.ordinal();
        for (int i = 0; i < thisPlayerColorOrdinal; i++) {
            indexListFromPlayerID[i] = i;
        }
        for (int i = thisPlayerColorOrdinal + 1; i < MainGame.totalHumanPlayers; i++) {
            indexListFromPlayerID[i] = i - 1;
        }
        for (int i = MainGame.totalHumanPlayers; i < MainGame.totalPlayers; i++) {
            indexListFromPlayerID[i] = serverListIndex;
        }
    }

    public static void closeAllConnection() {
        if (networkConnection != null) networkConnection.closeAllConnection();
        NetworkTask.doTaskAfterClosingConnection();
    }

}
