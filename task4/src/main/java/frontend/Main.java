package frontend;

import backend.*;
import javax.sound.sampled.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("Nadajnik / Odbiornik: ");
        String no = (new Scanner(System.in)).nextLine(); //Choose if sender of receiver
        System.out.print("Częstotliwość próbkowania: ");
        float sampleRate  = (new Scanner(System.in)).nextFloat(); //Choose sample rate
        System.out.print("Liczba bitów poziomu kwantyzacji: ");
        int bitsInSample = (new Scanner(System.in)).nextInt(); //Choose bits num in sample
        AudioFormat audioFormat = Audio.getAudioFormat(sampleRate, bitsInSample); //Initialize audio format
        switch (no) {
            case "Odbiornik" -> { //if receiver
                try {
                    System.out.print("Podaj IP serwera: ");
                    String serverIp = new Scanner(System.in).nextLine();
                    SourceDataLine speakerLine = Audio.getTargetDataLineForPlay(audioFormat); //get speaker line
                    speakerLine.start(); //start speaker
                    ReceiverSocket rs = new ReceiverSocket(serverIp, speakerLine);
                    System.out.println("Rozpoczęto nasłuchiwanie portu.");
                    while (true) { //waiting for transmission
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            case "Nadajnik" -> { //if sender
                try {
                    System.out.println("Rozpoczęto transmisje mikrofonu.");
                    TargetDataLine microphoneLine = Audio.getTargetDataLineForRecord(audioFormat); //get microphone stream
                    microphoneLine.start(); //start microphone
                    byte[] data = new byte[microphoneLine.getBufferSize()];
                    SenderSocket ss = new SenderSocket();
                    while (true) {
                        microphoneLine.read(data, 0, data.length); //read data from microphone
                        ss.send(data);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> System.out.println("Wybrano nieprawidłową opcję"); //incorrect option
        }
    }


}
