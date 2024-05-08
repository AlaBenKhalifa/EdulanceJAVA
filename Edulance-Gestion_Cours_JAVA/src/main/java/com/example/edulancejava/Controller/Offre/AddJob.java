package com.example.edulancejava.Controller.Offre;


import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AddJob extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException, ClassNotFoundException{
        MySQLConnectors connector = new MySQLConnectors();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/addJob.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add job");
        // Show the stage
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}