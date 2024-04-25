package com.example.edulancejava;

import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DisplayReclamationApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage primaryStage) throws IOException, SQLException, ClassNotFoundException{
        MySQLConnectors connector = new MySQLConnectors();
        Connection connection = connector.getCnx();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayComplaint.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add Complaint");
        primaryStage.show();
    }



}