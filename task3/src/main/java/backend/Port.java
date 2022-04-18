package backend;

import com.fazecast.jSerialComm.SerialPort;

public class Port implements AutoCloseable {
    private final SerialPort port;

    public Port(SerialPort port) {
        this.port = port;
        port.openPort();
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
    }

    public void close() {
        port.closePort();
    }

    public void send(byte[] bytes) {
        port.writeBytes(bytes, bytes.length);
    }

    public void send(byte code) {
        send(new byte[]{code});
    }
}
