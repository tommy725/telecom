package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;
import pl.tele.frontend.SenderSerialPortListener;

public class SenderPort extends Port {
    byte[] fileBytes;
    boolean endingTransmision = false;
    int blockNumber = 0;
    int blockToSend = 0;

    public SenderPort(SerialPort port) {
        super(port);
        port.addDataListener(new SenderSerialPortListener());
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public int getBlockToSend() {
        return blockToSend;
    }

    public boolean isEndingTransmision() {
        return endingTransmision;
    }

    public void setEndingTransmision(boolean endingTransmision) {
        this.endingTransmision = endingTransmision;
    }

    public void setFileBytes(byte[] fileBytes) {
        if (fileBytes.length % 128 == 0) {
            this.fileBytes = fileBytes;
        } else {
            int n128bytesBlocksNumber = ((fileBytes.length / 128) + 1) * 128;
            byte[] enlongedFileBytes = new byte[n128bytesBlocksNumber];
            System.arraycopy(fileBytes, 0, enlongedFileBytes, 0, fileBytes.length);
            for (int i = fileBytes.length; i < n128bytesBlocksNumber; i++) {
                enlongedFileBytes[i] = 0;
            }
            this.fileBytes = enlongedFileBytes;
        }
        blockToSend = this.fileBytes.length / 128;
    }

    public void inicializeConnection(byte receivedData) {
        this.setConnected(true);
        if (receivedData == 0x15) {
            setWithCRC(false);
        }
        if (receivedData == 0x43) {
            setWithCRC(true);
        }
        blockNumber = 0;
    }

    public byte[] getDataBlock(boolean moveToNext) {
        if (moveToNext) {
            blockNumber++;
        }
        byte[] data = new byte[133];
        data[0] = 0x01;
        data[1] = (byte) (blockNumber + 1);
        data[2] = (byte) (255 - (blockNumber + 1));
        for (int j = 3; j < 131; j++) {
            data[j] = fileBytes[blockNumber * 128 + j - 3];
        }
        String checkSumBits;
        if (isWithCRC()) {
            byte[] block = new byte[128];
            for (int i = 0; i < 128; i++) {
                block[i] = fileBytes[128 * blockNumber + i];
            }
            checkSumBits = countCRC(block);
        } else {
            checkSumBits = countChecksum(fileBytes, blockNumber);
        }
        data[131] = (byte) bitsToInt(checkSumBits.substring(0, 8));
        data[132] = (byte) bitsToInt(checkSumBits.substring(8, 16));
        return data;
    }
}
