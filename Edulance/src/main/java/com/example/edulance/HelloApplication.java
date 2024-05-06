package com.example.edulance;

import com.example.edulance.Tools.MySQLConnectors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MySQLConnectors connector = new MySQLConnectors();
        Connection connection = connector.getCnx();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UI/ForgetPasswordEmail.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Edulance");

        // Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}