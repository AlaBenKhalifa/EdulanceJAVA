package com.example.edulancejava.Controller.Complaint_Gestion;

import com.example.edulancejava.Connectors.MySQLConnectors;
import com.example.edulancejava.Controller.Course_Gestion.Course;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ResponseController implements Initializable {

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextField responseTextField;




    @FXML
    private void initialize() {

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

    // Your existing code...

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
    @FXML
    private void handleSubmit(ActionEvent event) {
        // Get input values
        String responseText = responseTextField.getText().trim();
        String status = statusComboBox.getValue();
        int complaintId = this.complaintId;

        // Validate input
        if (responseText.isEmpty() || status == null) {
            showAlert("Please enter response text and select a status.");
            return;
        }

        // Perform database operations
        MySQLConnectors connection = new MySQLConnectors();
        String insertResponseQuery = "INSERT INTO complaint_response (id_complaint_id, response, status,date) VALUES (?, ?, ?,?)";
        String updateComplaintQuery = "UPDATE complaint SET status = ? WHERE id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement insertStatement = conn.prepareStatement(insertResponseQuery);
             PreparedStatement updateStatement = conn.prepareStatement(updateComplaintQuery)) {

            // Set parameters for the INSERT statement
            insertStatement.setInt(1, complaintId);
            insertStatement.setString(2, responseText);
            insertStatement.setString(3, status);
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            insertStatement.setDate(4, currentDate); // Set current date
            int rowsInserted = insertStatement.executeUpdate();

            updateStatement.setString(1, status);
            updateStatement.setInt(2, complaintId);
            updateStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Response added successfully!");
                switchToAnotherView2(event); // Switch to another view
            }
            if (status.equalsIgnoreCase("Resolved")) {
                String userEmail = getUserEmailByComplaintId(complaintId);
                sendEmail(userEmail, 1);
            }

        } catch (SQLException e) {
            System.out.println("Error adding response or updating status: " + e.getMessage());
        }
    }

    public static void showAlert(String message) {
        // Create a new instance of Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Show the alert dialog and wait for user interaction
        alert.showAndWait();
    }


    public void switchToAnotherView2(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/displaycomplaintresponse.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int complaintId;


    public void setComplaintId(int idReclamation) {
        this.complaintId = idReclamation;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

