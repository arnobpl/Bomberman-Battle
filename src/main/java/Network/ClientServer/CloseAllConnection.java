package Network.ClientServer;

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
        if (BaseNetwork.thisPC_createdGame) {
            for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {

                CreateGameNet.sendingThreadList[i].stop();
                CreateGameNet.receivingThreadList[i].stop();

                try {
                    CreateGameNet.socketList[i].close();
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JoinGameNet.sendingThread.stop();
            JoinGameNet.receivingThread.stop();

            try {
                JoinGameNet.socket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        NetworkTask.doTaskAfterClosingConnection();
    }
}
