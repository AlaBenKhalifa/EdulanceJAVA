module com.example.edulance {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.net.http;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires java.desktop;
    requires java.mail;

    opens com.example.edulance to javafx.fxml;
    exports com.example.edulance;
    exports com.example.edulance.GUI;
    opens com.example.edulance.GUI to javafx.fxml;
    exports com.example.edulance.Tools;
    opens com.example.edulance.Tools to javafx.fxml;
}