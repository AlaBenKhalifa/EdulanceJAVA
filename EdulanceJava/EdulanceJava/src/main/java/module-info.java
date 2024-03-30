module com.example.edulancejava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.edulancejava to javafx.fxml;
    exports com.example.edulancejava;
}