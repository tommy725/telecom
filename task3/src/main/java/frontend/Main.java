package frontend;

import backend.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        System.out.print("Nadajnik / Odbiornik: ");
        String no = (new Scanner(System.in)).nextLine(); //Choose if sender of receiver
        switch (no) {
            case "Odbiornik" -> { //if receiver
                System.out.println("Komunikacja rozpoczęta jako: " + no);
                System.out.print("Podaj ścieżkę do pliku: "); //Import file to send
                ReceiverSocket rs = new ReceiverSocket(Paths.get((new Scanner(System.in)).nextLine()),root);
            }
            case "Nadajnik" -> { //if sender
                List<String> fileText = null;
                System.out.print("Podaj ścieżkę do pliku: "); //Import file to send
                Path path = Paths.get((new Scanner(System.in)).nextLine());
                if (!Files.exists(path)) {
                    System.out.println("Plik nie znaleziony!"); //If file not exists stop
                    return;
                }
                try {
                    fileText = Files.readAllLines(path); //read all bytes of the file
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Komunikacja rozpoczęta jako: " + no);
                ConsolePrinter.printCode(root, "");
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < fileText.size(); i++) {
                    result.append(fileText.get(i));
                    if (i != fileText.size() - 1) {
                        result.append("\n");
                    }
                }
                SenderSocket ss = new SenderSocket();
                try {
                    ss.send(result.chars().toArray(),codes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            default -> System.out.println("Wybrano nieprawidłową opcję"); //incorrect option
        }
    }
}
