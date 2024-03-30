package com.example.edulancejava;

import com.example.edulancejava.Connectors.AddQuery;
import com.example.edulancejava.Connectors.DisplayQuery;
import com.example.edulancejava.Connectors.Entities.Complaint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.control.Label;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ComplaintController1 implements Initializable {

    @FXML
    private Button Button;

    @FXML
    private Button btnSubmit;

    @FXML
    private TextArea tfdescribe;

    @FXML
    private ChoiceBox<?> tfpriority;

    @FXML
    private CheckBox tfrtypetechnical;

    @FXML
    private CheckBox tftypeconflict;
    @FXML
    private Label lblImagePath;
    private String image_path;

    @FXML
    void handleSubmitButton(ActionEvent event) {



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
@FXML
private void addReclamation() {
    String description = tfdescribe.getText();
    String type;
    String priorityStr = (String) tfpriority.getValue(); // Get the selected priority as a String
    int priority = Integer.parseInt(priorityStr); // Convert the String to an int

    if (description.isEmpty()) {
        // Display an error message if the description is empty
        showAlert("Please fill out the text area.");
        return; // Exit the method early if the description is empty
    }

    if (tfrtypetechnical.isSelected()) {
        type = "Technical";
    } else if (tftypeconflict.isSelected()) {
        type = "Conflict";
    } else {
        type = "";
    }
    Date currentDate = new Date(); // Current date and time

    Complaint complaint = new Complaint(description, type);
    complaint.setDate(currentDate); // Set the date
    complaint.setPriority(priority);

    AddQuery query = new AddQuery();
    query.addComplaint(complaint,image_path);
    switchToDisplayView();
}

    private void switchToDisplayView() {
        try {
            // Load the FXML file for the display view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayComplaint.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the stage from the button's scene
            Stage stage = (Stage) btnSubmit.getScene().getWindow();

            // Set the new scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

}


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private String uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            image_path = selectedFile.getAbsolutePath(); // Store the image path
            lblImagePath.setText(image_path); // Update label with file path
            return image_path; // Return image path if a file is selected
        } else {
            return null; // Return null if no file was selected
        }


    }}
