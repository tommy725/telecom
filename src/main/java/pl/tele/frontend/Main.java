package pl.tele.frontend;

import pl.tele.backend.Port;
import pl.tele.backend.PortManager;

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
        try (Port port = PortManager.getPort(s.nextInt())) {
            System.out.println("Wybierz opcję:");
            System.out.println("1. Nadaję");
            System.out.println("2. Odbieram");
            System.out.print("Wybór: ");
            int choice = s.nextInt();
            while (true) {
                switch (choice) {
                    case 1 -> {
                        port.send((new Scanner(System.in)).nextLine());
                        choice = 2;
                    }
                    case 2 -> {
                        port.pickUp();
                        choice = 1;
                    }
                    default -> throw new Exception("Wybrano nieprawidłową opcję!");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
