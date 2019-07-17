package Network.ClientServer;

import Network.BaseNetwork.BaseNetwork;
import Network.BaseNetwork.NetworkConnection;
import Network.ClientServer.SendReceive.ClientSendReceive;
import Network.ClientServer.SendReceive.SendReceiveType;
import Network.ClientServer.SendReceive.ServerSendReceive;

/**
 * Created by Arnob on 31/01/2015.
 * Connection functions for Client/Server network
 */
public class CS_Connection implements NetworkConnection {

    public static final int connectionTypeAndCompatibilityCode = 1;  // by this number, client will detect connection type and compatibility

    private static SendReceiveType sendReceiveType;

    public CS_Connection() {
        if (BaseNetwork.thisPC_createdGame)
            sendReceiveType = new ServerSendReceive();
        else
            sendReceiveType = new ClientSendReceive();
    }

    public void sendToOthers(byte byteToSend) {
        sendReceiveType.send(byteToSend);
    }

    public byte receiveFromPlayer(int playerID) {
        return sendReceiveType.receive(playerID);
    }

    public void closeAllConnection() {
        new CloseAllConnection();
    }
}
