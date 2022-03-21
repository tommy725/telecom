package pl.tele.frontend;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        String message = new String(receivedData);
        if (message.getBytes().length == 1 && message.getBytes()[0] == 0x15) {
            System.out.print("(NAK 0x15)");
        }
        System.out.println("    " + message);
    }
}
