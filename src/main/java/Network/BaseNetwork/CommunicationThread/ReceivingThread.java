package Network.BaseNetwork.CommunicationThread;

import AppInfo.BaseWindow;
import Network.BaseNetwork.BaseNetwork;
import Scene.AllScenes.MenuScenes.ConnectionErrorScene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arnob on 03/02/2015.
 * This thread receives data from other computers and maintain blocking queue.
 */
public class ReceivingThread implements Runnable {

    private static final int receivingDataQueueCapacity = 16;

    private final BlockingQueue<Byte> receivedDataQueue = new ArrayBlockingQueue<>(receivingDataQueueCapacity);
    private Thread receivingThread;
    private ObjectInputStream receivingStream;

    public ReceivingThread(ObjectInputStream receivingStream) {
        this.receivingStream = receivingStream;
        receivingThread = new Thread(this);
    }

    public void start() {
        receivingThread.start();
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                receivedDataQueue.put(receivingStream.readByte());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public byte receive() {
        byte receivedData = 0x00;
        try {
            receivedData = receivedDataQueue.poll(BaseNetwork.receiveDataTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            BaseWindow.scene = new ConnectionErrorScene(e);
        }
        return receivedData;
    }

    public void stop() {
        receivingThread.interrupt();
    }
}
