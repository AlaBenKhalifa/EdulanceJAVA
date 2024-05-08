package com.example.edulancejava.Controller.Offre;

import com.example.edulancejava.Connectors.EditJobQuery;
import com.example.edulancejava.Connectors.Entities.Offre;
import com.example.edulancejava.Connectors.GetJobsQuery;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class EditJobController {
    private DisplayJobController displayJobController;

    public void setDisplayJobController(DisplayJobController displayJobController) {
        this.displayJobController = displayJobController;
    }
    @FXML
    private ComboBox<String> categoryField;
    @FXML
    private Button btnAddJob;

    @FXML
    private DatePicker datefield;

    @FXML
    private TextField descriptionfield;

    @FXML
    private ComboBox<String> experiencefield;

    @FXML
    private ComboBox<String> jobtypefield;

    @FXML
    private ComboBox<String> languagefield;

    @FXML
    private ComboBox<String> salaryfield;

    @FXML
    private TextField titlefield;
    private Offre offre; // Declare Offre object

    private Offre offreToEdit;

    public void initData(Offre offre) {
        this.offreToEdit = offre;
        populateCategoryComboBox();

        titlefield.setText(offre.getTitre());
        jobtypefield.setValue(String.valueOf(offre.getTypeOffre()));
        experiencefield.setValue(String.valueOf(offre.getExperienceLevel()));
        salaryfield.setValue(String.valueOf(offre.getSalary()));
        datefield.setValue(offre.getExpirationDate());
        descriptionfield.setText(offre.getDescription());
        languagefield.setValue(offre.getLanguage());
        String categoryTitle = getCategorytitre(offre.getCategoryId());
        if (categoryTitle != null) {
            categoryField.setValue(categoryTitle);
        }
    }
    private void populateCategoryComboBox() {
        try {
            GetJobsQuery query = new GetJobsQuery();
            List<String> categoryTitles = query.getAllCategoryTitles();
            categoryField.setItems(FXCollections.observableArrayList(categoryTitles));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String getCategorytitre(int categoryId) {
        try {
            GetJobsQuery query = new GetJobsQuery();
            return query.getCategoryTitle(categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    private int getCategoryId(String categoryTitle) {
        try {
            GetJobsQuery query = new GetJobsQuery();
            return query.getCategoryId(categoryTitle);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    @FXML
    private void handleSave(ActionEvent event) {
        saved = true;

        if (offreToEdit != null) {


            offreToEdit.setTitre(titlefield.getText());
            offreToEdit.setTypeOffre(jobtypefield.getValue());
            offreToEdit.setDescription(descriptionfield.getText());
            offreToEdit.setExperienceLevel(experiencefield.getValue());
            offreToEdit.setExpirationDate(datefield.getValue());
            offreToEdit.setSalary(salaryfield.getValue());
            offreToEdit.setLanguage(languagefield.getValue());
            String selectedCategory = categoryField.getValue();
            int categoryId = getCategoryId(selectedCategory);
            if (categoryId != -1) {
                offreToEdit.setCategoryId(categoryId);
            }
            EditJobQuery editJobQuery = new EditJobQuery();

            editJobQuery.updateOffre(offreToEdit);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();        }
    }



    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Create a custom label for the message with styling
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

        // Create a custom grid pane for layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(messageLabel, 0, 0);

        // Set the custom grid pane as the content of the alert dialog
        alert.getDialogPane().setContent(gridPane);

        // Show the alert dialog
        alert.showAndWait();
    }
    private boolean saved = false;

    // Other controller code

    public boolean isSaved() {
        return saved;
    }
    // Other controller code...

    public void setOffre(Offre offre) {
        this.offre = offre; // Set the Offre object
        // Use the Offre object to populate your edit fields, if needed
    }
}