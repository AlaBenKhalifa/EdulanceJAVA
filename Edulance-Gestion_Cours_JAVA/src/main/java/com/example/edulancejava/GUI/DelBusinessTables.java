package com.example.edulancejava.GUI;


import com.example.edulancejava.Connectors.Entities.Business;

import com.example.edulancejava.Controller.User.BusinessController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DelBusinessTables implements Initializable {

    @FXML
    private Button Button;

    @FXML
    private ListView<Business> Table;

    @FXML
    private Button btn;

    @FXML
    private Button btn1;

    @FXML
    private ImageView btn1Profile;

    @FXML
    private Button btn2;

    @FXML
    private Button btn_workbench;

    @FXML
    private Button btn_workbench1;

    @FXML
    private Button btn_workbench11;

    @FXML
    private Button btn_workbench111;

    @FXML
    private Button btn_workbench112;

    @FXML
    private Button btn_workbench12;

    @FXML
    private Button btn_workbench13;

    @FXML
    private Button btn_workbench131;

    @FXML
    private Button btn_workbench1311;

    @FXML
    private Button btn_workbench2;

    @FXML
    private Button btn_workbench21;

    @FXML
    private Pane inner_pane;

    @FXML
    private Pane inner_pane1;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side;

    @FXML
    void switchdeluser(ActionEvent event) throws IOException {
        Stage stage = (Stage) side.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/DelUserTables.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);
        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void business(ActionEvent event) throws IOException {
        Stage stage = (Stage) side.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/AdminBusinessTables.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);
        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void switchuser(ActionEvent event) throws IOException {
        Stage stage = (Stage) side.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/AdminTables.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);
        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BusinessController controller = new BusinessController();

        List<Business> list = controller.showdel();

        Table.getItems().clear();

        Table.setCellFactory(param -> new UsersListCell());

        Table.getItems().addAll(list);

    }

    private class UsersListCell extends ListCell<Business> {
        @Override
        protected void updateItem(Business item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox card = new VBox();

                Label nameLabel = new Label("Name: " + item.getName());
                Label familynameLabel = new Label("Family Name: " + item.getFamilyName());
                Label EmailLabel = new Label("Email: " + item.getEmail());
                Button editButton = new Button("Activate");
                editButton.setOnAction(event -> handleActive(item));
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                Button deleteButton = new Button("Delete permanently");
                deleteButton.setOnAction(event -> {
                    handleDelete(item);
                });
                deleteButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");
                card.getChildren().addAll(nameLabel,familynameLabel,EmailLabel);
                String imagePath = item.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);

                    Image image = new Image(new File(imagePath).toURI().toString());
                    imageView.setImage(image);

                    card.getChildren().add(imageView);
                }
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                buttonBox.getChildren().addAll(editButton, deleteButton);
                card.getChildren().add(buttonBox);
                setGraphic(card);
            }
        }

        @FXML
        private void handleActive(Business user) {
            if (user != null) {
                Table.getItems().remove(user);
                BusinessController uc = new BusinessController();
                uc.activate(user);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No User Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a User to edit.");
                alert.showAndWait();

            }
        }

        @FXML
        private void handleDelete(Business user) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this User permanently?");
            alert.setContentText("Choose.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    Table.getItems().remove(user);
                    BusinessController uc = new BusinessController();
                    uc.delete(user);
                }
            });
        }

    }

    @FXML
    void admin(ActionEvent event) throws IOException {
        Stage stage = (Stage) side.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/AdminAdminTables.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);
        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void deladmin(ActionEvent event) throws IOException {
        Stage stage = (Stage) side.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/DelAdminTables.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);
        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }

}
