package Network.PeerToPeer;

import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.NetworkTask;

import java.io.IOException;

/**
 * Created by Arnob on 14/11/2014.
 * This thread closes all connections after champion is determined.
 */
public class CloseAllConnection implements Runnable {
    public CloseAllConnection() {
        new Thread(this).start();
    }

    public void run() {
        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {

            P2P_Connection.sendingThreadList[i].stop();
            P2P_Connection.receivingThreadList[i].stop();

            try {
                P2P_Connection.socketList[i].close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        NetworkTask.doTaskAfterClosingConnection();
    }
}
