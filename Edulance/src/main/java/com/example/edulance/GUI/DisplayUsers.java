package com.example.edulance.GUI;

import com.example.edulance.Entities.NormalUser;
import com.example.edulance.Services.NormalUserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayUsers implements Initializable {

        @FXML
        private Button Button;
    @FXML
    private TableView<?> aff;
        @FXML
        private ListView<NormalUser> Table;
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            NormalUserController controller = new NormalUserController();

            List<NormalUser> list = controller.show();

            Table.getItems().clear();

            Table.setCellFactory(param -> new UsersListCell());

            Table.getItems().addAll(list);

        }

    private class UsersListCell extends ListCell<NormalUser> {
        @Override
        protected void updateItem(NormalUser item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox card = new VBox();

                Label nameLabel = new Label("Name: " + item.getName());
                Label familynameLabel = new Label("Family Name: " + item.getFamilyName());
                Label EmailLabel = new Label("Email: " + item.getEmail());
                Button editButton = new Button("edit");
                editButton.setOnAction(event -> handleEdit(item));
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                Button deleteButton = new Button("delete");
                deleteButton.setOnAction(event -> {
                    handleDelete(item);
                });
                deleteButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");
                card.getChildren().addAll(nameLabel,familynameLabel,EmailLabel);
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                buttonBox.getChildren().addAll(editButton, deleteButton);
                card.getChildren().add(buttonBox);
                setGraphic(card);
                /*Label descriptionLabel = new Label("Description: " + item.getDescription());
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


                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.getChildren().addAll(editButton, deleteButton);
                card.getChildren().add(buttonBox);

                setGraphic(card);*/
            }
        }

        @FXML
        private void handleEdit(NormalUser user) {
            if (user != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/EditUser.fxml"));
                    Parent root = loader.load();
                    EditUser edituser = loader.getController();
                    edituser.initData(user);
                    Stage stage = new Stage();
                    stage.setTitle("Edit User");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                    if (edituser.isSaved()) {
                        NormalUserController uc = new NormalUserController();
                        List<NormalUser> users = uc.show();

                        Table.getItems().clear();

                        Table.getItems().addAll(users);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No User Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a User to edit.");
                alert.showAndWait();

            }
        }

        @FXML
        private void handleDelete(NormalUser user) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this User?");
            alert.setContentText("Choose.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    Table.getItems().remove(user);
                    NormalUserController uc = new NormalUserController();
                    uc.delact(user);
                }
            });
        }

    }

}
