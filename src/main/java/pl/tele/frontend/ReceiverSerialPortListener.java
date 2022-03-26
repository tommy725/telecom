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

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        ReceiverPort rp = (ReceiverPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
        if ((receivedData.length == 1) && (receivedData[0] == EOT)) {
            System.out.println("OTRZYMANO PROSBE ZAKONCZENIA POLACZENIA. WYSYLANIE ACK");
            rp.send(new byte[]{ACK});
            System.out.println("ZAKONCZONO POLACZENIE.");
            System.out.print("Podaj ścieżkę do zapisu otrzymanego pliku: ");
            Path path = Paths.get((new Scanner(System.in)).nextLine());
            try {
                Files.write(path, rp.getResultBytesWith0RemovedFromEnd());
            } catch (IOException e) {
                System.out.println("Nie mozna zapisać pliku");
                e.printStackTrace();
            }
            return;
        }
        if (!rp.isConnected()) {
            System.out.println("NAWIAZANO POLACZENIE Z NADAJNIKIEM");
            rp.setConnected(true);
        }
        rp.addToReceivedBlock(receivedData);
        try {
            if (rp.checkReceivedBlock()) {
                rp.moveFromTempToFinalBytes();
                System.out.println("OTRZYMANO POPRAWNY BLOK DANYCH. WYSYLANIE ACK");
                rp.send(new byte[]{ACK});
            } else {
                System.out.println("OTRZYMANO NIEPOPRAWNY BLOK DANYCH. WYSYLANIE NACK");
                rp.clearReceivedBlock();
                rp.send(new byte[]{NAK});
            }
        } catch (IllegalStateException ignored) {
        }
    }
}
