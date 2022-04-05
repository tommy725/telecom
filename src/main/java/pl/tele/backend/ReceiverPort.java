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

    /**
     * Add byte array to Byte result list
     * @param receivedBytes byte array
     */
    public void addToReceivedBlock(byte[] receivedBytes) {
        for (byte b : receivedBytes) {
            receivedBlock.add(b);
        }
    }

    /**
     * Clear received list
     */
    public void clearReceivedBlock() {
        receivedBlock.clear();
    }

    /**
     * Move received list to finalBytes (result of transmission)
     */
    public void moveFromTempToFinalBytes() {
        for (int i = 0; i < 128; i++) {
            finalBytes.add(receivedBlock.get(i + 3));
        }
        receivedBlock.clear();
    }

    /**
     * Remove 0 from the end of the result list (they are added to fullfil 128 bytes in last block)
     * @return list with 0 removed from the end
     */
    public byte[] getResultBytesWith0RemovedFromEnd() {
        for (int i = finalBytes.size() - 1; i > 0; i--) {
            if (finalBytes.get(i) == 0) {
                finalBytes.remove(i);
            } else {
                finalBytes.remove(i);
                break;
            }
        }
        byte[] finalByteArray = new byte[finalBytes.size()];
        for (int i = 0; i < finalBytes.size(); i++) {
            finalByteArray[i] = finalBytes.get(i);
        }
        return finalByteArray;
    }

    /**
     * Check if received block sum/crc depending on version is valid and correct
     * @return result of the check
     */
    public boolean checkReceivedBlock() {
        if (receivedBlock.size() != 133) {
            throw new IllegalStateException();
        }
        byte[] receivedBytes = new byte[128];
        for (int i = 0; i < 128; i++) {
            receivedBytes[i] = receivedBlock.get(i + 3); //move data bytes to temp array
        }
        String checkSumBits;
        if (isWithCRC()) { //check the version
            checkSumBits = countCRC(receivedBytes); //if crc count crc
        } else {
            checkSumBits = countChecksum(receivedBytes, 0); //if sum count check sum
        }
        byte byte131 = (byte) bitsToInt(checkSumBits.substring(0, 8));
        byte byte132 = (byte) bitsToInt(checkSumBits.substring(8, 16));
        return (receivedBlock.get(131) == byte131 && receivedBlock.get(132) == byte132); //returns result of the test (test bytes equal or not)
    }

    /**
     * Set receiver port state to sum connection
     * @throws InterruptedException exception
     */
    public void initializeSumConnection() throws InterruptedException {
        setWithCRC(false);
        initializeConnection(NAK);
    }

    /**
     * Set receiver port state to crc connection
     * @throws InterruptedException exception
     */
    public void initializeCRCConnection() throws InterruptedException {
        setWithCRC(true);
        initializeConnection(C);
    }

    /**
     * Send beginning character (NAK or C) depending on the version of the algorithm (6 times every 10sec)
     * @param code character to send
     * @throws InterruptedException exception
     */
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
