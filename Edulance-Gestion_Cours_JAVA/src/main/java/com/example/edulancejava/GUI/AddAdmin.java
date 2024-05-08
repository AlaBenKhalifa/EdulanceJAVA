package com.example.edulancejava.GUI;


import com.example.edulancejava.Connectors.Entities.Admin;
import com.example.edulancejava.Connectors.ImageVerificationAPI;
import com.example.edulancejava.Controller.User.AdminController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddAdmin implements Initializable {


    public boolean saved = false;
    private String imagePath;
    @FXML
    private TextArea Description;

    @FXML
    private TextField Email;

    @FXML
    private TextField FamilyName;

    @FXML
    private TextField Language;

    @FXML
    private TextField Name;

    @FXML
    private TextField Nationality;

    @FXML
    private PasswordField Password;

    @FXML
    private TextField PhoneNumber;

    @FXML
    private Label lblImagePath;

    @FXML
    private Button saveButton;

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imagePath = "C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\icons\\icons8-person-94.png";
        Image image = new Image(new File(imagePath).toURI().toString());
        imageView.setImage(image);
    }

    @FXML
    void AddAdmin(ActionEvent event) throws SQLException, IOException {
        String description = Description.getText();
        String email = Email.getText();
        String familyName = FamilyName.getText();
        String language = Language.getText();
        String name = Name.getText();
        String nationality = Nationality.getText();
        String password = Password.getText();
        ImageVerificationAPI imgverif = new ImageVerificationAPI();
        try {
            if(!imgverif.sendRequest(imagePath)){
                showAlert("You can't insert this image. Please put an image which does not contain Nudity, Drugs or offensive content.");
                deleteImage(imagePath);
                imagePath = "C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\icons\\icons8-person-94.png";
                Image image = new Image(new File(imagePath).toURI().toString());
                imageView.setImage(image);
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int phoneNumber = 0; // Default value in case parsing fails
        try {
            phoneNumber = Integer.parseInt(PhoneNumber.getText());
            if ((phoneNumber < 10000000)||(phoneNumber > 99999999)){
                showAlert("Phone number must be a valid number.");
                return;
            }
        } catch (NumberFormatException e) {
            // Handle parsing error
            showAlert("Phone number must be a valid integer.");
            return; // Exit the method early
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid email format.");
            return;
        }

        if (description.isEmpty() || email.isEmpty() || familyName.isEmpty() || language.isEmpty() ||
                name.isEmpty() || nationality.isEmpty() || password.isEmpty()) {
            showAlert("Please fill out all required fields.");
            return;
        }
            Admin user = new Admin(name, familyName, imagePath, phoneNumber, email, nationality, language, description, password);
            AdminController usercontroller = new AdminController();
            usercontroller.add(user);
            saved = true;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void DeleteImage(ActionEvent event) {
        if(!imagePath.equals("C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\icons\\icons8-person-94.png")){
            deleteImage(imagePath);
            imagePath = "C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\icons\\icons8-person-94.png";
            Image image = new Image(new File(imagePath).toURI().toString());
            imageView.setImage(image);
        }
    }

    public void deleteImage(String imagePath) {
        File imageFile = new File(imagePath);

        // Check if the file exists
        if (imageFile.exists()) {
            // Attempt to delete the file
            boolean deleted = imageFile.delete();

            if (deleted) {
                System.out.println("Image deleted successfully.");
            } else {
                System.out.println("Failed to delete the image.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        if(!imagePath.equals("C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\icons\\icons8-person-94.png")){
            deleteImage(imagePath);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        // Expression régulière pour vérifier le format d'e-mail
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = saveImageAndGetPath(file);
            Image image = new Image(new File(imagePath).toURI().toString());
            imageView.setImage(image);
        }

    }

    private String saveImageAndGetPath(File file) {
        String destinationPath = "C:\\Users\\MSI\\Desktop\\Edulance-Gestion_Cours_JAVA\\src\\main\\resources\\com\\example\\edulancejava\\Images"; // Specify your destination directory
        File destinationFile = new File(destinationPath, file.getName());
        try {
            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isSaved(){
        return saved;
    }

}
