package Network.ClientServer;

import AppInfo.BaseWindow;
import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.CheckReadyConnection;
import Network.BaseNetwork.CommunicationThread.ReceivingThread;
import Network.BaseNetwork.CommunicationThread.SendingThread;
import Network.BaseNetwork.InitializeGameNet.InitializeCreateGameNet;
import Scene.AllScenes.MenuScenes.ConnectionErrorScene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Arnob on 08/11/2014.
 * This class contains all vital things that are necessary for creating a game.
 */
public class CreateGameNet implements Runnable, CheckReadyConnection {
    private static boolean ready;   // this will be true when all connections are established

    public static final Socket[] socketList = new Socket[BaseNetwork.maxPlayersLessOne]; // only for closing socket

    public static final ObjectOutputStream[] sendStreamList = new ObjectOutputStream[BaseNetwork.maxPlayersLessOne];
    public static final ObjectInputStream[] receiveStreamList = new ObjectInputStream[BaseNetwork.maxPlayersLessOne];

    public static final SendingThread[] sendingThreadList = new SendingThread[BaseNetwork.maxPlayersLessOne];
    public static final ReceivingThread[] receivingThreadList = new ReceivingThread[BaseNetwork.maxPlayersLessOne];

    public CreateGameNet() {
        ready = false;
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        // assigning data from InitializeCreateGameNet and threads
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            socketList[i] = InitializeCreateGameNet.sockets[i];
            sendStreamList[i] = InitializeCreateGameNet.sendToClients[i];
            receiveStreamList[i] = InitializeCreateGameNet.receiveFromClients[i];
            sendingThreadList[i] = new SendingThread(sendStreamList[i]);
            receivingThreadList[i] = new ReceivingThread(receiveStreamList[i]);
        }

        // Now server is ready! Send this message to the clients
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            try {
                sendStreamList[i].writeBoolean(true);
                sendStreamList[i].flush();
            } catch (IOException e) {
                e.printStackTrace();
                BaseWindow.scene = new ConnectionErrorScene(e);
                return;
            }
        }

        // generate index from playerID
        BaseNetwork.generateIndexListFromPlayerID();

        // starting sending and receiving threads for continuous communication
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            sendingThreadList[i].start();
            receivingThreadList[i].start();
        }

        // now ready to play game!
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }
}
