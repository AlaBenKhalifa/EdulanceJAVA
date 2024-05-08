package com.example.edulancejava.Controller.Offre;

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

public class EditCategoryController {

    @FXML
    private Button savecategorybtn;

    @FXML
    private TextField categoryfield;
    private Category categoryToUpdate;

    @FXML
    void handlesave(ActionEvent event) {
        String category = categoryfield.getText();
        categoryToUpdate.setTitre(category);
        CategoryController categoryController = new CategoryController();
        categoryController.updateCategory(categoryToUpdate);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/category.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initData(Category category) {
        this.categoryToUpdate = category;
        categoryfield.setText(category.getTitre());
    }
}
