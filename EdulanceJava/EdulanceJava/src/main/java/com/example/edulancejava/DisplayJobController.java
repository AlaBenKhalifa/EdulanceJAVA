package com.example.edulancejava;

import com.example.edulancejava.Connectors.DeleteJOBQuery;
import com.example.edulancejava.Connectors.Entities.Offre;
import com.example.edulancejava.Connectors.GetJobsQuery;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayJobController implements Initializable {

    @FXML
    private ListView<Offre> jobListView;

    @FXML
    private Pagination pagination;

    private final int itemsPerPage = 5;
    private ObservableList<Offre> jobList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadJobs();
        pagination.setPageFactory(this::createPage);


        jobListView.setItems(jobList);
        jobListView.setCellFactory(new Callback<ListView<Offre>, ListCell<Offre>>() {
            @Override
            public ListCell<Offre> call(ListView<Offre> offreListView) {
                return new ListCell<Offre>() {
                    @Override
                    protected void updateItem(Offre offre, boolean empty) {
                        super.updateItem(offre, empty);
                        if (offre != null && !empty) {
                            try {
                                String categoryTitle = new GetJobsQuery().getCategoryTitle(offre.getCategoryId());
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                String expirationDateFormatted = offre.getExpirationDate().format(formatter);
                                VBox vbox = new VBox();
                                vbox.setSpacing(5);
                                Label titleLabel = new Label(offre.getTitre());
                                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                                Label categoryLabel = new Label("Category: " + categoryTitle);
                                Label descriptionLabel = new Label(offre.getDescription());
                                descriptionLabel.setWrapText(true);
                                Label expirationDateLabel = new Label("Expiration Date: " + expirationDateFormatted);
                                Label salaryLabel = new Label("Salary: " + offre.getSalary());
                                Label languageLabel = new Label("Language: " + offre.getLanguage());
                                Label experienceLabel = new Label("Experience Level: " + offre.getExperienceLevel());
                                Label typeLabel = new Label("Type of Offer: " + offre.getTypeOffre());


                                Button editButton = new Button("Edit");
                                Button deleteButton = new Button("Delete");

                              editButton.setOnAction(event -> handleEditButton(offre));
                                deleteButton.setOnAction(event -> handleDeleteButton(offre));

                                vbox.getChildren().addAll(titleLabel, categoryLabel, descriptionLabel, salaryLabel, languageLabel, expirationDateLabel, experienceLabel, typeLabel, editButton, deleteButton);
                                setGraphic(vbox);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }
    private void handleDeleteButton(Offre offre) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete this item?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                DeleteJOBQuery deleteQuery = new DeleteJOBQuery();
                deleteQuery.deleteOffre(offre.getId());
                jobList.remove(offre);
                jobListView.setItems(jobList);

            }
        });
    }
    private void handleEditButton(Offre offre) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editJob.fxml"));
        Parent root;
        try {
            root = loader.load();
            EditJobController controller = loader.getController();
            controller.initData(offre);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            jobListView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, jobList.size());
        jobListView.setItems(FXCollections.observableArrayList(jobList.subList(fromIndex, toIndex)));
        VBox pageContent = new VBox();
        pageContent.getChildren().add(jobListView);
        return pageContent;
    }
}
