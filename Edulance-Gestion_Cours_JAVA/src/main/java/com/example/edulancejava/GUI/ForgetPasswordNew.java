package com.example.edulancejava.GUI;


import com.example.edulancejava.Connectors.Entities.NormalUser;
import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ForgetPasswordNew implements Initializable {

    @FXML
    private TextField Password;

    @FXML
    private Label pwdv;

    private NormalUser user;

    @FXML
    private Label lblImagePath;

    @FXML
    private Button saveButton;

    private boolean saved = false;

    private String email;

    public void initData(String e){
        email = e;
        MySQLConnectors connection = new MySQLConnectors();
        user = new NormalUser();
        String query = "SELECT * FROM global_user where Email = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user.setName(resultSet.getString("Name"));
                user.setFamilyName(resultSet.getString("Family_Name"));
            }
        } catch (SQLException ee) {
            System.out.println("Error retrieving complaints: " + ee.getMessage());
        }
        System.out.println(user.getName());
        Password.textProperty().addListener((observable, oldValue, newValue) -> {
            // If password is empty, clear the status label
            if (newValue.isEmpty()) {
                pwdv.setText("");
            } else {
                // Otherwise, check the strength of the password and update the status label
                String strength = checkPasswordStrength(newValue,user.getName(),user.getFamilyName());
                pwdv.setText(strength);
                if (strength.equals("Weak"))
                    pwdv.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                if (strength.equals("Average"))
                    pwdv.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold;");
                if (strength.equals("Strong"))
                    pwdv.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            }
        });
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSave(ActionEvent event) {
        if(pwdv.getText().equals("Weak")){
            showAlert("Your Password is weak.");
            return;
        }
        if(pwdv.getText().equals("Average")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Registration");
            alert.setHeaderText("Your Password is Average. Do you want to register?");
            alert.setContentText("Choose.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    saved = true;
                }
            });
        }
        if(pwdv.getText().equals("Strong")){
            saved = true;
        }
        if(saved == true) {
            MySQLConnectors connection = new MySQLConnectors();
            String query = "UPDATE global_user "
                    + "SET  Password = ?"
                    + "WHERE Email = ?";
            try (Connection conn = connection.getCnx();
                 PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, Password.getText());
                statement.setString(2, email);
                statement.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Your password has been updated");
                alert.showAndWait();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                System.out.println("Error updating user: " + e.getMessage());
            }
        }
    }

    public static String checkPasswordStrength(String password,String Name,String FamilyName) {


        if (isAverage(password, Name,FamilyName)) {
            return "Average";
        }

        if (isWeak(password,Name,FamilyName)) {
            return "Weak";
        }

        if (isStrong(password,Name,FamilyName)) {
            return "Strong";
        }

        return "";
    }

    private static boolean isStrong(String password,String name,String familyName) {
        if (name != null && !name.isEmpty() && password.toLowerCase().contains(name.toLowerCase())) {
            return false;
        }
        if (familyName != null && !familyName.isEmpty() && password.toLowerCase().contains(familyName.toLowerCase())) {
            return false;
        }
        // Check if password contains all of these 4 (number and a special character and a majiscule and minuscule)
        if (password.matches(".*\\d.*") && password.matches(".*\\W.*") && password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) {
            return true;
        }
        return false;
    }

    private static boolean isWeak(String password,String name,String familyName) {
        for (int i = 0; i < password.length() - 2; i++) {
            if ((password.charAt(i) + 1 == password.charAt(i + 1))
                    && (password.charAt(i + 1) + 1 == password.charAt(i + 2))) {
                return true;
            }
        }
        if (!password.matches(".*\\d.*") || !password.matches(".*\\W.*") || !password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") || !password.matches(".*[A-Z].*")) {
            return true;
        }

        if (name != null && !name.isEmpty() && password.toLowerCase().contains(name.toLowerCase())) {
            return true;
        }
        if (familyName != null && !familyName.isEmpty() && password.toLowerCase().contains(familyName.toLowerCase())) {
            return true;
        }
        return false;
    }

    private static boolean isAverage(String password,String name,String familyName) {

        if (name != null && !name.isEmpty() && password.toLowerCase().contains(name.toLowerCase())) {
            return false;
        }
        if (familyName != null && !familyName.isEmpty() && password.toLowerCase().contains(familyName.toLowerCase())) {
            return false;
        }
        if(password.length() >=5){
            int count = 0;
            if (password.matches(".*\\d.*")) count++;
            if (password.matches(".*\\W.*")) count++;
            if (password.matches(".*[a-z].*")) count++;
            if (password.matches(".*[A-Z].*")) count++;
            return (count >= 2)&&(count < 4);
        }
        else
            return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
