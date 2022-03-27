package pl.tele.frontend;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import pl.tele.backend.PortManager;
import pl.tele.backend.SenderPort;

import static pl.tele.backend.Port.*;

public class SenderSerialPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        SenderPort sp = (SenderPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
        if (sp.getBlockNumber() + 1 >= sp.getBlockToSend() && (sp.isConnected() || sp.isEndingTransmission())) {
            if (!sp.isConnected() && receivedData.length == 1 && receivedData[0] == ACK) {
                System.out.println("ZAKONCZONO POLACZENIE.");
                return;
            }
            if (sp.isEndingTransmission() && receivedData.length == 1 && (receivedData[0] == ACK || !sp.isConnected())) {
                System.out.println("ZAKONCZONO POLACZENIE. WYSYLANIE ETB");
                sp.setConnected(false);
                sp.send(ETB);
            } else {
                System.out.println("ZAKONCZONO PRZESYLANIE PLIKU. WYSYLANIE EOT");
                sp.endTransmission();
                sp.send(EOT);
            }
            return;
        }
        if (sp.isConnected() && receivedData.length == 1 && receivedData[0] == NAK) {
            System.out.println("OTRZYMANO NACK. WYSYLANIE PONOWNIE TEGO SAMEGO BLOKU DANYCH");
            sp.send(sp.getDataBlock(false));
        }
        if (sp.isConnected() && (receivedData.length == 1) && (receivedData[0] == ACK)) {
            System.out.println("OTRZYMANO ACK. WYSYLANIE KOLEJNEGO BLOKU DANYCH");
            sp.send(sp.getDataBlock(true));
        }
        if (!sp.isConnected() && receivedData.length == 1 && (receivedData[0] == NAK || receivedData[0] == C)) {
            sp.initializeConnection(receivedData[0]);
            switch (receivedData[0]) {
                case NAK -> System.out.println("OTRZYMANO NACK (" + receivedData[0] + "). INICJACJA POLACZENIA Z SUMA KONTROLNA");
                case C -> System.out.println("OTRZYMANO C (" + receivedData[0] + "). INICJACJA POLACZENIA Z CRC");
            }
            System.out.println("WYSYLANIE PIERWSZEGO BLOKU DANYCH");
            sp.send(sp.getDataBlock(false));
        }
    }
}
