package com.example.edulancejava.Controller.Complaint_Gestion;

import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class DashboardApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    ///rakah interface zabzab ena zabzoubbbaaaa
    @Override
    public void start(Stage stage)throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/com/example/edulancejava/Complaint/Dashboard.fxml"));


        Scene scene = new Scene(root);

        stage.setTitle("Dashboard");

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);

        stage.setScene(scene);
        stage.show();

    }
}


