package frontend;

import backend.PortManager;
import backend.ReceiverPort;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class ReceiverSerialPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    /**
     * Receiver port listener controlling transmission of the file
     *
     * @param serialPortEvent port event
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        ReceiverPort rp = (ReceiverPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
        byte[] receivedData = serialPortEvent.getReceivedData();
        for (byte b : receivedData) {
            String bString = Integer.toBinaryString(b);
            if (bString.length() > 8) {
                bString = bString.substring(bString.length() - 8);
            }
            if (bString.length() < 8) {
                bString = "0".repeat(8 - bString.length()) + bString;
            }
            rp.addToReceivedBlock(bString);
        }
    }
}
