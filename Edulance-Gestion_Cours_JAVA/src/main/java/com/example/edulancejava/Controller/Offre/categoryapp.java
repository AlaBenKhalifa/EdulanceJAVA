package com.example.edulancejava.Controller.Offre;

import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class categoryapp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        MySQLConnectors connector = new MySQLConnectors();
        Connection connection = connector.getCnx();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/category.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard");
        primaryStage.show();
    }
}
