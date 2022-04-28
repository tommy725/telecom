package backend;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static backend.BitsToInt.bitsToInt;

public class ReceiverSocket {
    private Socket socket;
    protected List<String> finalResult = new ArrayList<>();
    private Path path;
    private HuffmanNode root;
    private boolean ended = false;

    public ReceiverSocket(Path path, HuffmanNode root, String serverIP) {
        try {
            do {
                try {
                    socket = new Socket(serverIP, 12345);
                } catch (ConnectException ignored) {
                }
            } while (socket == null);
            this.root = root;
            this.path = path;
            listen(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(InputStream is) {
        try {
            while (!ended) {
                String bString = Integer.toBinaryString(is.read());
                if (bString.length() > 8) {
                    bString = bString.substring(bString.length() - 8);
                }
                if (bString.length() < 8) {
                    bString = "0".repeat(8 - bString.length()) + bString;
                }
                addToReceivedBlock(bString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add byte array to Byte result list
     *
     * @param receivedByte byte array
     */
    public void addToReceivedBlock(String receivedByte) {
        if (finalResult.size() > 0 && bitsToInt(finalResult.get(finalResult.size() - 1)) == 4
                && bitsToInt(receivedByte) < 8) {
            finalResult.remove(finalResult.size() - 1);
            endTransmissionAndSave(bitsToInt(receivedByte));
            return;
        }
        finalResult.add(receivedByte);
    }

    /**
     * End transmission, remove 0 from the last String based on last byte, save to file
     *
     * @param zeros number of 0 added in last but one byte
     */
    public void endTransmissionAndSave(int zeros) {
        String last = finalResult.remove(finalResult.size() - 1);
        finalResult.add(last.substring(0, 8 - zeros));
        StringBuilder finalSB = new StringBuilder();
        for (String s : finalResult) {
            finalSB.append(s);
        }
        String result = HuffmanCoding.decode(finalSB.toString().toCharArray(), root);
        try {
            Files.writeString(path, result.replace("\n", "\r\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ended = true;
    }
}
