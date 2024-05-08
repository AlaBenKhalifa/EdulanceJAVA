module com.course.edulance {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires unirest.java;
    requires gax;
    requires com.google.auth.oauth2;
   // requires proto.google.cloud.speech.v1;
    requires com.google.protobuf;
    requires google.cloud.speech;
    requires java.desktop;
    requires client.jar.sdk;
    requires twilio;


    opens com.example.edulancejava.Connectors.Entities to javafx.base;
            opens com.example.edulancejava to javafx.fxml;
            opens com.example.edulancejava.Controller.home to javafx.fxml;
            opens com.example.edulancejava.launcher to javafx.fxml;
            opens com.example.edulancejava.Controller.Complaint_Gestion to javafx.fxml;
            opens com.example.edulancejava.GUI to javafx.fxml;
    opens com.example.edulancejava.Controller.Offre;
            exports com.example.edulancejava;
            exports com.example.edulancejava.Controller.Offre;
            exports com.example.edulancejava.GUI;
            exports com.example.edulancejava.Controller.home;
            exports com.example.edulancejava.Controller.Course_Gestion;
            exports com.example.edulancejava.launcher;
            exports com.example.edulancejava.Controller.Complaint_Gestion;
            }
