package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;
import pl.tele.frontend.ReceiverSerialPortListener;

public class ReceiverPort extends Port {

    public ReceiverPort(SerialPort port) {
        super(port);
        port.addDataListener(new ReceiverSerialPortListener());
    }

    public void inicializeSumConnection() throws InterruptedException {
        for (int i = 0; i < 6; i++) {
            this.send(String.valueOf((char)0x15));
            Thread.sleep(10000);
        }
    }

    public void inicializeCRCConnection() throws InterruptedException {
        for (int i = 0; i < 6; i++) {
            this.send(String.valueOf((char)0x43));
            Thread.sleep(10000);
        }
    }
}
