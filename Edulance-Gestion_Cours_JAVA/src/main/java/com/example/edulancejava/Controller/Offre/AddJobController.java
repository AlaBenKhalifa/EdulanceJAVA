package com.example.edulancejava.Controller.Offre;

import com.example.edulancejava.Connectors.AddJobQuery;
import com.example.edulancejava.Connectors.Entities.Offre;
import com.example.edulancejava.Connectors.MySQLConnectors;
import com.example.edulancejava.Connectors.UserSession;
import com.example.edulancejava.Controller.Complaint_Gestion.ComplaintController1;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddJobController implements Initializable {
    @FXML
    private TextField titlefield;
    @FXML
    private BorderPane container;
    @FXML
    private DatePicker datefield;

    @FXML
    private TextField descriptionfield;

    @FXML
    private ComboBox<String> categoryfield;

    @FXML
    private ComboBox<String> experiencefield;

    @FXML
    private ComboBox<String> jobtypefield;

    @FXML
    private ComboBox<String> languagefield;

    @FXML
    private ComboBox<String> salaryfield;
    private ListView<Offre> jobListView;
    private ObservableList<Offre> offresList = FXCollections.observableArrayList();

    List<String> prohibitedWords = loadProhibitedWords("C:\\Users\\mouni\\Downloads\\EdulanceJavafin\\EdulanceJavafin\\EdulanceJava\\EdulanceJava\\src\\main\\java\\utils\\BadWords.txt");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateCategoryComboBox();
    }

    @FXML
    private Button sttButton;

    @FXML
    private Text Repreponse;
    @FXML
    private javafx.scene.control.Button Button;
    @FXML
    void stt(MouseEvent event){
        // Create a new Task for asynchronous speech recognition
        Task<String> recognitionTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                // Replace with your subscription key and region
                SpeechConfig speechConfig = SpeechConfig.fromSubscription("9060f63ba0654e7b8533abfa8e407a6e", "westeurope");

                try (SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, AudioConfig.fromDefaultMicrophoneInput())) {
                    System.out.println("Speak into your microphone."); // Inform user to speak

                    Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
                    SpeechRecognitionResult result = task.get();

                    if (result.getReason() == ResultReason.Canceled) {
                        System.out.println("Cancellation detected.");
                        return null;
                    } else if (result.getReason() == ResultReason.NoMatch) {
                        System.out.println("No speech recognized.");
                        return null;
                    } else {
                        String recognizedText = result.getText();
                        //System.out.println("Recognized text: " + recognizedText);
                        return recognizedText;
                    }
                }
            }
        };

        // Start the recognition task and handle the result
        recognitionTask.setOnSucceeded(event1 -> {
            String transcribedText = recognitionTask.getValue();
            if (transcribedText != null) {
                // Update UI element (if desired)
                descriptionfield.setText(transcribedText);
            }
        });

        recognitionTask.setOnFailed(event1 -> {
            Throwable exception = recognitionTask.getException();
            System.err.println("Speech recognition failed: " + exception.getMessage());
        });

        new Thread(recognitionTask).start();
    }



    @FXML
    void AddJob(ActionEvent event) throws IOException {
        String title = titlefield.getText();
        String commentText = descriptionfield.getText();
        String sanitizedComment = replaceBadWords(commentText, prohibitedWords);
        System.out.println(sanitizedComment);
        boolean containsProhibitedWords = !sanitizedComment.equals(commentText);

        if (containsProhibitedWords) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Your comment contained prohibited words and has been sanitized.");
            alert.showAndWait();
        }

        LocalDate expirationDate = datefield.getValue();
        if (expirationDate == null || expirationDate.isBefore(LocalDate.now())) {
            showAlert("Please select a future expiration date.");
            return;
        }
        String experienceLevel = experiencefield.getValue();
        String jobType = jobtypefield.getValue();
        String language = languagefield.getValue();
        String salary = salaryfield.getValue();

        String selectedCategoryTitle = categoryfield.getValue();

        if (title.isEmpty() || commentText.isEmpty() || experienceLevel == null || jobType == null || language == null || salary == null || expirationDate == null) {
            showAlert("Please fill out all required fields.");
            return;
        }

        if (descriptionExists(commentText)) {
            showAlert("A job offer with the same description already exists.");
            return;
        }

        int categoryId = getCategoryIdFromDatabase(selectedCategoryTitle);
        if (categoryId == -1) {
            showAlert("Selected category not found in the database.");
            return;
        }

        Offre offre = new Offre();
        offre.setTitre(title);
        offre.setDescription(sanitizedComment);
        offre.setExpirationDate(expirationDate);
        offre.setExperienceLevel(experienceLevel);
        offre.setTypeOffre(jobType);
        offre.setLanguage(language);
        offre.setSalary(salary);
        offre.setApproved(false); // Set to false by default

        offre.setCategoryId(categoryId);
        offre.setCategorytitre(selectedCategoryTitle);

        AddJobQuery query = new AddJobQuery();
        query.addOffre(offre, categoryId, selectedCategoryTitle, UserSession.getUser().getId());

        int userId = UserSession.getUser().getRoles();
        System.out.println("User ID: " + UserSession.getUser().getRoles());

        String resourcePath;

        if (userId == 0) {
            resourcePath = "/com/example/edulancejava/Offre/displayjob.fxml";
        } else if (userId == 1) {
            resourcePath = "/com/example/edulancejava/Offre/addJob.fxml";
        } else {
            // Handle invalid user ID or other unexpected conditions
            System.err.println("Invalid user ID or unexpected condition");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("list job");
            stage.show();
            offresList.add(offre);
            showAlertpos("Offer Added successfully");
        } catch (IOException e) {
            // Handle FXML loading exception
            e.printStackTrace();
        }
    }

    // Function to replace bad words with asterisks
    private String replaceBadWords(String commentText, List<String> prohibitedWords) {
        // Create a pattern to match the prohibited words with word boundaries
        String patternString = "\\b(" + String.join("|", prohibitedWords) + ")\\b";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

        // Replace the bad words with asterisks
        Matcher matcher = pattern.matcher(commentText);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            // Replace the bad word with asterisks
            String badWord = matcher.group();
            String asterisks = "*".repeat(badWord.length());
            matcher.appendReplacement(result, asterisks);
        }

        matcher.appendTail(result);
        return result.toString();
    }

    // Function to load prohibited words from a file
    private List<String> loadProhibitedWords(String filePath) {
        List<String> prohibitedWords = new ArrayList<>();

        System.out.println(prohibitedWords);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                prohibitedWords.add(line.trim());
            }
        } catch (IOException e) {
            // Handle file reading errors
            e.printStackTrace();
        }

        return prohibitedWords;

    }

    private boolean descriptionExists(String description) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "SELECT COUNT(*) FROM offre WHERE description = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, description);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking description uniqueness: " + e.getMessage());
        }
        return false;
    }


    private int getCategoryIdFromDatabase(String titre) {
        MySQLConnectors connection = new MySQLConnectors();

        int categoryId = -1;
        String sql = "SELECT id FROM categorie WHERE titre = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, titre);

            System.out.println("SQL Query: " + sql);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    categoryId = resultSet.getInt("id");
                    // Debugging: Print retrieved category ID
                    //System.out.println("Category ID: " + categoryId);
                }
            }
        } catch (SQLException e) {
            // Exception handling: Print exception message
            System.err.println("Error fetching category ID: " + e.getMessage());
            e.printStackTrace();
        }
        return categoryId;
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertpos(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Add successfully");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void populateCategoryComboBox() {
        categoryfield.getItems().clear();
        MySQLConnectors mySQLConnectors = new MySQLConnectors();
        String query = "SELECT titre FROM categorie";
        try (Connection conn = mySQLConnectors.getCnx();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String categoryTitle = resultSet.getString("titre");
                categoryfield.getItems().add(categoryTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void switchToAnotherViewListJob(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/DisplayJob.fxml"));
            Parent root = loader.load();

            // If you need to access the controller of the loaded FXML, you can do it like this:
            DisplayJobController displayJobController = loader.getController();

            // Set the loaded view in your container (assuming container is a BorderPane)
            container.setCenter(root);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("List Job");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
