module example.calcolatrice {
    requires javafx.controls;
    requires javafx.fxml;


    opens example.calcolatrice to javafx.fxml;
    exports example.calcolatrice;
    exports example.calcolatrice.controller;
    opens example.calcolatrice.controller to javafx.fxml;
}