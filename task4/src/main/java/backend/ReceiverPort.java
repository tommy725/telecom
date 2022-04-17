package backend;

import com.fazecast.jSerialComm.SerialPort;
import frontend.ReceiverSerialPortListener;

import javax.sound.sampled.SourceDataLine;

public class ReceiverPort extends Port {
    private SourceDataLine speakerLine;

    public ReceiverPort(SerialPort port, SourceDataLine speakerLine) {
        super(port);
        this.speakerLine = speakerLine;
        port.addDataListener(new ReceiverSerialPortListener());
    }

    public void writeToSpeakerLine(Byte[] data) {
        try {
            byte[] bytes = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                bytes[i] = data[i];
            }
            byte[] decompressed = Compressor.decompress(bytes);
            speakerLine.write(decompressed, 0, decompressed.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
