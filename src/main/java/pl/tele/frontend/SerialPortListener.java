package pl.tele.frontend;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialPortListener implements SerialPortDataListener {
    private static boolean messageReceived = false;

    public static boolean isMessageReceived() {
        return messageReceived;
    }

    public static void setMessageReceived(boolean messageReceived) {
        SerialPortListener.messageReceived = messageReceived;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        String message = new String(receivedData);
        System.out.println("    " + message);
        messageReceived = true;
    }
}
