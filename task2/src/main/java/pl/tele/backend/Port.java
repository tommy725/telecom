package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;

public class Port implements AutoCloseable {
    public static final byte SOH = 0x01;
    public static final byte EOT = 0x04;
    public static final byte ACK = 0x06;
    public static final byte NAK = 0x15;
    public static final byte ETB = 0x17;
    public static final byte CAN = 0x18;
    public static final byte C = 0x43;

    private final SerialPort port;
    private boolean withCRC = false;
    private boolean connected = false;

    public Port(SerialPort port) {
        this.port = port;
        port.openPort();
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
    }

    public boolean isWithCRC() {
        return withCRC;
    }

    public void setWithCRC(boolean withCRC) {
        this.withCRC = withCRC;
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

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    /**
     * Count checksum (sum of 128 block bytes)
     * @param fileBytes received 133 bytes block
     * @param blockNumber number of block
     * @return 16 bits string value of sum
     */
    protected String countChecksum(byte[] fileBytes, int blockNumber) {
        int checkSum = 0;
        for (int j = 3; j < 131; j++) {
            checkSum += fileBytes[blockNumber * 128 + j - 3]; //Adding values of data bytes to checksum
        }
        String checkSumBits = Integer.toBinaryString(checkSum); //Changing checksum value to bitsString
        return fixTo16Bits(checkSumBits); //return 16 bits string
    }

    protected String countCRC(byte[] fileBytes) {
        short crc = 0;
        for (byte b : fileBytes) { //For every bytes
            crc ^= (b << 8);       //xor current crc with bytes moved 8 bits to the left
            for (int i = 0; i < 8; i++) { //8 times:
                if ((crc & 0x8000) != 0) { //Check if 16th bits is 1
                    crc = (short) ((crc << 1) ^ 0x1021); //If so, reduce it with reduce polynomial (0x1021)
                } else {
                    crc <<= 1;                           //if not, move crc 1 bit to left
                }
            }
        }
        String checkSumBits = Integer.toBinaryString(crc); //Changing crc value to bitsString
        return fixTo16Bits(checkSumBits); //return 16 bits string
    }

    /**
     * Fix bits string to 16 bits (2 bytes)
     * @param checkSumBits bitsString
     * @return bitsString unified to 16 bits
     */
    private String fixTo16Bits(String checkSumBits) {
        if (checkSumBits.length() < 16) {
            StringBuilder sb = new StringBuilder();
            for (int i = checkSumBits.length(); i < 16; i++) {
                sb.append(0);
            }
            sb.append(checkSumBits);
            return sb.toString();
        }
        if (checkSumBits.length() > 16) {
            return checkSumBits.substring(checkSumBits.length() - 16);
        }
        return checkSumBits;
    }

    /**
     * Change 8 bits string to integer number
     * @param bitsString 8 bits string
     * @return integer equal to 8 bits string
     */
    int bitsToInt(String bitsString) {
        int result = 0;
        String reversed = new StringBuilder(bitsString).reverse().toString();
        for (int i = 0; i < 8; i++) {
            String s = reversed.substring(i, i + 1);
            if (s.equals("1")) {
                int temp = 1;
                for (int j = 0; j < i; j++) {
                    temp *= 2;
                }
                result += temp;
            }
        }
        return result;
    }
}
