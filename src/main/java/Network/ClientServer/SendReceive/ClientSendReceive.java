package Network.ClientServer.SendReceive;

import Network.ClientServer.JoinGameNet;

/**
 * Created by Arnob on 31/01/2015.
 * Defines the mechanism of sending and receiving of client in Client/Server (CS) connection.
 */
public class ClientSendReceive implements SendReceiveType {
    public void send(byte byteToSend) {
//        try {
//            JoinGameNet.sendStream.writeByte(byteToSend);
//            JoinGameNet.sendStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

        JoinGameNet.sendingThread.send(byteToSend);
    }

    public byte receive(int playerID) {
//        try {
//            return JoinGameNet.receiveStream.readByte();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        return 0x00;

        return JoinGameNet.receivingThread.receive();
    }
}
