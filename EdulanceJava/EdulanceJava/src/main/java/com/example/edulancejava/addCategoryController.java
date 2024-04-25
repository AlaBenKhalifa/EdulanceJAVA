package com.example.edulancejava;

import com.example.edulancejava.Connectors.AddCategoryQuery;
import com.example.edulancejava.Connectors.Entities.Category;
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

public class addCategoryController {


    @FXML
    private Button savecategorybtn;

    @FXML
    private TextField categoryfield;

    @FXML
    void handlesave(ActionEvent event) {
        String category = categoryfield.getText();

        if (category.isEmpty()) {
            showAlert("Please enter a category.");
            return;
        }

        Category newCategory = new Category();
        newCategory.setTitre(category);

        AddCategoryQuery query = new AddCategoryQuery();
        query.addCategory(newCategory);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("category.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
