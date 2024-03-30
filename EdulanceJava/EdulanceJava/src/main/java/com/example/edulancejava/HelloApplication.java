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
import java.util.Date;
import java.util.List;



public class HelloApplication extends Application {

    public void start(Stage stage) throws IOException, SQLException, ClassNotFoundException {


        MySQLConnectors connector = new MySQLConnectors();
        Connection connection = connector.getCnx();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Registration.fxml"));
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