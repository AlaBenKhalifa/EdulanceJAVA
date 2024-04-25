// EditComplaintController.java
package com.example.edulancejava;

import com.example.edulancejava.Connectors.EditQuery;
import com.example.edulancejava.Connectors.Entities.Complaint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EditComplaintController{
    @FXML
    private CheckBox tfrtypetechnical;

    @FXML
    private CheckBox tftypeconflict;
    @FXML
    private TextField tfType;
    @FXML
    private Label lblImagePath;
    private String imagePath;
    @FXML
    private TextArea tfDescription;

    @FXML
    private ChoiceBox<String> tfpriority;


    private Complaint complaintToEdit;

    public void initData(Complaint complaint) {
        this.complaintToEdit = complaint;
        // Populate the text fields with complaint data
        String type = complaint.getType();
        if (type.contains("Technical")) {
            tfrtypetechnical.setSelected(true);
        }
        if (type.contains("Conflict")) {
            tftypeconflict.setSelected(true);
        }        tfDescription.setText(complaint.getDescription());
        tfpriority.setValue(String.valueOf(complaint.getPriority()));
        lblImagePath.setText(complaint.getImagePath()); // Set the existing image path

    }

    @FXML
    private void handleSave(ActionEvent event) {
        saved = true;

        if (complaintToEdit != null) {
            if (!tfrtypetechnical.isSelected() && !tftypeconflict.isSelected()) {
                showAlert("Validation Error", "Please select at least one type.", Alert.AlertType.ERROR);
                return;
            }
            StringBuilder typeBuilder = new StringBuilder();
            if (tfrtypetechnical.isSelected()) {
                typeBuilder.append("Technical");
            }
            if (tftypeconflict.isSelected()) {
                if (!typeBuilder.isEmpty()) {
                    typeBuilder.append(", ");
                }
                typeBuilder.append("Conflict");
            }

            String priorityText = tfpriority.getValue().toString();

            if (priorityText.isEmpty()) {
                showAlert("Validation Error", "Please select a priority.", Alert.AlertType.ERROR);
                return;
            }

            try {
                int priority = Integer.parseInt(priorityText);
                if (priority < 1 || priority > 3) {
                    showAlert("Validation Error", "Priority must be between 1 and 3.", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Priority must be a valid number.", Alert.AlertType.ERROR);
                return;
            }



            complaintToEdit.setType(typeBuilder.toString());
            complaintToEdit.setDescription(tfDescription.getText());

            complaintToEdit.setPriority(Integer.parseInt(priorityText));
            complaintToEdit.setImagePath(lblImagePath.getText()); // Set the image path

            // Create a new instance of the EditQuery class
            EditQuery editQuery = new EditQuery();

            // Call the updateComplaint method of the EditQuery class to update the complaint in the database
            editQuery.updateComplaint(complaintToEdit);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Create a custom label for the message with styling
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

        // Create a custom grid pane for layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(messageLabel, 0, 0);

        // Set the custom grid pane as the content of the alert dialog
        alert.getDialogPane().setContent(gridPane);

        // Show the alert dialog
        alert.showAndWait();
    }
    private boolean saved = false;

    // Other controller code

    public boolean isSaved() {
        return saved;
    }
    @FXML
    private void uploadImage() {
        // Implement image uploading functionality here
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(lblImagePath.getScene().getWindow());

        // Update the label with the selected image path
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            lblImagePath.setText(imagePath);
        }
    }
}
