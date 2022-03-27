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

    /**
     * Move bytes to inside of the class and fullfill to the n*128 number of bytes with 0 at the end
     * @param fileBytes file bytes
     */
    public void setFileBytes(byte[] fileBytes) {
        if (fileBytes.length % 128 == 0) {
            this.fileBytes = fileBytes; //If numOfBytes could be divided by 128 we dont do anything
        } else {
            int n128bytesBlocksNumber = ((fileBytes.length / 128) + 1) * 128; //If numOfBytes could not be divided by 128 we fulfill last block with 0s at the end
            byte[] elongatedFileBytes = new byte[n128bytesBlocksNumber];
            System.arraycopy(fileBytes, 0, elongatedFileBytes, 0, fileBytes.length);
            for (int i = fileBytes.length; i < n128bytesBlocksNumber; i++) {
                elongatedFileBytes[i] = 0;
            }
            this.fileBytes = elongatedFileBytes;
        }
        blockToSend = this.fileBytes.length / 128;
    }

    /**
     * Set sender port state to connection state with choose of the version
     * @param receivedData received init data (C or NAK)
     */
    public void initializeConnection(byte receivedData) {
        this.setConnected(true);
        switch (receivedData) {
            case NAK -> setWithCRC(false);
            case C -> setWithCRC(true);
        }
        blockNumber = 0;
    }

    /**
     * Get data Xmodem data block
     * @param moveToNext switch to next block?
     * @return xmodem frame
     */
    public byte[] getDataBlock(boolean moveToNext) {
        if (moveToNext) {
            blockNumber++; //if we want to go to next block we increment current blockNumber
        }
        byte[] data = new byte[133];
        data[0] = SOH;    //First byte is SOH
        data[1] = (byte) (blockNumber + 1); //Number of block
        data[2] = (byte) (255 - (blockNumber + 1)); //255 - block number
        for (int j = 3; j < 131; j++) {
            data[j] = fileBytes[blockNumber * 128 + j - 3]; //128 bytes of data (4-132)
        }
        String checkSumBits; //count check sum
        if (isWithCRC()) {   //check version
            byte[] block = new byte[128];
            for (int i = 0; i < 128; i++) {
                block[i] = fileBytes[128 * blockNumber + i];
            }
            checkSumBits = countCRC(block); //if crc version count it
        } else {
            checkSumBits = countChecksum(fileBytes, blockNumber); //if sum version count checksum
        }
        data[131] = (byte) bitsToInt(checkSumBits.substring(0, 8)); //first part of checksum
        data[132] = (byte) bitsToInt(checkSumBits.substring(8, 16)); //second part of checksum
        return data; //return 133 bytes xmodem fram
    }
}
