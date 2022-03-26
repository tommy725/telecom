package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;
import pl.tele.frontend.SenderSerialPortListener;

public class SenderPort extends Port {
    protected byte[] fileBytes;
    protected boolean endingTransmission = false;
    protected int blockNumber = 0;
    protected int blockToSend = 0;

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

    public boolean isEndingTransmission() {
        return endingTransmission;
    }

    public void endTransmission() {
        this.endingTransmission = true;
    }

    public void setFileBytes(byte[] fileBytes) {
        if (fileBytes.length % 128 == 0) {
            this.fileBytes = fileBytes;
        } else {
            int n128bytesBlocksNumber = ((fileBytes.length / 128) + 1) * 128;
            byte[] elongatedFileBytes = new byte[n128bytesBlocksNumber];
            System.arraycopy(fileBytes, 0, elongatedFileBytes, 0, fileBytes.length);
            for (int i = fileBytes.length; i < n128bytesBlocksNumber; i++) {
                elongatedFileBytes[i] = 0;
            }
            this.fileBytes = elongatedFileBytes;
        }
        blockToSend = this.fileBytes.length / 128;
    }

    public void initializeConnection(byte receivedData) {
        this.setConnected(true);
        switch (receivedData) {
            case NAK -> setWithCRC(false);
            case EOT -> setWithCRC(true);
        }
        blockNumber = 0;
    }

    public byte[] getDataBlock(boolean moveToNext) {
        if (moveToNext) {
            blockNumber++;
        }
        byte[] data = new byte[133];
        data[0] = SOH;
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
