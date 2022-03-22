package pl.tele.frontend;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import pl.tele.backend.PortManager;
import pl.tele.backend.ReceiverPort;
import pl.tele.backend.SenderPort;

import java.util.Arrays;

public class ReceiverSerialPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        ReceiverPort rp = (ReceiverPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
        if (!rp.isConnected()) {
            System.out.println("Nazwiązano połączenie");
            rp.setConnected(true);
        }
        System.out.println(Arrays.toString(receivedData));
    }
}
