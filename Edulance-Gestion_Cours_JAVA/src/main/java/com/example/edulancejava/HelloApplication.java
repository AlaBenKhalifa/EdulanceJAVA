package com.example.edulancejava;

import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MySQLConnectors connector = new MySQLConnectors();
        Connection connection = connector.getCnx();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/AddComplaint-view.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

/**
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
**/
        stage.setScene(scene);
        stage.setTitle("Edulance");

        // Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}