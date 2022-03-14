package pl.tele.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import pl.tele.backend.DoubleCorrection;

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
        if (singleDoubleCorrectionCombobox.getValue().equals("1 błąd")) {

        }
        if (singleDoubleCorrectionCombobox.getValue().equals("2 błędy")) {
            DoubleCorrection dc = new DoubleCorrection();
            codedForm.setText(dc.encode(originalForm.getText()));
        }
    }

    /**
     * Method to start decryption from GUI and return result on textField and to decodedData
     */
    public void decode() {
        if (singleDoubleCorrectionCombobox.getValue().equals("1 błąd")) {
            SingleCorrection sc = new SingleCorrection();
            originalForm.setText(sc.encode(codedForm.getText()));
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
}