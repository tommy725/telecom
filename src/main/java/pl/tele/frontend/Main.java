package pl.tele.frontend;

import pl.tele.backend.ReceiverPort;
import pl.tele.backend.PortManager;
import pl.tele.backend.SenderPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        String[] portsList = PortManager.getPortsNameList();
        System.out.println("Wybierz port:");
        for (String port : portsList) {
            System.out.println(port);
        }
        System.out.print("Wybór: ");
        int port = (new Scanner(System.in)).nextInt();
        System.out.print("Nadajnik / Odbiornik: ");
        String no = (new Scanner(System.in)).nextLine();
        if (no.equals("Odbiornik")) {
            System.out.print("Sum / CRC: ");
            String cs = (new Scanner(System.in)).nextLine();
            try (ReceiverPort receiverPort = (ReceiverPort) PortManager.inicializePort(port,true)) {
                System.out.println("Komunikacja rozpoczęta jako: " + no);
                if (cs.equals("Sum")) {
                    receiverPort.inicializeSumConnection();
                }
                if (cs.equals("CRC")) {
                    receiverPort.inicializeCRCConnection();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (no.equals("Nadajnik")) {
            byte[] fileBytes = null;
            System.out.print("Podaj ścieżkę do pliku: ");
            Path path = Paths.get((new Scanner(System.in)).nextLine());
            if (Files.exists(path)) {
                try {
                    fileBytes = Files.readAllBytes(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Plik nie znaleziony!");
                return;
            }
            try (SenderPort senderPort = (SenderPort) PortManager.inicializePort(port,false)) {
                senderPort.setFileBytes(fileBytes);
                System.out.println("Komunikacja rozpoczęta jako: " + no);
                while(true) {
                    (new Scanner(System.in)).nextLine();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
