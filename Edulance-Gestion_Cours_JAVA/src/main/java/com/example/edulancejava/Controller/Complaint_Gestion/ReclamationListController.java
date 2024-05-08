package com.example.edulancejava.Controller.Complaint_Gestion;

import com.example.edulancejava.Connectors.DisplayQuery;
import com.example.edulancejava.Connectors.Entities.Complaint;
import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.animation.TranslateTransition;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class ReclamationListController implements Initializable {

    @FXML
    private ImageView side_bar;

    @FXML
    private VBox slider;

    @FXML
    private BorderPane container;
    @FXML
    private Button home_nav;
    @FXML
    private Button Course_front;

    @FXML
    private Button Complaint_front;
    @FXML
    private ComboBox<String> typeFilterComboBox;
    @FXML
    private ComboBox<String> statusFilterComboBox;
    @FXML
    private ListView<Complaint> complaintListView;
    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private DisplayQuery displayQuery;
    @FXML
    private Pagination pagination;
    @FXML
    private VBox rootVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayQuery = new DisplayQuery();

        List<Complaint> complaints = displayQuery.getAllComplaints();

        complaintListView.getItems().clear();

        complaintListView.setCellFactory(param -> new ComplaintListCell());

        complaintListView.getItems().addAll(complaints);


        // Add pagination to the root VBox
        home_nav.setOnMouseClicked(event -> {
            try {
                HBox page = FXMLLoader.load(getClass().getResource("/com/example/edulancejava/components/Welcome_page.fxml"));
                container.setCenter(page);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Course_front.setOnMouseClicked(event -> {
                    try {
                        container.setCenter(FXMLLoader.load(getClass().
                                getResource("/com/example/edulancejava/Course/Course.fxml")
                        ));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        Complaint_front.setOnMouseClicked(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/AddComplaint-view.fxml"));
            try {
                BorderPane page = fxmlLoader.load(); // Load as AnchorPane
                ComplaintController1 complaintController = fxmlLoader.getController();
                container.setCenter(page);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });




        //*/



        int width_side_bar=170;
        slider.setMinSize(0,0);
        slider.setPrefWidth(0);

        side_bar.setRotate((side_bar.getRotate()+180)%360);
        side_bar.setOnMouseClicked(event -> {
            if(slider.getPrefWidth()==0)
                slider.setPrefWidth(width_side_bar);
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4),slider);
            slide.setFromX(side_bar.getRotate()!=0?-slider.getPrefWidth():slider.getTranslateX());
            slide.setToX(side_bar.getRotate()==0 ? -slider.getPrefWidth() : 0);
            slide.play();
            slide.setOnFinished(event1 -> {
                side_bar.setRotate((side_bar.getRotate()+180)%360);
                if(side_bar.getRotate()!=0)
                    slider.setPrefWidth(0);
            });

        });

    }








    @FXML
    private void handleFilter() {
        String typeFilter = typeFilterComboBox.getValue();
        String statusFilter = statusFilterComboBox.getValue();

        // Create a filtered list based on your original complaint list
        FilteredList<Complaint> filteredData = new FilteredList<>(complaintListView.getItems());

        // Apply type filter
        if (typeFilter != null && !typeFilter.isEmpty()) {
            filteredData.setPredicate(complaint -> complaint.getType().toLowerCase().contains(typeFilter.toLowerCase()));
        }

        // Apply status filter
        if (statusFilter != null && !statusFilter.isEmpty()) {
            filteredData.setPredicate(complaint -> complaint.getStatus().equalsIgnoreCase(statusFilter));
        }

        // Wrap the filtered list in a sorted list
        SortedList<Complaint> sortedData = new SortedList<>(filteredData);

        // Bind the sorted list to your ListView
        complaintListView.setItems(sortedData);
    }


    private class ComplaintListCell extends ListCell<Complaint> {

        @Override
        protected void updateItem(Complaint item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                int index = getIndex();
                if (index % 2 == 0) { // Even index, show current complaint on the left
                    if (index + 1 < getListView().getItems().size()) { // Ensure there's a complaint available for the right card
                        Complaint leftItem = item; // Current complaint on the left
                        Complaint rightItem = getListView().getItems().get(index + 1); // Next complaint on the right

                        VBox leftCard = createComplaintCard(leftItem);
                        VBox rightCard = createComplaintCard(rightItem);

                        HBox twoColumnLayout = new HBox(leftCard, rightCard);
                        twoColumnLayout.setSpacing(20); // Adjust spacing between cards
                        setGraphic(twoColumnLayout);
                    } else { // No right card available
                        VBox leftCard = createComplaintCard(item);
                        setGraphic(leftCard);
                    }
                } else { // Odd index, don't show anything (handled by even index)
                    setGraphic(null);
                }
            }
        }


        // Method to create a complaint card
        private VBox createComplaintCard(Complaint item) {
            VBox card = new VBox();
            card.getStyleClass().add("complaint-card");

            // Create labels for each piece of information
            Label typeLabel = new Label("Type: " + item.getType());
            VBox.setMargin(typeLabel, new Insets(0, 0, 5, 0)); // Add bottom margin to create space

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            Label dateLabel = new Label("Date: " + currentDate); // Create
            VBox.setMargin(dateLabel, new Insets(0, 0, 20, 0)); // Add bottom margin to create space


            Label statusLabel = new Label("Status: " + item.getStatus());
            VBox.setMargin(statusLabel, new Insets(0, 0, 20, 0)); // Add bottom margin to create space
            String status = item.getStatus().toLowerCase();
            if (status.contains("progress")) {
                statusLabel.getStyleClass().add("status-label-progress");
            } else if (status.contains("resolved")) {
                statusLabel.getStyleClass().add("status-label-resolved");
            } else {
                statusLabel.getStyleClass().add("status-label-default");
            }


            // Create a description area with a titled border
            TitledPane descriptionPane = new TitledPane();
            descriptionPane.setText("Description");
            Label descriptionLabel = new Label(item.getDescription());
            descriptionPane.setContent(descriptionLabel);
            descriptionPane.setCollapsible(false);
            VBox.setMargin(descriptionPane, new Insets(0, 0, 20, 0)); // Add bottom margin to create space

            // Create a priority area with a titled border
            TitledPane priorityPane = new TitledPane();
            priorityPane.setText("Priority");
            priorityPane.setMaxWidth(30);

            Label priorityLabel = new Label();
            priorityLabel.getStyleClass().add("priority-label");
            priorityLabel.setMaxWidth(100);
            priorityLabel.setWrapText(true);
            setPriorityText(priorityLabel, item.getPriority()); // Set priority text based on priority level
            priorityPane.setContent(priorityLabel);
            priorityPane.setCollapsible(false);
            VBox.setMargin(priorityPane, new Insets(0, 0, 20, 0)); // Add bottom margin to create space
            // Add labels and titled panes to the card
            card.getChildren().addAll(typeLabel, dateLabel, statusLabel, descriptionPane, priorityPane);

            // Add styling to the status label
            statusLabel.getStyleClass().add("status-label");

            // Add image if available
            String imagePath = item.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageView imageView = new ImageView();
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                Image image = new Image(new File(imagePath).toURI().toString());
                imageView.setImage(image);
                card.getChildren().add(imageView);
            }

            // Add edit and delete buttons
            Button editButton = new Button("Edit");
            editButton.getStyleClass().add("edit-button");
            editButton.setOnAction(event -> handleEdit(item));

            Button deleteButton = new Button("Delete");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(event -> handleDelete(item));

            // Add response button if available
            String response = displayQuery.getComplaintResponse(item.getId_reclamation());
            if (response != null && !response.isEmpty()) {
                Button showResponseButton = new Button("Show Response");
                showResponseButton.setOnAction(event -> handleShowResponse(item));

                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.getChildren().addAll(editButton, deleteButton, showResponseButton);
                VBox.setMargin(buttonBox, new Insets(10, 0, 0, 0)); // Add top margin to create space

                card.getChildren().add(buttonBox);
            } else {
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.getChildren().addAll(editButton, deleteButton);

                VBox.setMargin(buttonBox, new Insets(10, 0, 0, 0)); // Add top margin to create space


                card.getChildren().add(buttonBox);
            }

            return card;
        }

        private void setPriorityText(Label priorityLabel, int priority) {
            priorityLabel.setText(""); // Clear previous content
            StringBuilder fireEmojis = new StringBuilder();
            for (int i = 0; i < priority; i++) {
                fireEmojis.append("🔥");
            }
            Text fireEmoji = new Text(fireEmojis.toString());
            fireEmoji.setFill(Color.RED); // Set the color of the fire emojis
            priorityLabel.setGraphic(fireEmoji); // Set the Text node containing fire emojis as the graphic
        }

        @FXML
        private void handleShowResponse(Complaint complaintToShowResponse) {
            if (complaintToShowResponse != null) {
                String response = displayQuery.getComplaintResponse(complaintToShowResponse.getId_reclamation());
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Complaint Response");
                dialog.setHeaderText("Response for Complaint ID: " + complaintToShowResponse.getId_reclamation());

                Label responseLabel = new Label(response);
                responseLabel.setWrapText(true);
                responseLabel.setAlignment(Pos.CENTER);

                responseLabel.setStyle("-fx-font-size: 14pt; -fx-text-fill: #336699;");

                dialog.getDialogPane().setStyle("-fx-background-color: #f0f0f0;");

                dialog.getDialogPane().setPrefWidth(400);
                dialog.getDialogPane().setPrefHeight(300);

                dialog.getDialogPane().setContent(responseLabel);

                ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().add(closeButton);

                ButtonType AcceptButton = new ButtonType("Accept", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().add(AcceptButton);
                dialog.getDialogPane().getButtonTypes().forEach(buttonType -> {
                    Button button = (Button) dialog.getDialogPane().lookupButton(buttonType);
                    button.setStyle("-fx-font-size: 14pt; -fx-background-color: #336699; -fx-text-fill: white;");
                });

                dialog.showAndWait();

                complaintToShowResponse.setSeenByUser(true);

                markResponseAsSeenByUser(complaintToShowResponse.getId_reclamation());

                complaintListView.refresh();


                // editButton.setVisible(!complaintToShowResponse.isSeenByUser());
                // deleteButton.setVisible(!complaintToShowResponse.isSeenByUser());

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Complaint Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a complaint to view its response.");
                alert.showAndWait();
            }
        }

        public void markResponseAsSeenByUser(int complaintId) {
            MySQLConnectors connection = new MySQLConnectors();

            String query = "UPDATE complaint SET seenByUser = ? WHERE id = ?";
            try (Connection conn = connection.getCnx();
                 PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setBoolean(1, true);
                statement.setInt(2, complaintId);

                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error marking response as seen by user: " + e.getMessage());
            }
        }


        @FXML
        private void handleDelete(Complaint complaintToDelete) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this complaint?");
            alert.setContentText("This action cannot be undone.");

            // Customize the appearance of the alert dialog
            DialogPane dialogPane = alert.getDialogPane();
            URL cssResource = getClass().getResource("style.css");
            if (cssResource != null) {
                dialogPane.getStylesheets().add(cssResource.toExternalForm());
            } else {
                System.err.println("CSS resource not found.");
                // Handle the situation where the CSS resource is not found
            }
            dialogPane.setStyle("-fx-background-color: #f0f0f0;"); // Example inline CSS

            alert.initStyle(StageStyle.UTILITY); // Optional: Set the stage style

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    complaintListView.getItems().remove(complaintToDelete);
                    displayQuery.deleteComplaint(complaintToDelete);
                    refreshComplaintListView();
                }
            });
        }

        private void refreshComplaintListView() {
            // Fetch the latest data from the database
            List<Complaint> complaints = displayQuery.getAllComplaints();

            complaintListView.getItems().clear();

            complaintListView.getItems().addAll(complaints);
        }

        @FXML
        private void handleEdit(Complaint selectedComplaint) {
            if (selectedComplaint != null) {
                long currentTime = System.currentTimeMillis();

                System.out.println("Formatted Current Date: " + currentTime);
                String submissionDateString = displayQuery.getSubmissionTime(selectedComplaint.getId_reclamation());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date submissionDate;
                try {
                    submissionDate = dateFormat.parse(submissionDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }

                long submissionTime = submissionDate.getTime();

                System.out.println("Submission Time: " + submissionTime);

                // Calculate the difference in minutes
                long differenceMinutes = (currentTime - submissionTime) / (1000 * 60);
                System.out.println("Difference in Minutes: " + differenceMinutes);

                if (differenceMinutes > 2) {
                    // Show an information alert if more than 2 minutes have passed since submission
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Edit Not Allowed");
                    alert.setHeaderText(null);
                    alert.setContentText("Editing this complaint is no longer allowed as more than 2 minutes have passed since submission.");

                    // Apply custom styling to the alert dialog
                    DialogPane dialogPane = alert.getDialogPane();
                  //  dialogPane.getStylesheets().add(getClass().getResource("C:\\Users\\MSI\\Desktop\\Edulance-Gestion_Cours_JAVA\\src\\main\\resources\\com\\example\\edulancejava\\Complaint\\style.css").toExternalForm()); // Load CSS
                    dialogPane.setStyle("-fx-background-color: #f0f0f0;"); // Example inline CSS

                    alert.showAndWait();
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/EditComplaint.fxml"));
                        Parent root = loader.load();

                        EditComplaintController editController = loader.getController();
                        editController.initData(selectedComplaint);

                        Stage stage = new Stage();
                        stage.setTitle("Edit Complaint");
                        stage.setScene(new Scene(root));
                        stage.getIcons().add(new Image("C:\\Users\\MSI\\IdeaProjects\\EdulanceJava\\src\\main\\resources\\com\\example\\icons\\icons8-triste-64.png"));

                        stage.showAndWait();

                        if (editController.isSaved()) {
                            displayQuery = new DisplayQuery();
                            List<Complaint> complaints = displayQuery.getAllComplaints();

                            complaintListView.getItems().clear();
                            complaintListView.getItems().addAll(complaints);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    }







