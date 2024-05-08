package com.example.edulancejava.Controller.Offre;

import com.example.edulancejava.Connectors.Entities.Offre;
import com.example.edulancejava.Connectors.GetJobsQuery;
import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class DisplayJobAdminController implements Initializable {
    private Offre offre; // Declare Offre object


    public void setOffre(Offre offre) {
        this.offre = offre; // Set the Offre object
        // Use the Offre object to populate your edit fields, if needed
    }


    @FXML
    private TableColumn<Offre, Button> AcceptColumn;

    @FXML
    private TableView<Offre> jobTableView;

    @FXML
    private TableColumn<Offre, String> titleColumn;

    @FXML
    private TableColumn<Offre, String> typeColumn;

    @FXML
    private TableColumn<Offre, String> experienceColumn;

    @FXML
    private TableColumn<Offre, String> salaryColumn;

    @FXML
    private TableColumn<Offre, String> expirationColumn;

    @FXML
    private TableColumn<Offre, String> languageColumn;

    @FXML
    private TableColumn<Offre, String> descriptionColumn;

    @FXML
    private TableColumn<Offre, String> categorytitre; // Add category column

    private final ObservableList<Offre> jobList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        loadJobs();
    }

    private void initTable() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeOffre()));
        experienceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExperienceLevel()));
        salaryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSalary()));
        expirationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpirationDate().toString()));
        languageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLanguage()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        categorytitre.setCellValueFactory(cellData -> {
            Offre offre = cellData.getValue();
            if (offre != null && offre.getCategorie() != null) {
                return new SimpleStringProperty(offre.getCategorie());
            } else {
                return new SimpleStringProperty(""); // Or any default value you want to display
            }
        });




        jobTableView.setItems(jobList);

        AcceptColumn.setCellValueFactory(cellData -> {
            Button approveButton = new Button("Approve");
            approveButton.setOnAction(event -> handleApproveButtonAction(event, cellData.getValue()));
            return new SimpleObjectProperty<>(approveButton);
        });

    }
    private void handleApproveButtonAction(ActionEvent event, Offre offre) {
        try {
            // Set the offre as approved
            offre.setApproved(true);

            // Update the offre in the database
            GetJobsQuery query = new GetJobsQuery();
            query.updateOffre(offre);

            // Remove the offre from the job list
            jobList.remove(offre);

            // Refresh the TableView
            jobTableView.refresh();

            String userEmail = getUserEmailByOfferId(offre.getId());

            // Send an email notification
            sendEmail(userEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUserEmailByOfferId(int OffreId) {
        MySQLConnectors connection = new MySQLConnectors();

        String email = null;
        String query = "SELECT email FROM global_user WHERE id IN (SELECT id_creator_id FROM offre WHERE id = ?)";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, OffreId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                    System.out.println("Retrieved email: " + email);
                } else {
                    System.out.println("No email found for complaint with ID: " + OffreId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;

    }


    public static void sendEmail(String recipientEmail) {
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
            message.setSubject("Your Job has been approved !");
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
                    "<p><strong>Course Name:</strong> " + "test" + "</p>" +
                    "<p>Cheers! Please Confirm Your Participation</p>" +
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
                    "</button>" +
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


    private void loadJobs() {
        try {
            GetJobsQuery query = new GetJobsQuery();
            List<Offre> jobs = query.getNotApprovedJobs();
            jobList.addAll(jobs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void switchToAnotherViewaddcategory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/category.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
