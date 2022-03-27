package pl.tele.frontend;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import pl.tele.backend.PortManager;
import pl.tele.backend.ReceiverPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static pl.tele.backend.Port.*;

public class ReceiverSerialPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    /**
     * Receiver port listener controlling transmission of the file
     * @param serialPortEvent port event
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        ReceiverPort rp = (ReceiverPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
        if (receivedData.length == 1 && receivedData[0] == EOT) { //if EOT is received
            System.out.println("OTRZYMANO PROSBE ZAKONCZENIA TRANSMISJI. WYSYLANIE ACK");
            rp.send(ACK); //accept end of transmission by sending ACK
            return;
        }
        if (receivedData.length == 1 && receivedData[0] == ETB) { //if ETB is received
            System.out.println("OTRZYMANO PROSBE ZAKONCZENIA POLACZENIA. WYSYLANIE ACK");
            rp.send(ACK); //accept end of connection by sending ACK
            System.out.println("ZAKONCZONO POLACZENIE.");

            System.out.print("Podaj ścieżkę do zapisu otrzymanego pliku: ");
            Path path = Paths.get((new Scanner(System.in)).nextLine()); //get path to save file
            try {
                Files.write(path, rp.getResultBytesWith0RemovedFromEnd()); //saving received bytes to file
            } catch (IOException e) {
                System.out.println("Nie mozna zapisać pliku"); //file save error
            }
            System.exit(0); //end program
        }
        if (!rp.isConnected()) {
            System.out.println("NAWIAZANO POLACZENIE Z NADAJNIKIEM"); //connection is established when received first data block
            rp.setConnected(true);
        }
        rp.addToReceivedBlock(receivedData); //adding received block of block fragment to received block bytes list
        if (rp.checkReceivedBlock()) { //check if checksum is correct
            rp.moveFromTempToFinalBytes(); //if check of checksum is correct we move temp received bytes to final result
            System.out.println("OTRZYMANO POPRAWNY BLOK DANYCH. WYSYLANIE ACK");
            rp.send(ACK); //send ACK as the packet is correct
        } else {
            System.out.println("OTRZYMANO NIEPOPRAWNY BLOK DANYCH. WYSYLANIE NACK"); //if check of checksum is incorrect
            rp.clearReceivedBlock();
            rp.send(NAK); //send NAK as the packet is incorrect
        }
    }
}
