package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;
import pl.tele.frontend.ReceiverSerialPortListener;

import java.util.ArrayList;
import java.util.List;

public class ReceiverPort extends Port {
    protected List<Byte> receivedBlock = new ArrayList<>();
    protected List<Byte> finalBytes = new ArrayList<>();

    public ReceiverPort(SerialPort port) {
        super(port);
        port.addDataListener(new ReceiverSerialPortListener());
    }

    public void addToReceivedBlock(byte[] receivedBytes) {
        for (byte b : receivedBytes) {
            receivedBlock.add(b);
        }
    }

    public void clearReceivedBlock() {
        receivedBlock.clear();
    }

    public void moveFromTempToFinalBytes() {
        for (int i = 0; i < 128; i++) {
            finalBytes.add(receivedBlock.get(i + 3));
        }
        receivedBlock.clear();
    }

    public byte[] getResultBytesWith0RemovedFromEnd() {
        for (int i = finalBytes.size() - 1; i > 0; i--) {
            if (finalBytes.get(i) == 0) {
                finalBytes.remove(i);
            } else {
                break;
            }
        }
        byte[] finalByteArray = new byte[finalBytes.size()];
        for (int i = 0; i < finalBytes.size(); i++) {
            finalByteArray[i] = finalBytes.get(i);
        }
        return finalByteArray;
    }

    public boolean checkReceivedBlock() {
        if (receivedBlock.size() != 133) {
            throw new IllegalStateException();
        }
        byte[] receivedBytes = new byte[128];
        for (int i = 0; i < 128; i++) {
            receivedBytes[i] = receivedBlock.get(i + 3);
        }
        String checkSumBits;
        if (isWithCRC()) {
            checkSumBits = countCRC(receivedBytes);
        } else {
            checkSumBits = countChecksum(receivedBytes, 0);
        }
        byte byte131 = (byte) bitsToInt(checkSumBits.substring(0, 8));
        byte byte132 = (byte) bitsToInt(checkSumBits.substring(8, 16));
        return (receivedBlock.get(131) == byte131 && receivedBlock.get(132) == byte132);
    }

    public void initializeSumConnection() throws InterruptedException {
        setWithCRC(false);
        initializeConnection(NAK);
    }

    public void initializeCRCConnection() throws InterruptedException {
        setWithCRC(true);
        initializeConnection(C);
    }

    private void initializeConnection(byte code) throws InterruptedException {
        for (int i = 0; i < 6; i++) {
            this.send(code);
            Thread.sleep(10000);
            if (isConnected()) {
                break;
            }
        }
    }
}
