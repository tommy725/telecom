package backend;

import javax.sound.sampled.*;

public class Audio {
    public static AudioFormat getAudioFormat() {
        return new AudioFormat(1500f,
                8,    // sample size in bits
                1,     // mono
                true,  // signed
                true); // little endian
    }

    public static TargetDataLine getTargetDataLineForRecord(AudioFormat format) throws LineUnavailableException {
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format, line.getBufferSize());
        return line;
    }

    public static SourceDataLine getTargetDataLineForPlay(AudioFormat format) throws LineUnavailableException {
        SourceDataLine line;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        }
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format, line.getBufferSize());
        return line;
    }

}
