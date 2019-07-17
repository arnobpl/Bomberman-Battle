package Network.BaseNetwork.InitializeGameNet;

import AppInfo.BaseWindow;
import GameObject.Player.PlayerEnum.Color;
import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.CheckReadyConnection;
import Network.ClientServer.CS_Connection;
import Network.PeerToPeer.CreateGameNet;
import Network.PeerToPeer.P2P_Connection;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.ConnectionErrorScene;
import Scene.AllScenes.MenuScenes.OnlineGame.CreateGame.CreateGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Arnob on 07/02/2015.
 * This class performs the common tasks of creating a game in both connections (P2P and CS).
 */
public class InitializeCreateGameNet implements Runnable {
    private static boolean isInitialized;

    private static int connectionTypeAndCompatibilityCode;
    private static CheckReadyConnection checkReadyConnection;

    public static final Socket[] sockets = new Socket[BaseNetwork.maxPlayersLessOne];
    public static final ObjectOutputStream[] sendToClients = new ObjectOutputStream[BaseNetwork.maxPlayersLessOne];
    public static final ObjectInputStream[] receiveFromClients = new ObjectInputStream[BaseNetwork.maxPlayersLessOne];
    public static final String[] socketStringList = new String[BaseNetwork.maxPlayersLessOne];


    public InitializeCreateGameNet() {
        isInitialized = false;

        MainGame.thisPlayerColor = Color.White;
        BaseNetwork.totalPlayersLessOne = MainGame.totalPlayers - 1;
        BaseNetwork.totalHumanPlayersLessOne = MainGame.totalHumanPlayers - 1;
        BaseNetwork.thisPC_createdGame = true;

        if (CreateGame.networkConnectionType == 0) {
            connectionTypeAndCompatibilityCode = P2P_Connection.connectionTypeAndCompatibilityCode;
            BaseNetwork.networkConnection = new P2P_Connection();
        } else {
            connectionTypeAndCompatibilityCode = CS_Connection.connectionTypeAndCompatibilityCode;
            BaseNetwork.networkConnection = new CS_Connection();
        }
        new Thread(this).start();
    }

    public void run() {
        // create server socket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(BaseNetwork.ipPort, BaseNetwork.totalPlayersLessOne);
            serverSocket.setSoTimeout(BaseNetwork.serverSocketTimeout);
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        // connect to all computers
        for (int i = 1; i < MainGame.totalHumanPlayers; i++) {
            try {
                boolean isCompatible = initialTaskToEachClient(i, serverSocket.accept());

                if (!isCompatible) i--;     // the current client is not compatible, so discount  it
            } catch (IOException e) {
                e.printStackTrace();
                BaseWindow.scene = new ConnectionErrorScene(e);
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }
        }

        // close server socket
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // now invoke CreateGameNet class according to the connection type
        if (CreateGame.networkConnectionType == 0)
            checkReadyConnection = new CreateGameNet();
        else
            checkReadyConnection = new Network.ClientServer.CreateGameNet();

        // now start additional work to create the game
        checkReadyConnection.start();

        // primary initialization of creating game is complete!
        isInitialized = true;
    }

    /**
     * @return <code>true</code> if client is compatible, otherwise <code>false</code>
     */
    private boolean initialTaskToEachClient(int playerID, Socket acceptedSocket) throws IOException {
        int listIndex = playerID - 1;
        socketStringList[listIndex] = ((InetSocketAddress) acceptedSocket.getRemoteSocketAddress()).getAddress().toString().substring(1);

        acceptedSocket.setTcpNoDelay(true);

        sockets[listIndex] = acceptedSocket;
        sendToClients[listIndex] = new ObjectOutputStream(acceptedSocket.getOutputStream());
        receiveFromClients[listIndex] = new ObjectInputStream(acceptedSocket.getInputStream());

        sendToClients[listIndex].writeInt(connectionTypeAndCompatibilityCode);   // send connection type and compatibility code
        sendToClients[listIndex].flush();

        boolean isCompatible;
        try {
            isCompatible = receiveFromClients[listIndex].readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                acceptedSocket.close();     // disconnect incompatible client
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;           // the client is not compatible implicitly
        }
        if (!isCompatible) {
            try {
                acceptedSocket.close();     // disconnect incompatible client
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;           // the client is not compatible explicitly
        }

        sendInitialData(listIndex);  // the client is compatible, so send initial data

        return true;
    }

    private void sendInitialData(int listIndex) throws IOException {
        sendToClients[listIndex].writeInt(MainGame.totalPlayers);
        sendToClients[listIndex].writeInt(MainGame.totalHumanPlayers);
        sendToClients[listIndex].writeInt(listIndex + 1);
        sendToClients[listIndex].writeInt(MainGame.matchToWin);
        sendToClients[listIndex].writeInt(MainGame.matchDurationMinute);
        sendToClients[listIndex].flush();
    }

    public static boolean isReady() {
        return isInitialized && checkReadyConnection.isReady();
    }
}
