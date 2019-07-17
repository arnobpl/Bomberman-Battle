package Network.PeerToPeer;

import AppInfo.BaseWindow;
import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.CheckReadyConnection;
import Network.BaseNetwork.CommunicationThread.ReceivingThread;
import Network.BaseNetwork.CommunicationThread.SendingThread;
import Network.BaseNetwork.InitializeGameNet.InitializeJoinGameNet;
import Scene.AllScenes.GameScenes.MainGame.MainGame;
import Scene.AllScenes.MenuScenes.ConnectionErrorScene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Arnob on 08/11/2014.
 * This class contains all vital things that are necessary for joining a game.
 */
public class JoinGameNet implements Runnable, CheckReadyConnection {
    private static volatile boolean ready = false;    // this will be true when all connections are established

    public JoinGameNet() {
        ready = false;
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        // assigning data from InitializeJoinGameNet
        P2P_Connection.socketList[BaseNetwork.serverListIndex] = InitializeJoinGameNet.socket;
        P2P_Connection.sendStreamList[BaseNetwork.serverListIndex] = InitializeJoinGameNet.sendToServer;
        P2P_Connection.receiveStreamList[BaseNetwork.serverListIndex] = InitializeJoinGameNet.receiveFromServer;

        // assigning sending and receiving threads
        P2P_Connection.sendingThreadList[BaseNetwork.serverListIndex] = new SendingThread(P2P_Connection.sendStreamList[BaseNetwork.serverListIndex]);
        P2P_Connection.receivingThreadList[BaseNetwork.serverListIndex] = new ReceivingThread(P2P_Connection.receiveStreamList[BaseNetwork.serverListIndex]);

        // create peer server
        Thread createPeerServerThread = new CreatePeerServer();

        // connect to peer server
        Thread joinPeerServerThread = new JoinPeerServer();

        // wait for peer server-client connection establishment
        try {
            createPeerServerThread.join();
            joinPeerServerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // send main server that this client is ready to play
        try {
            P2P_Connection.sendStreamList[BaseNetwork.serverListIndex].writeBoolean(true);
            P2P_Connection.sendStreamList[BaseNetwork.serverListIndex].flush();
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // now receive data from the main server to know if server is ready
        try {
            P2P_Connection.receiveStreamList[BaseNetwork.serverListIndex].readBoolean();     // this will return 'true' if successful
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // generate index from playerID
        BaseNetwork.generateIndexListFromPlayerID();

        // starting sending and receiving threads for continuous communication
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            P2P_Connection.sendingThreadList[i].start();
            P2P_Connection.receivingThreadList[i].start();
        }

        ready = true;
    }

    private static class CreatePeerServer extends Thread {
        public CreatePeerServer() {
            start();
        }

        public void run() {
            int peerClientToConnect = MainGame.totalHumanPlayers - MainGame.thisPlayerColor.ordinal() - 1;
            if (MainGame.thisPlayerColor.ordinal() + 1 < MainGame.totalHumanPlayers) {
                ServerSocket peerSocket = null;
                try {
                    peerSocket = new ServerSocket(BaseNetwork.ipPort + MainGame.thisPlayerColor.ordinal(), peerClientToConnect);
                    peerSocket.setSoTimeout(BaseNetwork.serverSocketTimeout);
                    for (int i = 0; i < peerClientToConnect; i++) {
                        Socket acceptedSocket = peerSocket.accept();
                        acceptedSocket.setTcpNoDelay(true);

                        int peerClientListIndex = i + MainGame.thisPlayerColor.ordinal();
                        P2P_Connection.socketList[peerClientListIndex] = acceptedSocket;
                        P2P_Connection.sendStreamList[peerClientListIndex] = new ObjectOutputStream(acceptedSocket.getOutputStream());
                        P2P_Connection.receiveStreamList[peerClientListIndex] = new ObjectInputStream(acceptedSocket.getInputStream());
                        P2P_Connection.sendingThreadList[peerClientListIndex] = new SendingThread(P2P_Connection.sendStreamList[peerClientListIndex]);
                        P2P_Connection.receivingThreadList[peerClientListIndex] = new ReceivingThread(P2P_Connection.receiveStreamList[peerClientListIndex]);
                    }
                    peerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    BaseWindow.scene = new ConnectionErrorScene(e);
                    try {
                        if (peerSocket != null) peerSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    private static class JoinPeerServer extends Thread {
        public JoinPeerServer() {
            start();
        }

        public void run() {
            int peerServerToConnect = MainGame.thisPlayerColor.ordinal() - 1;
            for (int i = 0; i < peerServerToConnect; i++) {
                try {
                    String serverIP = (String) P2P_Connection.receiveStreamList[BaseNetwork.serverListIndex].readObject();
                    Socket peerClientSocket = new Socket(serverIP, BaseNetwork.ipPort + i + 1);
                    peerClientSocket.setTcpNoDelay(true);

                    P2P_Connection.sendStreamList[BaseNetwork.serverListIndex].writeBoolean(true);
                    P2P_Connection.sendStreamList[BaseNetwork.serverListIndex].flush();

                    int peerServerListIndex = i + 1;
                    P2P_Connection.sendStreamList[peerServerListIndex] = new ObjectOutputStream(peerClientSocket.getOutputStream());
                    P2P_Connection.receiveStreamList[peerServerListIndex] = new ObjectInputStream(peerClientSocket.getInputStream());
                    P2P_Connection.sendingThreadList[peerServerListIndex] = new SendingThread(P2P_Connection.sendStreamList[peerServerListIndex]);
                    P2P_Connection.receivingThreadList[peerServerListIndex] = new ReceivingThread(P2P_Connection.receiveStreamList[peerServerListIndex]);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    BaseWindow.scene = new ConnectionErrorScene(e);
                    return;
                }
            }
        }
    }

    public boolean isReady() {
        return ready;
    }
}
