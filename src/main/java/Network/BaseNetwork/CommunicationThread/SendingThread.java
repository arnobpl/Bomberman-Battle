package Network.BaseNetwork.CommunicationThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Arnob on 07/02/2015.
 * This class contains the operation for sending so that it can be used with a thread pool.
 */
public class SendingThread implements Runnable {

    private static final int sendingDataQueueCapacity = 16;

    private final BlockingQueue<Byte> byteToSendQueue = new ArrayBlockingQueue<>(sendingDataQueueCapacity);
    private ExecutorService sendingThread;
    private ObjectOutputStream sendingStream;

    public SendingThread(ObjectOutputStream sendingStream) {
        this.sendingStream = sendingStream;
    }

    public void start() {
        sendingThread = Executors.newSingleThreadExecutor();
    }

    public void run() {
        try {
            sendingStream.writeByte(byteToSendQueue.take());
            sendingStream.flush();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte byteToSend) {
        try {
            byteToSendQueue.put(byteToSend);
            sendingThread.execute(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        sendingThread.shutdown();
    }
}
