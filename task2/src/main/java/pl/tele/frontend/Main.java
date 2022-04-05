package pl.tele.frontend;

import pl.tele.backend.PortManager;
import pl.tele.backend.ReceiverPort;
import pl.tele.backend.SenderPort;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String[] portsList = PortManager.getPortsNameList();
        System.out.println("Wybierz port:");
        for (String port : portsList) {
            System.out.println(port); //Display port list
        }
        System.out.print("Wybór: ");
        int port = (new Scanner(System.in)).nextInt(); //Choose port

        System.out.print("Nadajnik / Odbiornik: ");
        String no = (new Scanner(System.in)).nextLine(); //Choose if sender of receiver
        switch (no) {
            case "Odbiornik" -> { //if receiver
                System.out.print("Sum / CRC: "); //check checksum version (crc or checksums)
                String cs = (new Scanner(System.in)).nextLine();
                try (ReceiverPort receiverPort = (ReceiverPort) PortManager.inicializePort(port, true)) { //inicialize port
                    System.out.println("Komunikacja rozpoczęta jako: " + no);
                    switch (cs) {
                        case "Sum" -> receiverPort.initializeSumConnection(); //inicialize checksum version connection
                        case "CRC" -> receiverPort.initializeCRCConnection(); //inicialize crc version connection
                        default -> System.out.println("Wybrano nieprawidłową opcję");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            case "Nadajnik" -> { //if sender
                byte[] fileBytes = null;
                System.out.print("Podaj ścieżkę do pliku: "); //Import file to send
                Path path = Paths.get((new Scanner(System.in)).nextLine());
                if (!Files.exists(path)) {
                    System.out.println("Plik nie znaleziony!"); //If file not exists stop
                    return;
                }
                try (SenderPort senderPort = (SenderPort) PortManager.inicializePort(port, false)) { //inicialize port
                    fileBytes = Files.readAllBytes(path); //read all bytes of the file
                    senderPort.setFileBytes(fileBytes); //send bytes to sender port object
                    System.out.println("Komunikacja rozpoczęta jako: " + no);
                    while (true) {
                        (new Scanner(System.in)).nextLine();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> System.out.println("Wybrano nieprawidłową opcję"); //incorrect option
        }
    }
}
