package com.example.edulance.GUI;

import com.example.edulance.Entities.Business;
import com.example.edulance.Entities.NormalUser;
import com.example.edulance.Services.BusinessController;
import com.example.edulance.Services.NormalUserController;
import com.example.edulance.Tools.ImageVerificationAPI;
import com.example.edulance.Tools.MySQLConnectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


public class RegistrationController implements Initializable {

    @FXML
    private TextArea Description;

    @FXML
    private TextField Email;

    @FXML
    private TextField FamilyName;

    @FXML
    private Label pwdv;

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
    private ChoiceBox<?> Role;

    private String imagePath;

    @FXML
    private ImageView imageView;

    private boolean saved = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imagePath = "C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\icons\\icons8-person-94.png";
        Image image = new Image(new File(imagePath).toURI().toString());
        imageView.setImage(image);
        Password.textProperty().addListener((observable, oldValue, newValue) -> {
            // If password is empty, clear the status label
            if (newValue.isEmpty()) {
                pwdv.setText("");
            } else {
                // Otherwise, check the strength of the password and update the status label
                String strength = checkPasswordStrength(newValue,Name.getText(),FamilyName.getText());
                pwdv.setText(strength);
                if (strength.equals("Weak"))
                    pwdv.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                if (strength.equals("Average"))
                    pwdv.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold;");
                if (strength.equals("Strong"))
                    pwdv.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            }
        });
    }

    public static String checkPasswordStrength(String password,String Name,String FamilyName) {


        if (isAverage(password, Name,FamilyName)) {
            return "Average";
        }

        if (isWeak(password,Name,FamilyName)) {
            return "Weak";
        }

        if (isStrong(password,Name,FamilyName)) {
            return "Strong";
        }

        return "";
    }

    private static boolean isStrong(String password,String name,String familyName) {
        if (name != null && !name.isEmpty() && password.toLowerCase().contains(name.toLowerCase())) {
            return false;
        }
        if (familyName != null && !familyName.isEmpty() && password.toLowerCase().contains(familyName.toLowerCase())) {
            return false;
        }
        // Check if password contains all of these 4 (number and a special character and a majiscule and minuscule)
        if (password.matches(".*\\d.*") && password.matches(".*\\W.*") && password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) {
            return true;
        }
        return false;
    }

    private static boolean isWeak(String password,String name,String familyName) {
        for (int i = 0; i < password.length() - 2; i++) {
            if ((password.charAt(i) + 1 == password.charAt(i + 1))
                    && (password.charAt(i + 1) + 1 == password.charAt(i + 2))) {
                return true;
            }
        }
        if (!password.matches(".*\\d.*") || !password.matches(".*\\W.*") || !password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*") || !password.matches(".*[A-Z].*")) {
            return true;
        }

        if (name != null && !name.isEmpty() && password.toLowerCase().contains(name.toLowerCase())) {
            return true;
        }
        if (familyName != null && !familyName.isEmpty() && password.toLowerCase().contains(familyName.toLowerCase())) {
            return true;
        }
        return false;
    }

    private static boolean isAverage(String password,String name,String familyName) {

        if (name != null && !name.isEmpty() && password.toLowerCase().contains(name.toLowerCase())) {
            return false;
        }
        if (familyName != null && !familyName.isEmpty() && password.toLowerCase().contains(familyName.toLowerCase())) {
            return false;
        }
        if(password.length() >=5){
            int count = 0;
            if (password.matches(".*\\d.*")) count++;
            if (password.matches(".*\\W.*")) count++;
            if (password.matches(".*[a-z].*")) count++;
            if (password.matches(".*[A-Z].*")) count++;
            return (count >= 2)&&(count < 4);
        }
        else
            return false;
    }

    @FXML
    void Registration(ActionEvent event) throws SQLException, IOException {
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

        if(EmailValidator(email)){
            showAlert("This email already used.");
            return;
        }

        // Assuming Role is a ChoiceBox<String> - adjust if it's a different type
        String role = Role.getValue().toString();

        if (description.isEmpty() || email.isEmpty() || familyName.isEmpty() || language.isEmpty() ||
                name.isEmpty() || nationality.isEmpty() || password.isEmpty()) {
            showAlert("Please fill out all required fields.");
            return;
        }
        if(pwdv.getText().equals("Weak")){
            showAlert("Your Password is weak.");
            return;
        }
        if(pwdv.getText().equals("Average")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Registration");
            alert.setHeaderText("Your Password is Average. Do you want to register?");
            alert.setContentText("Choose.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    saved = true;
                }
            });
        }

        if(pwdv.getText().equals("Strong")){
            saved = true;
        }

        if(saved == true) {
            FXMLLoader loaderr = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/EmailVerification.fxml"));
            Parent roott = loaderr.load();
            EmailVerification emailverification = loaderr.getController();
            emailverification.initData(generateRandomNumber(100000,999999),email);
            Stage stagee = new Stage();
            stagee.setTitle("Email Verification");
            stagee.setScene(new Scene(roott));
            stagee.showAndWait();
            if (emailverification.isSaved()) {
                if (role.equals("Normal User")) {
                    NormalUser user = new NormalUser(name, familyName, imagePath, phoneNumber, email, nationality, language, description, password);
                    NormalUserController usercontroller = new NormalUserController();
                    usercontroller.add(user);
                } else {
                    Business user = new Business(name, familyName, imagePath, phoneNumber, email, nationality, language, description, password);
                    BusinessController usercontroller = new BusinessController();
                    usercontroller.add(user);
                }
                Stage stage = (Stage) Name.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulance/UI/AdminTables.fxml"));
                Parent root = loader.load();

                // Create a new scene with the loaded FXML
                Scene scene = new Scene(root);
                // Set the new scene to the stage
                stage.setScene(scene);
                stage.show();
            }
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

    private int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
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

    public boolean EmailValidator(String Email)
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<NormalUser> users = new ArrayList<>();
        String query = "SELECT COUNT(*) FROM global_user where email = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, Email);
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

}
