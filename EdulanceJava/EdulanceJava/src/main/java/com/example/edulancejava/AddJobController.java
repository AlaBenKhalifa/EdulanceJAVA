package com.example.edulancejava;

import com.example.edulancejava.Connectors.AddJobQuery;
import com.example.edulancejava.Connectors.MySQLConnectors;
import com.example.edulancejava.Connectors.Entities.Offre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddJobController implements Initializable {
    @FXML
    private TextField titlefield;
    @FXML
    private DatePicker datefield;

    @FXML
    private TextField descriptionfield;

    @FXML
    private ComboBox<String> categoryfield;

    @FXML
    private ComboBox<String> experiencefield;

    @FXML
    private ComboBox<String> jobtypefield;

    @FXML
    private ComboBox<String> languagefield;

    @FXML
    private ComboBox<String> salaryfield;
    private ListView<Offre> jobListView;
    private ObservableList<Offre> offresList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateCategoryComboBox();


    }
    @FXML
    void AddJob(ActionEvent event) throws IOException {
        String title = titlefield.getText();
        String description = descriptionfield.getText();
        //metier bad words
        String[] motsInterdits = {"thanks", "fuck", "shit"};
        for (String motInterdit : motsInterdits) {
            String replacement = motInterdit.replaceAll(".", "*");
            description = description.replaceAll("(?i)\\b" + motInterdit + "\\b", replacement);
        }
        LocalDate expirationDate = datefield.getValue();
        if (expirationDate == null || expirationDate.isBefore(LocalDate.now())) {
            showAlert("Please select a future expiration date.");
            return;
        }
        String experienceLevel = experiencefield.getValue();
        String jobType = jobtypefield.getValue();
        String language = languagefield.getValue();
        String salary = salaryfield.getValue();

        String selectedCategoryTitle = categoryfield.getValue();

        if (title.isEmpty() || description.isEmpty() || experienceLevel == null || jobType == null || language == null || salary == null || expirationDate == null) {
            showAlert("Please fill out all required fields.");
            return;
        }

        for (Offre o : offresList) {
            if (o.getDescription().equals(description)) {
                showAlert("An Offer with the same description already exists.");
                return;
            }
        }

        int categoryId = getCategoryIdFromDatabase(selectedCategoryTitle);
        if (categoryId == -1) {
            showAlert("Selected category not found in the database.");
            return;
        }

        Offre offre = new Offre();
        offre.setTitre(title);
        offre.setDescription(description);
        offre.setExpirationDate(expirationDate);
        offre.setExperienceLevel(experienceLevel);
        offre.setTypeOffre(jobType);
        offre.setLanguage(language);
        offre.setSalary(salary);

        offre.setCategoryId(categoryId);
        offre.setCategorytitre(selectedCategoryTitle); // Set the category title

        AddJobQuery query = new AddJobQuery();
        query.addOffre(offre, categoryId, selectedCategoryTitle);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("displayJob.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        offresList.add(offre);

    }


    private int getCategoryIdFromDatabase(String titre) {
        MySQLConnectors connection = new MySQLConnectors();

        int categoryId = -1;
        String sql = "SELECT id FROM categorie WHERE titre = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, titre);

            System.out.println("SQL Query: " + sql);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    categoryId = resultSet.getInt("id");
                    // Debugging: Print retrieved category ID
                    System.out.println("Category ID: " + categoryId);
                }
            }
        } catch (SQLException e) {
            // Exception handling: Print exception message
            System.err.println("Error fetching category ID: " + e.getMessage());
            e.printStackTrace();
        }
        return categoryId;
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void populateCategoryComboBox() {
        categoryfield.getItems().clear();
        MySQLConnectors mySQLConnectors = new MySQLConnectors();
        String query = "SELECT titre FROM categorie";
        try (Connection conn = mySQLConnectors.getCnx();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String categoryTitle = resultSet.getString("titre");
                categoryfield.getItems().add(categoryTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void switchToAnotherViewListJob(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayJob.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
