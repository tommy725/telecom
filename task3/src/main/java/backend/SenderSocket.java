package backend;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static backend.BitsToInt.bitsToInt;

public class SenderSocket {
    private Socket socket;
    private ServerSocket servsock;

    public SenderSocket() {
        try {
            servsock = new ServerSocket(12345);
            socket = servsock.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(int[] bytesToSend, Map<Character, String> codes) throws IOException {
        OutputStream socketOutputStream = socket.getOutputStream();
        StringBuilder bitsString = new StringBuilder();
        for (int charInt : bytesToSend) {
            bitsString.append(codes.get((char) charInt)); //code with huffman map
        }
        String toSend = fulfillString(bitsString.toString()); //add 0 at the end to make it % 8 = 0
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < toSend.length(); i += 8) {
            integers.add(bitsToInt(toSend.substring(i, i + 8))); //change bits strings to int
        }
        byte[] byteArray = new byte[integers.size()];
        for (int i = 0; i < integers.size(); i++) { //change int to byte
            int temp = integers.get(i);
            byteArray[i] = (byte) temp;
        }
        socketOutputStream.write(byteArray); //send compressed message
        socketOutputStream.write((byte) 0x04); //send EOT
        socketOutputStream.write((byte) (8 - (bitsString.length() % 8))); //send number of 0 added in last byte
    }

    /**
     * Fulfill bits string to 8 bits
     *
     * @param bitsString bits string to fulfill
     * @return 8 bits string
     */
    public String fulfillString(String bitsString) {
        StringBuilder sb = new StringBuilder();
        sb.append(bitsString);
        for (int i = 0; i < 8 - (bitsString.length() % 8); i++) {
            sb.append(0);
        }
        return sb.toString();
    }
}
