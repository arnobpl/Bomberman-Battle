package Network.ClientServer.SendReceive;

import Network.BaseNetwork.BaseNetwork;
import Network.ClientServer.CreateGameNet;

/**
 * Created by Arnob on 31/01/2015.
 * Defines the mechanism of sending and receiving of server in Client/Server (CS) connection.
 */
public class ServerSendReceive implements SendReceiveType {
    public void send(byte byteToSend) {
//        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
//            try {
//                CreateGameNet.sendStreamList[i].writeByte(byteToSend);
//                CreateGameNet.sendStreamList[i].flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }

        for (int i = 0; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            CreateGameNet.sendingThreadList[i].send(byteToSend);
        }
    }

    public byte receive(int playerID) {
//        byte receivedDataBuffer = 0x00;
//        try {
//            receivedDataBuffer = CreateGameNet.receiveStreamList[BaseNetwork.indexListFromPlayerID[playerID]].readByte();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        try {
//            for (int i = 0; i < BaseNetwork.indexListFromPlayerID[playerID]; i++) {
//                CreateGameNet.sendStreamList[i].writeByte(receivedDataBuffer);
//                CreateGameNet.sendStreamList[i].flush();
//            }
//            for (int i = BaseNetwork.indexListFromPlayerID[playerID] + 1; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
//                CreateGameNet.sendStreamList[i].writeByte(receivedDataBuffer);
//                CreateGameNet.sendStreamList[i].flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        return receivedDataBuffer;

        byte receivedDataBuffer = CreateGameNet.receivingThreadList[BaseNetwork.indexListFromPlayerID[playerID]].receive();

//        try {
//            for (int i = 0; i < BaseNetwork.indexListFromPlayerID[playerID]; i++) {
//                CreateGameNet.sendStreamList[i].writeByte(receivedDataBuffer);
//                CreateGameNet.sendStreamList[i].flush();
//            }
//            for (int i = BaseNetwork.indexListFromPlayerID[playerID] + 1; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
//                CreateGameNet.sendStreamList[i].writeByte(receivedDataBuffer);
//                CreateGameNet.sendStreamList[i].flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

        for (int i = 0; i < BaseNetwork.indexListFromPlayerID[playerID]; i++) {
            CreateGameNet.sendingThreadList[i].send(receivedDataBuffer);
        }
        for (int i = BaseNetwork.indexListFromPlayerID[playerID] + 1; i < BaseNetwork.totalHumanPlayersLessOne; i++) {
            CreateGameNet.sendingThreadList[i].send(receivedDataBuffer);
        }

        return receivedDataBuffer;
    }
}
