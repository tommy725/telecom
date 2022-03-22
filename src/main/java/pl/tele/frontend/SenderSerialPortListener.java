package pl.tele.frontend;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import pl.tele.backend.PortManager;
import pl.tele.backend.ReceiverPort;
import pl.tele.backend.SenderPort;

import java.util.Objects;

public class SenderSerialPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        String message = new String(receivedData);
        SenderPort sp = (SenderPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
        if (message.getBytes().length == 1 && (receivedData[0] == 0x15 || receivedData[0] == 0x43) && !sp.isConnected()) {
            System.out.print("(" + receivedData[0] + ") Odbiornik inicuje połączenie");
            sp.setConnected(true);
            sp.send("TEST");
        }
        System.out.println("    " + message);
    }
}
