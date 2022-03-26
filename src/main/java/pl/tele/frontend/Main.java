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
            System.out.println(port);
        }
        System.out.print("Wybór: ");
        int port = (new Scanner(System.in)).nextInt();

        System.out.print("Nadajnik / Odbiornik: ");
        String no = (new Scanner(System.in)).nextLine();
        switch (no) {
            case "Odbiornik" -> {
                System.out.print("Sum / CRC: ");
                String cs = (new Scanner(System.in)).nextLine();
                try (ReceiverPort receiverPort = (ReceiverPort) PortManager.inicializePort(port, true)) {
                    System.out.println("Komunikacja rozpoczęta jako: " + no);
                    switch (cs) {
                        case "Sum" -> receiverPort.initializeSumConnection();
                        case "CRC" -> receiverPort.initializeCRCConnection();
                        default -> System.out.println("Wybrano nieprawidłową opcję");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            case "Nadajnik" -> {
                byte[] fileBytes;
                System.out.print("Podaj ścieżkę do pliku: ");
                Path path = Paths.get((new Scanner(System.in)).nextLine());
                if (!Files.exists(path)) {
                    System.out.println("Plik nie znaleziony!");
                    return;
                }
                try (SenderPort senderPort = (SenderPort) PortManager.inicializePort(port, false)) {
                    fileBytes = Files.readAllBytes(path);
                    senderPort.setFileBytes(fileBytes);
                    System.out.println("Komunikacja rozpoczęta jako: " + no);
                    while (true) {
                        (new Scanner(System.in)).nextLine();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> System.out.println("Wybrano nieprawidłową opcję");
        }
    }
}
