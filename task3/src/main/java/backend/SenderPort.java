package backend;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static backend.BitsToInt.bitsToInt;

public class SenderPort extends Port {
    public SenderPort(SerialPort port) {
        super(port);
    }

    /**
     * Code text usung huffman tree, change to bytes and send through COM port
     * @param toArray chars to send integer values
     * @param codes huffman code map
     */
    public void send(int[] toArray, Map<Character, String> codes) {
        StringBuilder bitsString = new StringBuilder();
        for (int charInt : toArray) {
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
            byteArray[i] = (byte)temp;
        }
        super.send(byteArray); //send compressed message
        super.send((byte) 0x04); //send EOT
        super.send((byte) (8 - (bitsString.length() % 8))); //send number of 0 added in last byte
    }

    /**
     * Fulfill bits string to 8 bits
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
