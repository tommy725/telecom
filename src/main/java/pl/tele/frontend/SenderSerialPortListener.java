package pl.tele.frontend;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import pl.tele.backend.PortManager;
import pl.tele.backend.SenderPort;

public class SenderSerialPortListener implements SerialPortDataListener {
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] receivedData = serialPortEvent.getReceivedData();
        String message = new String(receivedData);
        SenderPort sp = (SenderPort) PortManager.getPort(serialPortEvent.getSerialPort().getSystemPortName());
        if (message.getBytes().length == 1 && (receivedData[0] == 0x15 || receivedData[0] == 0x43) && !sp.isConnected()) {
            System.out.print("(" + receivedData[0] + ") Odbiornik inicuje połączenie");
            sp.setConnected(true);
            byte[] data = getDataBlock(sp,0);
            sp.send(data);
        }
//        for (int i = 128; i < sp.getFileBytes().length; i += 128) {
//            sendDataBlock(sp, i/128);
//        }
    }

    private byte[] getDataBlock(SenderPort sp, int blockNumber) {
        byte[] data = new byte[133];
        data[0] = 0x01;
        data[1] = (byte) (blockNumber + 1);
        data[2] = (byte) (255 - (blockNumber + 1));
        int checkSum = 0;
        for (int j = 3; j < 131; j++) {
            data[j] = sp.getFileBytes()[blockNumber * 128 + j - 3];
            checkSum += data[j];
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
        data[131] = (byte) bitsToInt(checkSumBits.substring(0, 8));
        data[132] = (byte) bitsToInt(checkSumBits.substring(8, 16));
        return data;
    }

    private int bitsToInt(String bitsString) {
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
