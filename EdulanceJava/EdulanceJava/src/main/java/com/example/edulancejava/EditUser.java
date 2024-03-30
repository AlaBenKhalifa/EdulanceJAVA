package com.example.edulancejava;

import com.example.edulancejava.Connectors.Entities.Complaint;
import com.example.edulancejava.Connectors.Entities.NormalUser;
import com.example.edulancejava.Connectors.NormalUserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUser {

    Integer id;

    boolean saved = false;

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
    private Label lblImagePath;

    @FXML
    private Button saveButton;

    public void initData(NormalUser user){
        // Populate the text fields with complaint data
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

        /*if (type.contains("Technical")) {
            tfrtypetechnical.setSelected(true);
        }
        if (type.contains("Conflict")) {
            tftypeconflict.setSelected(true);
        }        tfDescription.setText(complaint.getDescription());
        tfpriority.setValue(String.valueOf(complaint.getPriority()));
        lblImagePath.setText(complaint.getImagePath()); // Set the existing image path*/

    }

    @FXML
    void handleSave(ActionEvent event) {
        NormalUser user = new NormalUser();
        user.setId(id);
        user.setName(Name.getText().toString());
        user.setFamilyName(FamilyName.getText().toString());
        user.setEmail(Email.getText().toString());
        user.setPhoneNumber(Integer.parseInt(PhoneNumber.getText().toString()));
        user.setLanguage(Language.getText().toString());
        user.setNationality(Nationality.getText().toString());
        user.setDescription(Description.getText().toString());
        user.setPassword(Password.getText().toString());
        NormalUserController uc = new NormalUserController();
        uc.update(user);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public boolean isSaved() {
        return saved;
    }


}
