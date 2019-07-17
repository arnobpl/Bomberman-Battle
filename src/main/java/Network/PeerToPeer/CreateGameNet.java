package Network.PeerToPeer;

import AppInfo.BaseWindow;
import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.CheckReadyConnection;
import Network.BaseNetwork.CommunicationThread.ReceivingThread;
import Network.BaseNetwork.CommunicationThread.SendingThread;
import Network.BaseNetwork.InitializeGameNet.InitializeCreateGameNet;
import Scene.AllScenes.MenuScenes.ConnectionErrorScene;

import java.io.IOException;

/**
 * Created by Arnob on 08/11/2014.
 * This class contains all vital things that are necessary for creating a game.
 */

public class CreateGameNet implements Runnable, CheckReadyConnection {
    private static boolean ready;   // this will be true when all connections are established

    private static final String[] socketStringList = new String[BaseNetwork.maxPlayersLessOne]; // this is needed to get IP addresses of other computers

    public CreateGameNet() {
        ready = false;
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        // assigning data from InitializeCreateGameNet and threads
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            P2P_Connection.socketList[i] = InitializeCreateGameNet.sockets[i];
            P2P_Connection.sendStreamList[i] = InitializeCreateGameNet.sendToClients[i];
            P2P_Connection.receiveStreamList[i] = InitializeCreateGameNet.receiveFromClients[i];
            socketStringList[i] = InitializeCreateGameNet.socketStringList[i];
            P2P_Connection.sendingThreadList[i] = new SendingThread(P2P_Connection.sendStreamList[i]);
            P2P_Connection.receivingThreadList[i] = new ReceivingThread(P2P_Connection.receiveStreamList[i]);
        }

        // give IP of other servers to other clients so that they can connect each other
        for (int i = 1; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            try {
                for (int j = 0; j < i; j++) {
                    P2P_Connection.sendStreamList[i].writeObject(socketStringList[j]);
                    P2P_Connection.sendStreamList[i].flush();
                    P2P_Connection.receiveStreamList[i].readBoolean(); // this will return 'true' if connection is established in the IP
                }
            } catch (IOException e) {
                e.printStackTrace();
                BaseWindow.scene = new ConnectionErrorScene(e);
                return;
            }
        }

        // wait until other computers are connected to each other
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            try {
                P2P_Connection.receiveStreamList[i].readBoolean();     // this will return 'true' if successful
            } catch (IOException e) {
                e.printStackTrace();
                BaseWindow.scene = new ConnectionErrorScene(e);
                return;
            }
        }

        // Now all clients are ready! Send this message to the other clients
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            try {
                P2P_Connection.sendStreamList[i].writeBoolean(true);
                P2P_Connection.sendStreamList[i].flush();
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
            P2P_Connection.sendingThreadList[i].start();
            P2P_Connection.receivingThreadList[i].start();
        }

        // now ready to play game!
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }
}
