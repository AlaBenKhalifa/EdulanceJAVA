package com.example.edulancejava.Controller.Complaint_Gestion;

import com.example.edulancejava.Connectors.AddQuery;
import com.example.edulancejava.Connectors.DisplayQuery;
import com.example.edulancejava.Connectors.Entities.Complaint;
import com.example.edulancejava.Connectors.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ComplaintController1 implements Initializable {


    @FXML
    private void openChat(ActionEvent event) {
        // Create a new stage for the Gemini chat
        Stage chatStage = new Stage();

        // Start the GeminiChatbot application on the new stage
        GeminiChatbot geminiChatbot = new GeminiChatbot();
        geminiChatbot.start(chatStage);
    }




    @FXML
    private Button openChatButton; // Button to open chat

    @FXML
    private void startConversation(ActionEvent event) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
        System.out.println("Starting a conversation...");

    }








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
    private Label animatedTextLabel;

    private String[] texts = {"Welcome.", "Before Adding your Complaint.", "You may use your chat support."};
    private String[] symbols = {" 😊", " ❗", " 💬"}; // Symbols corresponding to each sentence


    @FXML
    void handleSubmitButton(ActionEvent event) {


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTextContent();

    }
    @FXML


    private void updateTextContent() {
        for (int i = 0; i < texts.length; i++) {
            texts[i] += symbols[i];
        }
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    int sentenceIndex = 0;
                    int charIndex = 0;

                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            String currentText = texts[sentenceIndex];
                            if (charIndex < currentText.length()) {
                                animatedTextLabel.setText(currentText.substring(0, charIndex + 1));
                                charIndex++;
                            } else {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                charIndex = 0;
                                sentenceIndex = (sentenceIndex + 1) % texts.length;
                            }
                        });
                    }
                },
                0,
                180
        );
    }




    @FXML
    private void addReclamation() {
        String description = tfdescribe.getText();
        String type;
        String priorityStr = (String) tfpriority.getValue();
        int priority = Integer.parseInt(priorityStr);

        if (description.isEmpty()) {
            showAlert("Please fill out the description field.");
            return;
        }

        if (!tfrtypetechnical.isSelected() && !tftypeconflict.isSelected()) {
            showAlert("Please select a complaint type.");
            return;
        }

        if (tfrtypetechnical.isSelected()) {
            type = "Technical";
        } else if (tftypeconflict.isSelected()) {
            type = "Conflict";
        } else {
            type = "";
        }

        if (image_path == null || image_path.isEmpty()) {
            showAlert("Please upload an image.");
            return;
        }

        DisplayQuery displayQuery = new DisplayQuery();
        List<Complaint> complaints = displayQuery.getAllComplaints();
        for (Complaint c : complaints) {
            if (c.getDescription().equals(description)) {
                showAlert("A complaint with the same description already exists.");
                return;
            }
        }

        Date currentDate = new Date();

        Complaint complaint = new Complaint(description, type);
        complaint.setDate(currentDate);
        complaint.setPriority(priority);

        AddQuery query = new AddQuery();
        query.addComplaint(complaint, image_path, UserSession.getUser().getId());
        switchToDisplayView();
    }


    private void switchToDisplayView() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/DisplayComplaint.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnSubmit.getScene().getWindow();
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
            image_path = selectedFile.getAbsolutePath();
            lblImagePath.setText(image_path);
            return image_path;
        } else {
            return null;
        }


    }

}
