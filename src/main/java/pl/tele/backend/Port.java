package pl.tele.backend;

import com.fazecast.jSerialComm.SerialPort;


public class Port implements AutoCloseable{
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

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public String countChecksum(byte[] fileBytes, int blockNumber) {
        int checkSum = 0;
        for (int j = 3; j < 131; j++) {
            checkSum += fileBytes[blockNumber * 128 + j - 3];
        }
        String checkSumBits = Integer.toBinaryString(checkSum);
        if (checkSumBits.length() < 16) {
            StringBuilder sb = new StringBuilder();
            for (int i = checkSumBits.length(); i < 16; i++) {
                sb.append(0);
            }
            sb.append(checkSumBits);
            checkSumBits = sb.toString();
        }
        if (checkSumBits.length() > 16) {
            checkSumBits = checkSumBits.substring(checkSumBits.length() - 16);
        }
        return checkSumBits;
    }

    public String countCRC(byte[] fileBytes, int blockNumber) {
        int checkSum = 0;
        for (int j = 3; j < 131; j++) {
            checkSum += fileBytes[blockNumber * 128 + j - 3];
        }
        String checkSumBits = Integer.toBinaryString(checkSum);
        if (checkSumBits.length() < 16) {
            StringBuilder sb = new StringBuilder();
            for (int i = checkSumBits.length(); i < 16; i++) {
                sb.append(0);
            }
            sb.append(checkSumBits);
            checkSumBits = sb.toString();
        }
        if (checkSumBits.length() > 16) {
            checkSumBits = checkSumBits.substring(checkSumBits.length() - 16);
        }
        return checkSumBits;
    }

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
