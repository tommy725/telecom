package frontend;

import backend.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static backend.HuffmanCoding.*;

public class Main {
    public static void main(String[] args) {
        char[] chars = {' ', '\n', 'a', 'i', 'o', 'e', 'z', 'n', 'r',
                'w', 's', 't', 'c', 'y', 'k', 'd',
                'p', 'm', 'u', 'j', 'l', 'ł', 'b',
                'g', 'ę', 'h', 'ą', 'ó', 'ż', 'ś',
                'ć', 'f', 'ń', 'q', 'ź', 'v', 'x'};
        int[] probabilities = {30, 5, 87, 80, 76, 75, 55, 54, 46,
                45, 42, 39, 38, 37, 34, 32,
                30, 27, 24, 22, 20, 17, 14,
                13, 10, 9, 9, 7, 7, 5,
                4, 3, 2, 1, 1, 1, 1};
        HuffmanNode root = HuffmanCoding.createTree(HuffmanCoding.inicializeNodes(chars, probabilities));
        Map<Character, String> codes = getCodes(root);

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
                try (ReceiverPort receiverPort = (ReceiverPort) PortManager.inicializePort(port, true)) { //inicialize port
                    receiverPort.setRoot(root);
                    System.out.println("Komunikacja rozpoczęta jako: " + no);
                    while (true) {
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            case "Nadajnik" -> { //if sender
                List<String> fileText = null;
                System.out.print("Podaj ścieżkę do pliku: "); //Import file to send
                Path path = Paths.get((new Scanner(System.in)).nextLine());
                if (!Files.exists(path)) {
                    System.out.println("Plik nie znaleziony!"); //If file not exists stop
                    return;
                }
                try (SenderPort senderPort = (SenderPort) PortManager.inicializePort(port, false)) { //inicialize port
                    fileText = Files.readAllLines(path); //read all bytes of the file
                    System.out.println("Komunikacja rozpoczęta jako: " + no);
                    printCode(root, "");
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < fileText.size(); i++) {
                        result.append(fileText.get(i));
                        if (i != fileText.size() - 1) {
                            result.append("\n");
                        }
                    }
                    senderPort.send(result.chars().toArray(),codes);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> System.out.println("Wybrano nieprawidłową opcję"); //incorrect option
        }
    }
}
