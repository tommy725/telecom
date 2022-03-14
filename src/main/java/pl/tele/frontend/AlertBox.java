package pl.tele.frontend;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertBox {
    /**
     * Method shows alert box with parameters.
     * @param title String
     * @param message String
     * @param alertType AlertType
     */
    public static void alertShow(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
