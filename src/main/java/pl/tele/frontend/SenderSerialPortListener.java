package pl.tele.frontend;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import pl.tele.backend.PortManager;
import pl.tele.backend.SenderPort;

import java.util.Arrays;

public class SenderSerialPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        SenderPort sp = (SenderPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
        if (sp.getBlockNumber() + 1 == sp.getBlockToSend()) {
            if (!sp.isConnected() && receivedData.length == 1 && receivedData[0] == 0x06) {
                System.out.println("ZAKONCZONO POLACZENIE.");
                return;
            }
            if (sp.isEndingTransmision() && receivedData.length == 1 && (receivedData[0] == 0x06 || !sp.isConnected())) {
                System.out.println("ZAKONCZONO POLACZENIE. WYSYLANIE ETB");
                sp.setConnected(false);
                byte[] ETB = {0x17};
                sp.send(ETB);
                return;
            } else {
                System.out.println("ZAKONCZONO PRZESYLANIE PLIKU. WYSYLANIE EOT");
                sp.setEndingTransmision(true);
                byte[] EOT = {0x04};
                sp.send(EOT);
                return;
            }
        }
        if (sp.isConnected() && receivedData.length == 1 && receivedData[0] == 0x15) {
            System.out.println("OTRZYMANO NACK. WYSYLANIE PONOWNIE TEGO SAMEGO BLOKU DANYCH");
            sp.send(sp.getDataBlock(false));
        }
        if (sp.isConnected() && receivedData.length == 1 && receivedData[0] == 0x06) {
            System.out.println("OTRZYMANO ACK. WYSYLANIE KOLEJNEGO BLOKU DANYCH");
            sp.send(sp.getDataBlock(true));
        }
        if (receivedData.length == 1 && (receivedData[0] == 0x15 || receivedData[0] == 0x43) && !sp.isConnected()) {
            sp.inicializeConnection(receivedData[0]);
            switch (receivedData[0]) {
                case 0x15:
                    System.out.println("OTRZYMANO NACK (" + receivedData[0] + "). INICJACJA POLACZENIA Z SUMA KONTROLNA");
                    break;
                case 0x43:
                    System.out.println("OTRZYMANO C (" + receivedData[0] + "). INICJACJA POLACZENIA Z CRC");
                    break;
            }
            System.out.println("WYSYLANIE PIERWSZEGO BLOKU DANYCH");
            sp.send(sp.getDataBlock(false));
        }
    }


}
