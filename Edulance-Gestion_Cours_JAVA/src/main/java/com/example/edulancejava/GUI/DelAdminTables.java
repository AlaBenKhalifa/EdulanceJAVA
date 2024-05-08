package com.example.edulancejava.GUI;


import com.example.edulancejava.Connectors.Entities.Admin;
import com.example.edulancejava.Controller.User.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

public class DelAdminTables implements Initializable {

    @FXML
    private Button Button;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Admin> Table;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AdminController controller = new AdminController();

        List<Admin> list = controller.showdel();

        Table.getItems().clear();

        Table.setCellFactory(param -> new UsersListCell());

        ObservableList<Admin> dataList = FXCollections.observableArrayList(list);
        FilteredList<Admin> filteredList = new FilteredList<>(dataList, p -> true);

        Table.setItems(filteredList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Check if any property value matches the search text
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name
                } else if (user.getFamilyName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches family name
                }
                // Add more conditions if needed for other properties
                return false; // Does not match any filter condition
            });
        });

    }

    private class UsersListCell extends ListCell<Admin> {
        @Override
        protected void updateItem(Admin item, boolean empty) {

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
        private void handleActive(Admin user) {
            if (user != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("Are you sure you want to delete this User permanently?");
                alert.setContentText("Choose.");

                alert.showAndWait().ifPresent(response -> {
                    AdminController uc = new AdminController();
                    uc.activate(user);
                    Stage stagee = (Stage) side.getScene().getWindow();
                    FXMLLoader loaderr = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/DelAdminTables.fxml"));
                    Parent roott = null;
                    try {
                        roott = loaderr.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Scene scenee = new Scene(roott);
                    // Set the new scene to the stage
                    stagee.setScene(scenee);
                    stagee.show();
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No User Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a User to edit.");
                alert.showAndWait();

            }
        }

        @FXML
        private void handleDelete(Admin user) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this User permanently?");
            alert.setContentText("Choose.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    AdminController uc = new AdminController();
                    uc.delete(user);
                    Stage stagee = (Stage) side.getScene().getWindow();
                    FXMLLoader loaderr = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/DelAdminTables.fxml"));
                    Parent roott = null;
                    try {
                        roott = loaderr.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Scene scenee = new Scene(roott);
                    // Set the new scene to the stage
                    stagee.setScene(scenee);
                    stagee.show();
                }
            });
        }

    }

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

    @FXML
    void delbusiness(ActionEvent event) throws IOException {
        Stage stage = (Stage) side.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/DelBusinessTables.fxml"));
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
}
