package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;
import pl.tele.frontend.ReceiverSerialPortListener;
import pl.tele.frontend.SenderSerialPortListener;

public class SenderPort extends Port {
    byte[] fileBytes;

    public SenderPort(SerialPort port) {
        super(port);
        port.addDataListener(new SenderSerialPortListener());
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }
}
