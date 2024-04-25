package com.example.edulancejava;

import com.example.edulancejava.Connectors.DeleteJOBQuery;
import com.example.edulancejava.Connectors.Entities.Offre;
import com.example.edulancejava.Connectors.GetJobsQuery;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayJobAdminController implements Initializable {
    private Offre offre; // Declare Offre object

    // Other controller code...

    public void setOffre(Offre offre) {
        this.offre = offre; // Set the Offre object
        // Use the Offre object to populate your edit fields, if needed
    }
    @FXML
    private TableColumn<Offre, Button> editColumn;

    @FXML
    private TableColumn<Offre, Button> deleteColumn;

    @FXML
    private TableView<Offre> jobTableView;

    @FXML
    private TableColumn<Offre, String> titleColumn;

    @FXML
    private TableColumn<Offre, String> typeColumn;

    @FXML
    private TableColumn<Offre, String> experienceColumn;

    @FXML
    private TableColumn<Offre, String> salaryColumn;

    @FXML
    private TableColumn<Offre, String> expirationColumn;

    @FXML
    private TableColumn<Offre, String> languageColumn;

    @FXML
    private TableColumn<Offre, String> descriptionColumn;

    @FXML
    private TableColumn<Offre, String> categorytitre; // Add category column

    private final ObservableList<Offre> jobList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        loadJobs(); // Call loadJobs() method to populate the table with data
    }

    private void initTable() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeOffre()));
        experienceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExperienceLevel()));
        salaryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSalary()));
        expirationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpirationDate().toString()));
        languageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLanguage()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        categorytitre.setCellValueFactory(cellData -> {
            Offre offre = cellData.getValue();
            if (offre != null && offre.getCategorie() != null) {
                return new SimpleStringProperty(offre.getCategorie());
            } else {
                return new SimpleStringProperty(""); // Or any default value you want to display
            }
        });




        jobTableView.setItems(jobList);

        editColumn.setCellValueFactory(cellData -> {
            Button editButton = new Button("Edit");
            editButton.setOnAction(event -> handleEditButtonAction(event, cellData.getValue()));
            return new SimpleObjectProperty<>(editButton);
        });
        deleteColumn.setCellValueFactory(cellData -> {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> handleDeleteButtonAction(event, cellData.getValue()));
            return new SimpleObjectProperty<>(deleteButton);
        });
    }

    private void loadJobs() {
        try {
            GetJobsQuery query = new GetJobsQuery();
            List<Offre> jobs = query.getJobs();
            jobList.addAll(jobs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditButtonAction(ActionEvent event, Offre selectedJob) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditJob.fxml"));
        try {
            Parent root = loader.load();
            EditJobController editJobController = loader.getController();
            editJobController.initData(selectedJob);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDeleteButtonAction(ActionEvent event, Offre selectedJob) {
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = selectedJob.getExpirationDate();

        if (currentDate.isAfter(expirationDate)) {
            DeleteJOBQuery deleteJobQuery = new DeleteJOBQuery();
            deleteJobQuery.deleteOffre(selectedJob.getId());
            jobList.remove(selectedJob);
            System.out.println("Offre deleted successfully!");
        }  else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Delete Offre");
            alert.setHeaderText(null);
            alert.setContentText("Cannot delete offre. Expiration date not reached.");
            alert.showAndWait();
        }
    }
}
