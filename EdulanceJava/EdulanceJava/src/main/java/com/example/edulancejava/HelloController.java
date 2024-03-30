package com.example.edulancejava;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class HelloController {

    @FXML
    private Label welcomeText;
    @FXML
    private Button Button; // Adjust variable name if needed

    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic goes here
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private void switchToAnotherView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddComplaint-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) Button.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
