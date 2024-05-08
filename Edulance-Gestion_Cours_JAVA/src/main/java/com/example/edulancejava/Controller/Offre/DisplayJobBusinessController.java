package com.example.edulancejava.Controller.Offre;

import com.example.edulancejava.Connectors.Entities.Offre;
import com.example.edulancejava.Connectors.GetJobsQuery;
import com.example.edulancejava.Connectors.MySQLConnectors;
import com.example.edulancejava.Connectors.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayJobBusinessController implements Initializable {
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

                        Button ApplyButton = new Button("Apply");
                        //ApplyButton.getStyleClass().add("edit-button");
                        ApplyButton.setOnAction(event -> handleApplyButton(offre));

                        HBox buttonContainer = new HBox(10, ApplyButton);
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


    public void addOffreglobaluser(int offreId, int userId) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "INSERT INTO offre_global_user (offre_id, global_user_id) VALUES (?, ?)";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, offreId); // Set the value of the first parameter (offre_id)
            statement.setInt(2, userId); // Set the value of the second parameter (global_user_id)
            statement.executeUpdate();

            System.out.println("Offre added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding offre: " + e.getMessage());
        }
    }


    @FXML
    void handleApplyButton(Offre selectedOffre) {
        if (selectedOffre != null) {
            int offreId = selectedOffre.getId();
            int userId = UserSession.getUser().getId();

            System.out.println("Offre ID: " + offreId);
            System.out.println("User ID: " + userId);
            addOffreglobaluser(offreId, userId);

             sendSMS();
        } else {
            System.out.println("No Offre selected");
        }
    }


    private void sendSMS() {
        com.twilio.Twilio.init("AC07750a961cb0daec3d886928fe7f5887", "ea1951b9544b3103fb41a5b4bfaabc89");

        // Convert the user's phone number to a String
        String strNumber = String.valueOf(UserSession.getUser().getPhoneNumber());

        com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message.creator(
                new com.twilio.type.PhoneNumber("+216" + strNumber),
                new com.twilio.type.PhoneNumber("+12176155863"),
                "Hello " + UserSession.getUser().getName() + ", You have been applied successfully to our company. BEST OF LUCK !"
        ).create();

        // You can add further logic here, such as handling success/failure of sending the message
    }


    private void loadJobs() {
            try {
                GetJobsQuery query = new GetJobsQuery();
                List<Offre> jobs;

                if (UserSession.getUser().getRoles() == 0) {
                    // If the user's role is 1, get all approved jobs for users with role 0
                    jobs = query.getAllApprovedJobsForRole1();
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