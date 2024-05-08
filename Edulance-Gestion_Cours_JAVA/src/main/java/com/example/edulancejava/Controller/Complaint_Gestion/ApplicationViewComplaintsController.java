package com.example.edulancejava.Controller.Complaint_Gestion;

import com.example.edulancejava.Connectors.Entities.Complaint;
import com.example.edulancejava.Connectors.MySQLConnectors;
import com.example.edulancejava.Controller.Course_Gestion.Course;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ApplicationViewComplaintsController implements Initializable {


    @FXML
    private Button acceptButton; // Button to open chat

    @FXML
    private void acceptConversation(ActionEvent event) {

        System.out.println("Accepting a conversation...");

        // Launch the ChatClient application
        launchChatClient();
    }
    private void launchChatClient() {
        try {
            // Launch the ChatClient application
            ChatClient chatClient = new ChatClient();
            Stage stage = new Stage();
            chatClient.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @FXML
    private TableColumn<Complaint, Integer> idColumn;



    @FXML
    private TableView<Complaint> complaintsTable;

    @FXML
    private TableColumn<Complaint, String> dateColumn;

    @FXML
    private TableColumn<Complaint, String> typeColumn;

    @FXML
    private TableColumn<Complaint, String> descriptionColumn;

    @FXML
    private TableColumn<Complaint, String> StatusColumn;

    @FXML
    private TableColumn<Complaint, Image> imageColumn;
    @FXML
    private TextField searchByDateTextField;

    @FXML
    private TextField searchByTypeTextField;
    private ObservableList<Complaint> allComplaints;

    private ObservableList<Complaint> filteredList;

//hne zeda salhet hajet aandhom relation bel faza mtaa les button


    @FXML
    private TableColumn<Complaint, Integer> priorityColumn;
    public List<Complaint> getAllComplaints() {
        MySQLConnectors connection = new MySQLConnectors();
        List<Complaint> complaints = new ArrayList<>();

        String query = "SELECT id AS id, date, type, description, priority, image_path,status,seenByUser FROM complaint";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Complaint complaint = new Complaint();
                complaint.setId_reclamation(resultSet.getInt("id"));

                java.sql.Date date = resultSet.getDate("date");
                if (date != null) {
                    complaint.setDate(date);
                } else {

                    complaint.setDate(new Date());
                }
                complaint.setType(resultSet.getString("type"));
                complaint.setDescription(resultSet.getString("description"));
                complaint.setPriority(resultSet.getInt("priority"));
                complaint.setImagePath(resultSet.getString("image_path"));
                complaint.setStatus(resultSet.getString("status"));
                complaint.setSeenByUser(resultSet.getBoolean("seenByUser"));

                complaints.add(complaint);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return complaints;
    }
    public String getUserEmailByComplaintId(int complaintId) {
        MySQLConnectors connection = new MySQLConnectors();

        String email = null;
        String query = "SELECT email FROM global_user WHERE id IN (SELECT id_user_id FROM complaint WHERE id = ?)";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                    System.out.println("Retrieved email: " + email);
                } else {
                    System.out.println("No email found for complaint with ID: " + complaintId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;

    }
    public static Course getCourseInfoFromDatabase(int courseId) {
        MySQLConnectors connection = new MySQLConnectors();
        Course course = null;

        String query = "SELECT description FROM course WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, courseId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String description = resultSet.getString("description");
                course = new Course(description);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving course information: " + e.getMessage());
        }

        return course;
    }

    public static void sendEmail(String recipientEmail,int courseId) {
        // Sender's email

        String senderEmail = "batoutbata5@gmail.com";
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            System.out.println("Recipient email is null or empty. Cannot send email.");
            return;
        }
        // pass
        String password = "ialgvzhizvvrwozy";

        // SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Thank you for your feedback");
            Course course = getCourseInfoFromDatabase(courseId);
            String messageText = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }" +
                    ".container { max-width: 600px; margin: 0 auto; padding: 20px; border-radius: 10px; background-color: #f4f4f4; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
                    "h1 { color: #007bff; text-align: center; }" +
                    "p { line-height: 1.6; }" +
                    ".welcome, .compensation, .closing { margin-bottom: 20px; }" +
                    ".welcome p:first-child { margin-top: 0; }" +
                    ".compensation { background-color: #f9f9f9; padding: 15px; border-radius: 5px; }" +
                    ".compensation p { margin: 10px 0; }" +
                    ".logo { text-align: center; margin-bottom: 20px; }" +
                    ".logo img { max-width: 200px; }" +
                    ".closing { text-align: center; color: #888888; font-style: italic; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='logo'>" +
                    // "<img src='https://photos.google.com/photo/AF1QipNEzAqtl-JTTIWX9U9FEQ6VR6j0m7oHf1aiuvJd' alt='Company Logo'>" +
                    "</div>" +
                    "<h1>Welcome to Our Community! </h1>" +
                    "<div class='welcome'>" +
                    "<p>Dear Customer,</p>" +
                    "<p>We are thrilled to welcome you to our community! 🎉 🎉 🎉 Your feedback is invaluable to us, and we appreciate your input. 🎉 🎉 🎉</p>" +
                    "</div>" +
                    "<div class='compensation'>" +
                    "<p>As a token of our appreciation and to make up for any inconvenience caused, we would like to offer you the following course:</p>" +
                    "<p><strong>Course Name:</strong> " + course.getDescription() + "</p>" +
                    "<p>Cheers! Please Confirm Your Participation</p>" +
                    "<a href='https://example.com/confirmation'>" +
                    "<button style='background-color: #4CAF50; /* Green */" +
                    "            border: none;" +
                    "            color: white;" +
                    "            padding: 15px 32px;" +
                    "            text-align: center;" +
                    "            text-decoration: none;" +
                    "            display: inline-block;" +
                    "            font-size: 16px;" +
                    "            margin: 4px 2px;" +
                    "            transition-duration: 0.4s;" +
                    "            cursor: pointer;" +
                    "            border-radius: 12px;'>" +
                    "  Confirm Participation" +
                    "</button>" +
                    "</a>" +
                    "<p>We hope you find this course valuable and enjoyable.</p>" +
                    "</div>" +

                    "<p>We hope you find this course valuable and enjoyable.</p>" +
                    "</div>" +
                    "<div class='closing'>" +
                    "<p>Cheers! 🎉 🎉 🎉</p>" +
                    "<p>Edulance</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

// Set email message content
            message.setContent(messageText, "text/html; charset=utf-8");
            Transport.send(message);



            System.out.println("Email sent successfully!");



        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Complaint> complaintsList = getAllComplaints();

        allComplaints = FXCollections.observableArrayList(complaintsList);

        complaintsTable.getItems().addAll(allComplaints);

        filteredList = FXCollections.observableArrayList(allComplaints);

        searchByDateTextField.textProperty().addListener((observable, oldValue, newValue) -> filterComplaints());
        searchByTypeTextField.textProperty().addListener((observable, oldValue, newValue) -> filterComplaints());

        complaintsTable.setItems(filteredList);

        //idColumn.setCellValueFactory(new PropertyValueFactory<>("id_reclamation"));

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        imageColumn.setCellValueFactory(cellData -> {
            try {
                Image image = new Image(cellData.getValue().getImagePath());
                return new SimpleObjectProperty<>(image);
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                return null;
            }
        });

        imageColumn.setCellFactory(column -> {
            return new TableCell<Complaint, Image>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(Image image, boolean empty) {
                    super.updateItem(image, empty);
                    if (empty || image == null) {
                        setGraphic(null);
                    } else {
                        imageView.setImage(image);
                        imageView.setFitWidth(100);
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    }
                }
            };
        });



        complaintsTable.getItems().addAll(complaintsList);

        ObservableList<Complaint> observableComplaints = FXCollections.observableArrayList(complaintsList);
        filteredList = FXCollections.observableArrayList(complaintsList);
        searchByDateTextField.textProperty().addListener((observable, oldValue, newValue) -> filterComplaints());
        searchByTypeTextField.textProperty().addListener((observable, oldValue, newValue) -> filterComplaints());


        complaintsTable.setItems(filteredList);





        TableColumn<Complaint, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(315);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button respondButton = new Button("Respond");
            private final Button displayResponsesButton = new Button("Display Response");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");


            {
                respondButton.setOnAction(event -> {
                    // Get the selected complaint
                    Complaint selectedComplaint = getTableView().getItems().get(getIndex());

                    // Check if the complaint is resolved
                    if (selectedComplaint.getStatus().equalsIgnoreCase("Resolved")) {
                        // Change the text of the button to "Recompensate"
                        respondButton.setText("Recompensate");

                        // Get the course information from the database

                        // Send an email with the course description

                        // Create a custom alert
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Complaint Resolved");
                        alert.setHeaderText("This complaint has already been resolved.");
                        alert.setContentText("No further response is needed.");
                        alert.showAndWait();
                        String userEmail = getUserEmailByComplaintId(selectedComplaint.getId_reclamation());
                        sendEmail(userEmail,1);
                    } else {
                        // Proceed with the regular functionality
                        // Your existing code to open the response window
                    }
                });


                displayResponsesButton.setOnAction(event -> {

                    Complaint selectedComplaint = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/displaycomplaintresponse.fxml"));
                    try {
                        Parent parent = loader.load();
                        DisplayComplaintResponseController displayComplaintResponseController = loader.getController();
                        displayComplaintResponseController.setComplaintId(selectedComplaint.getId_reclamation());

                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent)); // Taille moyenne de l'interface
                        stage.initStyle(StageStyle.UTILITY);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(ResponseController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                });

                // editButton.setOnAction(event -> handleEdit());
                respondButton.getStyleClass().add("respond-button");
                displayResponsesButton.getStyleClass().add("display-button");

            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Complaint selectedComplaint = getTableView().getItems().get(getIndex());

                    if (selectedComplaint.isSeenByUser()) {
                        setGraphic(displayResponsesButton);
                    } else {
                        setGraphic(new HBox(10, respondButton, displayResponsesButton));
                    }
                }
            }
        });
        complaintsTable.getColumns().add(actionsColumn);
    }

    private void filterComplaints() {
        String typeFilter = searchByTypeTextField.getText().trim().toLowerCase();
        String dateFilter = searchByDateTextField.getText().trim().toLowerCase();

        // Filter complaints based on typeFilter and dateFilter
        filteredList.clear();
        for (Complaint complaint : allComplaints) {
            if (complaint.getType().toLowerCase().contains(typeFilter) &&
                    complaint.getDate().toString().toLowerCase().contains(dateFilter)) {
                filteredList.add(complaint);
            }
        }
    }


}



