package com.example.edulance.GUI;

import com.example.edulance.Entities.Admin;
import com.example.edulance.Entities.Business;
import com.example.edulance.Entities.NormalUser;
import com.example.edulance.Services.AdminController;
import com.example.edulance.Services.NormalUserController;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class AdminAdminTables implements Initializable {

    @FXML
    private Button Button;

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
    private TextField searchField;

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

        List<Admin> list = controller.show();

        Table.getItems().clear();

        Table.setCellFactory(param -> new UsersListCell());

        ObservableList<Admin> dataList = FXCollections.observableArrayList(list);
        FilteredList<Admin> filteredList = new FilteredList<>(dataList, p -> true);

        Table.setItems(filteredList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all items when the filter is empty
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
                Button editButton = new Button("edit");
                editButton.setOnAction(event -> handleEdit(item));
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                Button deleteButton = new Button("delete");
                deleteButton.setOnAction(event -> {
                    handleDelete(item);
                });

                deleteButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");
                Button PDFButton = new Button("Download Resume");
                PDFButton.setOnAction(event -> {
                    handlepdf(item);
                });
                card.getChildren().addAll(nameLabel, familynameLabel, EmailLabel);
                String imagePath = item.getImage();
                ImageView imageView = null;
                if (imagePath != null && !imagePath.isEmpty()) {
                    imageView = new ImageView();
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);

                    Image image = new Image(new File(imagePath).toURI().toString());
                    imageView.setImage(image);
                }
                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                HBox buttonList = new HBox(10);
                buttonList.getChildren().addAll(editButton, deleteButton, PDFButton);
                card.getChildren().add(buttonList);
                buttonBox.getChildren().addAll(imageView, card);
                setGraphic(buttonBox);
            }
        }

        void handlepdf(Admin user){
            File templateFile = new File("C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\index.html");
            StringBuilder templateContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(templateFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    templateContent.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String htmlContent = templateContent.toString()
                    .replace("$NAME$", user.getName())
                    .replace("$IMG$",getImageName(user.getImage()))
                    .replace("$FAMILYNAM$",user.getFamilyName());
            try {
                Files.write(Paths.get("C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\temp.html"), htmlContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Process process = new ProcessBuilder("C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe", "C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\temp.html", "C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\output.pdf").start();
                process.waitFor();
            } catch (IOException e) {
                System.err.println("Error: wkhtmltopdf command not found. Make sure wkhtmltopdf is installed and added to the system PATH.");
                e.printStackTrace();
                return; // Exit the method or handle the error accordingly
            } catch (InterruptedException e) {
                e.printStackTrace();
                return; // Exit the method or handle the error accordingly
            }

            // Open PDF file using default PDF viewer
            try {
                File pdfFile = new File("C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\output.pdf");
                if (pdfFile.exists()) {
                    java.awt.Desktop.getDesktop().open(pdfFile);
                }
                else{
                    System.out.println("xxx");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @FXML
        private void handleEdit(Admin user) {
            if (user != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/EditAdmin.fxml"));
                    Parent root = loader.load();
                    EditAdmin edituser = loader.getController();
                    edituser.initData(user);
                    Stage stage = new Stage();
                    stage.setTitle("Edit User");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                    if (edituser.isSaved()) {
                        Stage stagee = (Stage) side.getScene().getWindow();
                        FXMLLoader loaderr = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/AdminAdminTables.fxml"));
                        Parent roott = loaderr.load();

                        // Create a new scene with the loaded FXML
                        Scene scenee = new Scene(roott);
                        // Set the new scene to the stage
                        stagee.setScene(scenee);
                        stagee.show();
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
        private void handleDelete(Admin user) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this User?");
            alert.setContentText("Choose.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    AdminController uc = new AdminController();
                    uc.delact(user);
                    Stage stagee = (Stage) side.getScene().getWindow();
                    FXMLLoader loaderr = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/AdminAdminTables.fxml"));
                    Parent roott = null;
                    try {
                        roott = loaderr.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Create a new scene with the loaded FXML
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/DelUserTables.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/DelBusinessTables.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/AdminBusinessTables.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/AdminTables.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);
        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }

    public static String getImageName(String imagePath) {
        Path path = Paths.get(imagePath);
        return path.getFileName().toString();
    }

    @FXML
    void AddAdmin(ActionEvent event) throws IOException {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/AddAdmin.fxml"));
                    Parent root = loader.load();
                    AddAdmin adduser = loader.getController();
                    Stage stage = new Stage();
                    stage.setTitle("Add an Admin");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                    if (adduser.isSaved()) {
                        AdminController uc = new AdminController();
                        List<Admin> users = uc.show();

                        Table.getItems().clear();

                        Table.getItems().addAll(users);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    @FXML
    void deladmin(ActionEvent event) throws IOException {
        Stage stage = (Stage) side.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/DelAdminTables.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);
        // Set the new scene to the stage
        stage.setScene(scene);
        stage.show();
    }
}
