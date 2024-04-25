package com.example.edulancejava;

import com.example.edulancejava.Connectors.DisplayQuery;
import com.example.edulancejava.Connectors.Entities.Complaint;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReclamationListController implements Initializable {

    @FXML
    private ListView<Complaint> complaintListView;

    private DisplayQuery displayQuery;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayQuery = new DisplayQuery();

        List<Complaint> complaints = displayQuery.getAllComplaints();

        complaintListView.getItems().clear();

        complaintListView.setCellFactory(param -> new ComplaintListCell());

        complaintListView.getItems().addAll(complaints);
    }

    private class ComplaintListCell extends ListCell<Complaint> {
        @Override
        protected void updateItem(Complaint item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox card = new VBox();

                Label typeLabel = new Label("Type: " + item.getType());
                Label descriptionLabel = new Label("Description: " + item.getDescription());
                Label priorityLabel = new Label("Priority: " + item.getPriority());
                Label dateLabel = new Label("Date: " + item.getDate());

                card.getChildren().addAll(typeLabel, descriptionLabel, priorityLabel, dateLabel);

                String imagePath = item.getImagePath();
                if (imagePath != null && !imagePath.isEmpty()) {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);

                    Image image = new Image(new File(imagePath).toURI().toString());
                    imageView.setImage(image);

                    card.getChildren().add(imageView);
                }
                Button editButton = new Button("edit");

                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

                editButton.setOnAction(event -> handleEdit(item));

                Button deleteButton = new Button("delete");

                deleteButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");

                deleteButton.setOnAction(event -> {
                    handleDelete(item);
                });
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.getChildren().addAll(editButton, deleteButton);
                card.getChildren().add(buttonBox);

                setGraphic(card);
            }
        }


    }

    @FXML
    private void handleDelete(Complaint complaintToDelete) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this complaint?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                complaintListView.getItems().remove(complaintToDelete);
                displayQuery.deleteComplaint(complaintToDelete);


            }
        });
    }

    @FXML
    private void handleEdit(Complaint selectedComplaint) {
        if (selectedComplaint != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditComplaint.fxml"));
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
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Complaint Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a complaint to edit.");
            alert.showAndWait();
        }
    }
}








