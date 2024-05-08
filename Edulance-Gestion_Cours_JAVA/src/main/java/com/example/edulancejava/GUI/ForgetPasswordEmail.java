package com.example.edulancejava.GUI;

import com.example.edulancejava.Connectors.Entities.NormalUser;
import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ForgetPasswordEmail {

    @FXML
    private TextField Email;

    @FXML
    private Label lblImagePath;

    @FXML
    private Button saveButton;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSave(ActionEvent event) {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<NormalUser> users = new ArrayList<>();
        String query = "SELECT COUNT(*) FROM global_user where email = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, Email.getText());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next(); // Move cursor to the first row
            int count = resultSet.getInt(1);

            // Close resources
            resultSet.close();
            statement.close();
            conn.close();
            if(count > 0){
                Stage stage = (Stage) Email.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/ForgetPasswordCode.fxml"));
                Parent root = loader.load();
                ForgetPasswordCode emailverification = loader.getController();
                emailverification.initData(generateRandomNumber(100000,999999),Email.getText());
                // Create a new scene with the loaded FXML
                Scene scene = new Scene(root);
                // Set the new scene to the stage
                stage.setScene(scene);
                stage.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("This Email Does Not Exist");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

}
