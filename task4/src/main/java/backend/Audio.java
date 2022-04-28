package backend;

import javax.sound.sampled.*;

public class Audio {

    /**
     * Define audio codec
     * @return audio format
     */
    public static AudioFormat getAudioFormat() {
        return new AudioFormat(48000f,
                16,    // sample size in bits
                2,     // mono
                true,  // signed
                true); // little endian
    }

    /**
     * Get data line for reading (microphone)
     * @param format audio format
     * @return data line
     * @throws LineUnavailableException exception
     */
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

    /**
     * Get data line for writing (speaker)
     * @param format audio format
     * @return data line
     * @throws LineUnavailableException exception
     */
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
