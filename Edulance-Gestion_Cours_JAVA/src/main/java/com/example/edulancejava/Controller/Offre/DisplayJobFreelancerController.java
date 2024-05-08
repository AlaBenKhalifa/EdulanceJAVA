package com.example.edulancejava.Controller.Offre;

import com.example.edulancejava.Connectors.DeleteJOBQuery;
import com.example.edulancejava.Connectors.Entities.Offre;
import com.example.edulancejava.Connectors.GetJobsQuery;
import com.example.edulancejava.Connectors.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayJobFreelancerController implements Initializable {

    @FXML
    private ListView<Offre> jobListView;

    @FXML
    private Pagination pagination;

    private final int itemsPerPage = 11;
    private ObservableList<Offre> jobList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadJobs();
        pagination.setPageFactory(this::createPage);


        jobListView.setItems(jobList);
        jobListView.setCellFactory(param -> new ListCell<Offre>() {
            @Override
            protected void updateItem(Offre offre, boolean empty) {
                super.updateItem(offre, empty);
                if (empty || offre == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox cardContainer = new VBox();
                    cardContainer.setPadding(new Insets(10));
                    cardContainer.setSpacing(10);
                    cardContainer.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));

                    Label titleLabel = createLabel("Title: " + offre.getTitre(), "#333333", FontWeight.BOLD, 16);
                    Label categoryLabel = createLabel("Category: " + getCategoryTitle(offre.getCategoryId()), "#555555", FontWeight.NORMAL, 14);
                    Label descriptionLabel = createLabel("Description: " + offre.getDescription(), "#555555", FontWeight.NORMAL, 14);
                    descriptionLabel.setWrapText(true);
                    descriptionLabel.setMaxWidth(300); // Limit the width for better readability

                    Label expirationDateLabel;
                    if (offre.getExpirationDate() != null) {
                        expirationDateLabel = createLabel("Expiration Date: " + offre.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "#555555", FontWeight.NORMAL, 14);
                    } else {
                        expirationDateLabel = createLabel("Expiration Date: N/A", "#555555", FontWeight.NORMAL, 14);
                    }
                    Label salaryLabel = createLabel("Salary: " + offre.getSalary(), "#555555", FontWeight.NORMAL, 14);
                    Label languageLabel = createLabel("Language: " + offre.getLanguage(), "#555555", FontWeight.NORMAL, 14);
                    Label experienceLabel = createLabel("Experience Level: " + offre.getExperienceLevel(), "#555555", FontWeight.NORMAL, 14);
                    Label typeLabel = createLabel("Type of Offer: " + offre.getTypeOffre(), "#555555", FontWeight.NORMAL, 14);
//mech amwjoud fel fxml
                    Button ContactButton = createButton("Contact", "#4CAF50", event -> handleContactButton(offre));

                    HBox buttonContainer = new HBox(10, ContactButton);
                    buttonContainer.setAlignment(Pos.CENTER_RIGHT);

                    cardContainer.getChildren().addAll(titleLabel, categoryLabel, descriptionLabel, expirationDateLabel, salaryLabel, languageLabel, experienceLabel, typeLabel, buttonContainer);
                    setGraphic(cardContainer);
                }
            }
        });
    }
    private Label createLabel(String text, String color, FontWeight weight, double fontSize) {
        Label label = new Label(text);
        label.setTextFill(Color.web(color));
        label.setFont(Font.font("Arial", weight, fontSize));
        return label;
    }

    // Helper method to create a styled button
    private Button createButton(String text, String color, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        return button;
    }

    private String getCategoryTitle(int categoryId) {
        try {
            return new GetJobsQuery().getCategoryTitle(categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private void handleContactButton(Offre offre) {
       /** FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/editJob.fxml"));
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
        **/
    }


   // int userId = UserSession.getUser().getId();

    private void loadJobs() {
        try {
            GetJobsQuery query = new GetJobsQuery();
            List<Offre> jobs;

            if (UserSession.getUser().getRoles() == 1) {
                // If the user's role is 1, get all approved jobs for users with role 0
                jobs = query.getAllApprovedJobsForRoleZero();
                jobList.addAll(jobs);

            }
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