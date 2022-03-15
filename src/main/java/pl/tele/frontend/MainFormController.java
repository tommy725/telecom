package pl.tele.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import pl.tele.backend.Correction;
import pl.tele.backend.DoubleCorrection;
import pl.tele.backend.SingleCorrection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFormController {

    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Koduj/odkoduj";

    @FXML
    public TextArea originalForm;
    public TextArea codedForm;
    public Button save1;
    public Button save2;
    public ComboBox singleDoubleCorrectionCombobox;
    public Button encode;
    public Button decode;

    private byte[] originalData;
    private byte[] codedData;

    /**
     * Method reads plain byte array from given file
     *
     * @param actionEvent eventFromJavaFX
     * @throws InvocationTargetException exception
     * @throws NoSuchMethodException     exception
     * @throws IllegalAccessException    exception
     * @throws IOException               exception
     */
    public void readFromFile(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException,
            IllegalAccessException, IOException {
        String strPath = FileChoose.openChooser("Choose a file to encode", false, actionEvent);
        if (!strPath.equals("")) {
            Path p = Paths.get(strPath);
            originalData = Files.readAllBytes(p);
            clearTextFields();
            originalForm.setText(byteArrayToBitString(originalData));
        }
    }

    /**
     * Method saves plain byte array to file
     *
     * @param actionEvent eventFromJavaFX
     * @throws InvocationTargetException exception
     * @throws NoSuchMethodException     exception
     * @throws IllegalAccessException    exception
     * @throws IOException               exception
     */
    public void writeToFile(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException,
            IllegalAccessException, IOException {
        String strPath = FileChoose.saveChooser("Choose a file to encrypt", false, actionEvent);
        if (!strPath.equals("")) {
            Path p = Paths.get(strPath);
            Files.write(p, originalData);
        }
    }

    /**
     * Method reads encrypted byte array from given file
     *
     * @param actionEvent eventFromJavaFX
     * @throws InvocationTargetException exception
     * @throws NoSuchMethodException     exception
     * @throws IllegalAccessException    exception
     * @throws IOException               exception
     */
    public void readFromFileEncrypted(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException,
            IllegalAccessException, IOException {
        String strPath = FileChoose.openChooser("Choose an encrypted file to decrypt",
                false, actionEvent);
        if (!strPath.equals("")) {
            Path p = Paths.get(strPath);
            codedData = Files.readAllBytes(p);
            clearTextFields();
            codedForm.setText(byteArrayToBitString(codedData));
        }
    }

    /**
     * Method saves encrypted byte array to file
     *
     * @param actionEvent eventFromJavaFX
     * @throws InvocationTargetException exception
     * @throws NoSuchMethodException     exception
     * @throws IllegalAccessException    exception
     * @throws IOException               exception
     */
    public void writeToFileEncrypted(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException,
            IllegalAccessException, IOException {
        String strPath = FileChoose.saveChooser("Choose a file to encrypt", false, actionEvent);
        if (!strPath.equals("")) {
            Path p = Paths.get(strPath);
            Files.write(p, codedData);
        }
    }

    /**
     * Method to start encryption from GUI and return result on textField and encodeData
     */
    public void encode() {
        if (singleDoubleCorrectionCombobox.getValue() == null) {
            AlertBox.alertShow("Program error","Nie wybrano ilości błędów!", Alert.AlertType.ERROR);
            return;
        }
        if (singleDoubleCorrectionCombobox.getValue().equals("1 błąd")) {
            SingleCorrection sc = new SingleCorrection();
            codedForm.setText(startEncode(sc));
        }
        if (singleDoubleCorrectionCombobox.getValue().equals("2 błędy")) {
            DoubleCorrection dc = new DoubleCorrection();
            codedForm.setText(startEncode(dc));
        }
    }

    /**
     * Method to start decryption from GUI and return result on textField and to decodedData
     */
    public void decode() {
        if (singleDoubleCorrectionCombobox.getValue() == null) {
            AlertBox.alertShow("Program error","Nie wybrano ilości błędów", Alert.AlertType.ERROR);
            return;
        }
        if (singleDoubleCorrectionCombobox.getValue().equals("1 błąd")) {
            SingleCorrection sc = new SingleCorrection();
            originalForm.setText(sc.decode(codedForm.getText()));
        }
        if (singleDoubleCorrectionCombobox.getValue().equals("2 błędy")) {
            DoubleCorrection dc = new DoubleCorrection();
            originalForm.setText(dc.decode(codedForm.getText()));
        }
    }

    /**
     * Method converts byte array to string
     *
     * @param data byteArray
     * @return String
     */
    private String byteArrayToBitString(byte[] data) {
        StringBuilder buffer = new StringBuilder();
        for (byte b : data) {
            buffer.append(Integer.toBinaryString(b));
        }
        return buffer.toString();
    }

    /**
     * Methods clear textFields
     */
    private void clearTextFields() {
        originalForm.setText("");
        codedForm.setText("");
    }

    private String startEncode(Correction c) {
        for (int i = 0; i < originalForm.getText().length(); i++) {
            if (originalForm.getText().charAt(i) != '0' && originalForm.getText().charAt(i) != '1') {
                StringBuilder sb = new StringBuilder();
                byte[] bytes = originalForm.getText().getBytes();
                for (byte aByte : bytes) {
                    System.out.println(Integer.toBinaryString(aByte));
                    StringBuilder byteToBits = new StringBuilder();
                    if (Integer.toBinaryString(aByte).length() != 8) {
                        for (int j = Integer.toBinaryString(aByte).length(); j < 8; j++) {
                            byteToBits.append(0);
                        }
                        byteToBits.append(Integer.toBinaryString(aByte));
                    }
                    sb.append(c.encode(byteToBits.toString()));
                }
                return sb.toString();
            }
        }
        if (originalForm.getText().length() % 8 != 0) {
            AlertBox.alertShow("Program error","Liczba bitów musi być podzielna przez 8!", Alert.AlertType.ERROR);
            return "Error";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < originalForm.getText().length(); i += 8) {
            sb.append(c.encode(originalForm.getText().substring(i,i+8)));
        }
        return sb.toString();
    }
}