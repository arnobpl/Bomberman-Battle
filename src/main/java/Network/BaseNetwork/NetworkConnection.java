package Network.BaseNetwork;

/**
 * Created by Arnob on 31/01/2015.
 * Common interface for the two types of connections: Peer-to-Peer (P2P) and Client/Server (CS)
 */
public interface NetworkConnection {
    /**
     * This method sends <code>byteToSend</code> data to the other players
     * directly by P2P network or indirectly by CS network (except server).
     */
    void sendToOthers(byte byteToSend);

    /**
     * This method receives data from the desired player of the <code>playerID</code>.
     */
    byte receiveFromPlayer(int playerID);

    /**
     * This closes all connections after champion is determined.
     */
    void closeAllConnection();
}
