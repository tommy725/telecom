package backend;

import com.fazecast.jSerialComm.SerialPort;

import javax.sound.sampled.SourceDataLine;

public class PortManager {
    private static final SerialPort[] ports = SerialPort.getCommPorts();
    private static String receiverName;
    private static Port receiver;
    private static String senderName;
    private static Port sender;

    /**
     * Return String of port names installed on current machine.
     *
     * @return String list of ports
     */
    public static String[] getPortsNameList() {
        String[] list = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            list[i] = i + 1 + ". " + ports[i].getSystemPortName();
        }
        return list;
    }

    /**
     * Inicialize port and saves it to class values.
     *
     * @param numberOnList number of port
     * @param dataline     is source line
     * @return port object
     * @throws Exception invalid port choosen
     */
    public static Port inicializePort(int numberOnList, SourceDataLine dataline) throws Exception {
        if (numberOnList < 1 || numberOnList > ports.length) {
            throw new Exception("Wybrano nieprawidłową opcję");
        }
        if (dataline != null) {
            PortManager.receiver = new ReceiverPort(ports[numberOnList - 1], dataline);
            PortManager.receiverName = ports[numberOnList - 1].getSystemPortName();
            return PortManager.receiver;
        } else {
            PortManager.sender = new Port(ports[numberOnList - 1]);
            PortManager.senderName = ports[numberOnList - 1].getSystemPortName();
            return PortManager.sender;
        }
    }

    /**
     * Return port saved in class values (singleton) not inicializing new one if the port exists
     *
     * @param portName system port name
     * @return port object
     */
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
