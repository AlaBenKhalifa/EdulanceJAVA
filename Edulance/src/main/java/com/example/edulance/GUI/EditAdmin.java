package com.example.edulance.GUI;

import com.example.edulance.Entities.Admin;
import com.example.edulance.Entities.NormalUser;
import com.example.edulance.Services.AdminController;
import com.example.edulance.Services.NormalUserController;
import com.example.edulance.Tools.ImageVerificationAPI;
import com.example.edulance.Tools.MySQLConnectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditAdmin {

    int id;
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

    String imagePath,imagePathh;

    @FXML
    private Button saveButton;

    public boolean saved = false;

    @FXML
    private ImageView imageView;



    public void initData(Admin user){
        saved = true;
        id = user.getId();
        Name.setText(user.getName());
        FamilyName.setText(user.getFamilyName());
        Email.setText(user.getEmail());
        PhoneNumber.setText(user.getPhoneNumber().toString());
        Language.setText(user.getLanguage());
        Nationality.setText(user.getNationality());
        Description.setText(user.getDescription());
        Password.setText(user.getPassword());
        imagePath = user.getImage();
        imagePathh = user.getImage();
        Image image = new Image(new File(imagePath).toURI().toString());
        imageView.setImage(image);
    }

    @FXML
    void cancel(ActionEvent event) {
        if(!imagePath.equals(imagePathh)){
            deleteImage(imagePath);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSave(ActionEvent event) {
        Admin user = new Admin();
        user.setId(id);
        user.setName(Name.getText().toString());
        user.setFamilyName(FamilyName.getText().toString());
        user.setEmail(Email.getText().toString());
        user.setPhoneNumber(Integer.parseInt(PhoneNumber.getText().toString()));
        user.setLanguage(Language.getText().toString());
        user.setNationality(Nationality.getText().toString());
        user.setDescription(Description.getText().toString());
        user.setPassword(Password.getText().toString());
        user.setImage(imagePath);
        ImageVerificationAPI imgverif = new ImageVerificationAPI();
        try {
            if(!imgverif.sendRequest(imagePath)){
                showAlert("You can't insert this image. Please put an image which does not contain Nudity, Drugs or offensive content.");
                deleteImage(imagePath);
                imagePath = imagePathh;
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

        if (!isValidEmail(user.getEmail())) {
            showAlert("Invalid email format.");
            return;
        }

        if(EmailValidator(user.getEmail(),user.getId())){
            showAlert("This email already used.");
            return;
        }

        if (user.getDescription().isEmpty() || user.getEmail().isEmpty() || user.getFamilyName().isEmpty() || user.getLanguage().isEmpty() ||
                user.getName().isEmpty() || user.getNationality().isEmpty() || user.getPassword().isEmpty()) {
            showAlert("Please fill out all required fields.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Edit");
        alert.setHeaderText("Are you sure you want to Update this Admin?");
        alert.setContentText("Choose.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                AdminController uc = new AdminController();
                uc.update(user);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        });
    }

    public boolean isSaved() {
        return saved;
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
        String destinationPath = "C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\Images"; // Specify your destination directory

        // Generate unique filename using current timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = timestamp + "_" + file.getName();

        File destinationFile = new File(destinationPath, fileName);
        try {
            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    public boolean EmailValidator(String Email,int id)
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<NormalUser> users = new ArrayList<>();
        String query = "SELECT COUNT(*) FROM global_user where email = ? and id <> ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, Email);
            statement.setInt(2, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next(); // Move cursor to the first row
            int count = resultSet.getInt(1);

            // Close resources
            resultSet.close();
            statement.close();
            conn.close();
            return count > 0;
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
            return false;
        }
    }



}