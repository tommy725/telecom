module pl.tele {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;

    opens pl.tele.frontend to javafx.fxml;
    exports pl.tele.frontend;
}