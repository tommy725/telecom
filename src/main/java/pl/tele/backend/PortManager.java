package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;

public class PortManager {
    private static final SerialPort[] ports = SerialPort.getCommPorts();
    private static String receiverName;
    private static Port receiver;
    private static String senderName;
    private static Port sender;

    public static String[] getPortsNameList() {
        String[] list = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            list[i] = i + 1 + ". " + ports[i].getSystemPortName();
        }
        return list;
    }

    public static Port inicializePort(int numberOnList, boolean receiver) throws Exception {
        if (numberOnList < 1 || numberOnList > ports.length) {
            throw new Exception("Wybrano nieprawidłową opcję");
        }
        if (receiver) {
            PortManager.receiver = new ReceiverPort(ports[numberOnList - 1]);
            PortManager.receiverName = ports[numberOnList - 1].getSystemPortName();
            return PortManager.receiver;
        } else {
            PortManager.sender = new SenderPort(ports[numberOnList - 1]);
            PortManager.senderName = ports[numberOnList - 1].getSystemPortName();
            return PortManager.sender;
        }
    }

    public static Port getPort(String portName) {
        if (receiverName != null && receiverName.equals(portName)) {
            return receiver;
        }
        if (senderName != null && senderName.equals(portName)) {
            return sender;
        }
        return null;
    }
}
