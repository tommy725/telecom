package backend;

import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;

public class ReceiverSocket {
    private SourceDataLine speakerLine;
    private Socket socket;

    public ReceiverSocket(String serverIP, SourceDataLine speakerLine) {
        this.speakerLine = speakerLine;
        try {
            do {
                try {
                    socket = new Socket(serverIP, 12345);
                } catch (ConnectException ignored) {
                }
            } while (socket == null);
            listen(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(InputStream is) {
        try {
            while (true) {
                byte[] bArray = new byte[4];
                for (int i = 0; i < bArray.length; i++) {
                    bArray[i] = (byte) is.read();
                }
                writeToSpeakerLine(bArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write received data directly to the speaker
     *
     * @param data byte array to write
     */
    public void writeToSpeakerLine(byte[] data) {
        try {
            speakerLine.write(data, 0, 4); //write to speaker line
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
