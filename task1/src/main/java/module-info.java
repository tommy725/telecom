module pl.tele {
    requires javafx.controls;
    requires javafx.fxml;

    opens pl.tele.frontend to javafx.fxml;
    exports pl.tele.frontend;
}