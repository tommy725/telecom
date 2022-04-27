//package frontend;
//
//import backend.PortManager;
//import backend.ReceiverPort;
//import com.fazecast.jSerialComm.SerialPort;
//import com.fazecast.jSerialComm.SerialPortDataListener;
//import com.fazecast.jSerialComm.SerialPortEvent;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class ReceiverSerialPortListener implements SerialPortDataListener {
//    List<Byte> buffer = new ArrayList<>();
//
//    @Override
//    public int getListeningEvents() {
//        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
//    }
//
//    /**
//     * Receiver port listener controlling transmission of the sound, dividing to blocks
//     *
//     * @param serialPortEvent port event
//     */
//    @Override
//    public void serialEvent(SerialPortEvent serialPortEvent) {
//        byte[] receivedData = serialPortEvent.getReceivedData();
//        ReceiverPort rp = (ReceiverPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
//        for (byte b : receivedData) {
//            if (buffer.size() > 1 && buffer.get(buffer.size() - 1) == 120 && b == -38) {
//                Byte[] array = new Byte[buffer.size() - 1];
//                rp.writeToSpeakerLine(buffer.toArray(array));
//                buffer.clear();
//                buffer.add((byte) 120);
//            }
//            buffer.add(b);
//        }
//    }
//}
