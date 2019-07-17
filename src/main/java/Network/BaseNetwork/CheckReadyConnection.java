package Network.BaseNetwork;

/**
 * Created by Arnob on 31/01/2015.
 * This is used as a common interface for the two types of connections: Peer-to-Peer (P2P) and Client/Server (CS)
 */
public interface CheckReadyConnection {
    void start();

    boolean isReady();
}
