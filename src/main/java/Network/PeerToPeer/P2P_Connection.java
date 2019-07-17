package Network.PeerToPeer;

import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.CommunicationThread.ReceivingThread;
import Network.BaseNetwork.CommunicationThread.SendingThread;
import Network.BaseNetwork.NetworkConnection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Arnob on 31/01/2015.
 * Connection functions for Peer-to-Peer network
 */
public class P2P_Connection implements NetworkConnection {

    public static final int connectionTypeAndCompatibilityCode = 0;  // by this number, client will detect connection type and compatibility

    public static final Socket[] socketList = new Socket[BaseNetwork.maxPlayersLessOne]; // only for closing socket

    public static final ObjectOutputStream[] sendStreamList = new ObjectOutputStream[BaseNetwork.maxPlayersLessOne];
    public static final ObjectInputStream[] receiveStreamList = new ObjectInputStream[BaseNetwork.maxPlayersLessOne];

    public static final SendingThread[] sendingThreadList = new SendingThread[BaseNetwork.maxPlayersLessOne];
    public static final ReceivingThread[] receivingThreadList = new ReceivingThread[BaseNetwork.maxPlayersLessOne];

    public void sendToOthers(byte byteToSend) {
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            sendingThreadList[i].send(byteToSend);
        }
    }

    public byte receiveFromPlayer(int playerID) {
        return receivingThreadList[BaseNetwork.indexListFromPlayerID[playerID]].receive();
    }

    public void closeAllConnection() {
        new CloseAllConnection();
    }
}
