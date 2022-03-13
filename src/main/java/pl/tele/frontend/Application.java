package pl.tele.frontend;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static pl.tele.frontend.MainFormController.*;

/**
 * =========================================================
 * AES ENCRYPTOR/DECRYPTOR (2022)
 * Kamil Bednarek 236500, Radosław Bucki 236507
 * =========================================================
 */

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(MAIN_FORM_RESOURCE));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(MAIN_FORM_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}