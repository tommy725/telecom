package backend;

import com.fazecast.jSerialComm.SerialPort;
import frontend.ReceiverSerialPortListener;

import static backend.BitsToInt.bitsToInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReceiverPort extends Port {
    protected List<String> finalResult = new ArrayList<>();
    private HuffmanNode root;

    public ReceiverPort(SerialPort port) {
        super(port);
        port.addDataListener(new ReceiverSerialPortListener());
    }

    public void setRoot(HuffmanNode root) {
        this.root = root;
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
     * @param zeros number of 0 added in last but one byte
     */
    public void endTransmissionAndSave(int zeros) {
        String last = finalResult.remove(finalResult.size() - 1);
        finalResult.add(last.substring(0, 8 - zeros));
        StringBuilder finalSB = new StringBuilder();
        for (String s : finalResult) {
            finalSB.append(s);
        }
        System.out.println("Otrzymano " + finalResult.size() + " bajtów");
        String result = HuffmanCoding.decode(finalSB.toString().toCharArray(), root);
        System.out.print("Podaj ścieżkę do pliku: "); //Import file to send
        Path path = Paths.get((new Scanner(System.in)).nextLine());
        try {
            Files.writeString(path, result.replace("\n","\r\n"));
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku");
        }
    }
}
