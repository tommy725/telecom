package backend;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class AudioTransmitter {
    private Queue<Byte> buffer = new LinkedList<>();
    private Port senderPort;
    Thread thread;

    public AudioTransmitter(Port senderPort) {
        this.senderPort = senderPort;
        thread = new Thread(() -> {
            while(true) {
                try {
                    sendFromBuffer();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * Add to list buffer
     * @param bytes bytes to add
     */
    public void addToBuffer(byte[] bytes) {
        for (byte b : bytes) {
            buffer.add(b);
        }
    }

    /**
     * Send from buffer if enough data
     * @throws IOException exception
     */
    public void sendFromBuffer() throws IOException, InterruptedException {
        if (buffer != null && buffer.size() >= 8000) { //if buffer size is higher than 8000
            byte[] byteArray = new byte[8000];
            for (int i = 0; i < 8000; i++) {
                byteArray[i] = buffer.poll(); //remove from buffer, move to byte array
            }
            senderPort.send(Compressor.compress(byteArray)); //send compressed data
        } else {
            Thread.sleep(1);
        }
    }
}
