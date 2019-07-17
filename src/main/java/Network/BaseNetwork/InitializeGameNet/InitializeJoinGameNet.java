package Network.BaseNetwork.InitializeGameNet;

import AppInfo.BaseWindow;
import AppInfo.Customization.AllSettings;
import GameObject.Player.PlayerEnum.Color;
import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.CheckReadyConnection;
import Network.ClientServer.CS_Connection;
import Network.PeerToPeer.JoinGameNet;
import Network.PeerToPeer.P2P_Connection;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.CompatibilityErrorScene;
import Scene.AllScenes.MenuScenes.ConnectionErrorScene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Arnob on 31/01/2015.
 * This class is for detecting network connection type
 * and invoke JoinGameNet from appropriate package (either PeerToPeer or ClientServer)
 */
public class InitializeJoinGameNet implements Runnable {
    private static boolean isInitialized;

    private static CheckReadyConnection checkReadyConnection;

    public static Socket socket;
    public static ObjectOutputStream sendToServer;
    public static ObjectInputStream receiveFromServer;

    public InitializeJoinGameNet() {
        isInitialized = false;
        BaseNetwork.thisPC_createdGame = false;
        new Thread(this).start();
    }

    public void run() {
        // connect to server
        try {
            socket = new Socket(AllSettings.userSettings.UserIP, BaseNetwork.ipPort);
            socket.setTcpNoDelay(true);
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // get server stream
        try {
            sendToServer = new ObjectOutputStream(socket.getOutputStream());
            receiveFromServer = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // receive connection type and compatibility code
        int joinGameConnectionType;
        try {
            joinGameConnectionType = receiveFromServer.readInt() & 15;   // checking 4 bits from LSB
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new CompatibilityErrorScene();     // implicit compatibility error
            return;
        }

        // check for compatibility
        if (joinGameConnectionType == P2P_Connection.connectionTypeAndCompatibilityCode) {
            checkReadyConnection = new JoinGameNet();
            BaseNetwork.networkConnection = new P2P_Connection();
        } else if (joinGameConnectionType == CS_Connection.connectionTypeAndCompatibilityCode) {
            checkReadyConnection = new Network.ClientServer.JoinGameNet();
            BaseNetwork.networkConnection = new CS_Connection();
        } else {    // other combination that can happen if server version is incompatible with this one
            try {
                sendToServer.writeBoolean(false);   // explicit compatibility error; server should discount and disconnect this client
                sendToServer.flush();
            } catch (IOException e) {
                e.printStackTrace();    // actually it is the error of both network and compatibility
            }
            BaseWindow.scene = new CompatibilityErrorScene();
            return;
        }

        // tell the server: this client is compatible with the server
        try {
            sendToServer.writeBoolean(true);    // no compatibility error
            sendToServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // receive initial data
        try {
            receiveInitialData();
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // now start additional work to join the game
        checkReadyConnection.start();

        // primary initialization of joining game is complete!
        isInitialized = true;
    }

    private void receiveInitialData() throws IOException {
        MainGame.totalPlayers = receiveFromServer.readInt();
        BaseNetwork.totalPlayersLessOne = MainGame.totalPlayers - 1;
        MainGame.totalHumanPlayers = receiveFromServer.readInt();
        BaseNetwork.totalHumanPlayersLessOne = MainGame.totalHumanPlayers - 1;
        MainGame.thisPlayerColor = Color.valuesCached()[receiveFromServer.readInt()];
        MainGame.matchToWin = receiveFromServer.readInt();
        MainGame.matchDurationMinute = receiveFromServer.readInt();
    }

    public static boolean isReady() {
        return isInitialized && checkReadyConnection.isReady();
    }
}
