package com.example.edulancejava;
import com.example.edulancejava.Connectors.Entities.*;
import com.example.edulancejava.Connectors.NormalUserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;


public class RegistrationController {

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
    private ChoiceBox<?> Role;

    @FXML
    void Registration(ActionEvent event) {

        String description = Description.getText();
        String email = Email.getText();
        String familyName = FamilyName.getText();
        String language = Language.getText();
        String name = Name.getText();
        String nationality = Nationality.getText();
        String password = Password.getText();
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



// Assuming Role is a ChoiceBox<String> - adjust if it's a different type
        String role = Role.getValue().toString();

        if (description.isEmpty() || email.isEmpty() || familyName.isEmpty() || language.isEmpty() ||
                name.isEmpty() || nationality.isEmpty() || password.isEmpty()) {
            showAlert("Please fill out all required fields.");
            return;
        }
        NormalUser user = new NormalUser(name, familyName, null, phoneNumber, email, nationality, language, description, password);
        NormalUserController usercontroller = new NormalUserController();
        usercontroller.add(user);
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

}
