package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;
import pl.tele.frontend.ReceiverSerialPortListener;

public class ReceiverPort extends Port {

    public ReceiverPort(SerialPort port) {
        super(port);
        port.addDataListener(new ReceiverSerialPortListener());
    }

    public void inicializeSumConnection() throws InterruptedException {
        byte[] NAK = {0x15};
        for (int i = 0; i < 6; i++) {
            this.send(NAK);
            Thread.sleep(10000);
            if (isConnected()) {
                break;
            }
        }
    }

    public void inicializeCRCConnection() throws InterruptedException {
        byte[] C = {0x43};
        for (int i = 0; i < 6; i++) {
            this.send(C);
            Thread.sleep(10000);
            if (isConnected()) {
                break;
            }
        }
    }
}
