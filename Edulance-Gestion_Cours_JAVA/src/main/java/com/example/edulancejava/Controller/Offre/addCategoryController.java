package com.example.edulancejava.Controller.Offre;

import com.example.edulancejava.Connectors.AddCategoryQuery;
import com.example.edulancejava.Connectors.Entities.Category;
import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addCategoryController {


    @FXML
    private Button savecategorybtn;

    @FXML
    private TextField categoryfield;

    @FXML
    void handlesave(ActionEvent event) {
        // Retrieve the entered category title
        String categoryTitle = categoryfield.getText();

        if (categoryTitle.isEmpty()) {
            showAlert("Please enter a category title.");
            return;
        }

        if (categoryTitleExists(categoryTitle)) {
            showAlert("A category with the same title already exists.");
            return;
        }

        Category newCategory = new Category();
        newCategory.setTitre(categoryTitle);

        AddCategoryQuery query = new AddCategoryQuery();
        query.addCategory(newCategory);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/category.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAlert("Category Added successfully");
    }

    private boolean categoryTitleExists(String categoryTitle) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "SELECT COUNT(*) FROM categorie WHERE titre = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, categoryTitle);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking category title uniqueness: " + e.getMessage());
        }
        return false;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
