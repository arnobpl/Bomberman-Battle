package Network.ClientServer.SendReceive;

/**
 * Created by Arnob on 31/01/2015.
 * This is for the different behaviour of sending and receiving between client and server in Client/Server (CS) connection.
 */
public interface SendReceiveType {
    void send(byte byteToSend);

    byte receive(int playerID);
}
