package Network.ClientServer;

import AppInfo.BaseWindow;
import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.CheckReadyConnection;
import Network.BaseNetwork.CommunicationThread.ReceivingThread;
import Network.BaseNetwork.CommunicationThread.SendingThread;
import Network.BaseNetwork.InitializeGameNet.InitializeJoinGameNet;
import Scene.AllScenes.MenuScenes.ConnectionErrorScene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Arnob on 08/11/2014.
 * This class contains all vital things that are necessary for joining a game.
 */
public class JoinGameNet implements Runnable, CheckReadyConnection {
    private static volatile boolean ready = false;    // this will be true when all connections are established

    public static Socket socket;

    public static ObjectOutputStream sendStream;
    public static ObjectInputStream receiveStream;

    public static SendingThread sendingThread;
    public static ReceivingThread receivingThread;

    public JoinGameNet() {
        ready = false;
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        // assigning data from InitializeJoinGameNet
        socket = InitializeJoinGameNet.socket;
        sendStream = InitializeJoinGameNet.sendToServer;
        receiveStream = InitializeJoinGameNet.receiveFromServer;

        // assigning sending and receiving threads
        sendingThread = new SendingThread(sendStream);
        receivingThread = new ReceivingThread(receiveStream);

        // now receive data from the main server to know if server is ready
        try {
            receiveStream.readBoolean();     // this will return 'true' if successful
        } catch (IOException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
            return;
        }

        // generate index from playerID
        BaseNetwork.generateIndexListFromPlayerID();

        // starting sending and receiving threads for continuous communication
        sendingThread.start();
        receivingThread.start();

        ready = true;
    }

    public boolean isReady() {
        return ready;
    }
}
