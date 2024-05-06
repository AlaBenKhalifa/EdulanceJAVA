package com.example.edulance.GUI;

import com.example.edulance.Entities.GlobalUser;
import com.example.edulance.Entities.NormalUser;
import com.example.edulance.Tools.MySQLConnectors;
import com.example.edulance.Tools.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController {

    @FXML
    private TextField Email;

    @FXML
    private PasswordField Password;

    private GlobalUser user = new GlobalUser();

    @FXML
    void Login(ActionEvent event) {
        String email = Email.getText();
        String password = Password.getText();
        if(EmailValidator(email)) {
            if (authenticate(email, password)) {

                UserSession.setUser(user);
                try {
                    Stage stage = (Stage) Email.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/AdminTables.fxml"));
                    Parent root = loader.load();

                    // Create a new scene with the loaded FXML
                    Scene scene = new Scene(root);
                    // Set the new scene to the stage
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Password is incorrect");
            }
        }
        else
        {
            showAlert("Email does not exist");
        }
    }

    private boolean authenticate(String email, String password) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "SELECT * FROM global_user where Email = ? and Password = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user.setName(resultSet.getString("Name"));
                user.setFamilyName(resultSet.getString("Family_Name"));
                user.setPhoneNumber(resultSet.getInt("Phone_Number"));
                user.setEmail(resultSet.getString("Email"));
                user.setPassword(resultSet.getString("Password"));
                user.setLanguage(resultSet.getString("Language"));
                user.setDescription(resultSet.getString("Description"));
                user.setId(resultSet.getInt("Id"));
                user.setNationality(resultSet.getString("Nationality"));
                user.setImage(resultSet.getString("Image"));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
            return false;
        }
        return false;
    }

    public boolean EmailValidator(String Email) {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<NormalUser> users = new ArrayList<>();
        String query = "SELECT COUNT(*) FROM global_user where email = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, Email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next(); // Move cursor to the first row
            int count = resultSet.getInt(1);

            // Close resources
            resultSet.close();
            statement.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
            return false;
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
