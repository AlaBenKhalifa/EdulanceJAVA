package com.example.edulancejava.Controller.Offre;
import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.DataSource;

import com.example.edulancejava.Connectors.DeleteJOBQuery;
import com.example.edulancejava.Connectors.Entities.GlobalUser;
import com.example.edulancejava.Connectors.Entities.Offre;
import com.example.edulancejava.Connectors.GetJobsQuery;
import com.example.edulancejava.Connectors.MySQLConnectors;
import com.example.edulancejava.Connectors.UserSession;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class DisplayJobController implements Initializable {

    @FXML
    private ListView<Offre> jobListView;

    @FXML
    private Pagination pagination;

    private final int itemsPerPage = 11;
    private ObservableList<Offre> jobList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadJobs();
        pagination.setPageFactory(this::createPage);


        jobListView.setItems(jobList);
        jobListView.setCellFactory(param -> new ListCell<Offre>() {
            @Override
            protected void updateItem(Offre offre, boolean empty) {
                super.updateItem(offre, empty);
                if (empty || offre == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox cardContainer = new VBox();
                    cardContainer.setPadding(new Insets(10));
                    cardContainer.setSpacing(10);
                    cardContainer.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));

                    Label titleLabel = createLabel("Title: " + offre.getTitre(), "#333333", FontWeight.BOLD, 16);
                    Label categoryLabel = createLabel("Category: " + getCategoryTitle(offre.getCategoryId()), "#555555", FontWeight.NORMAL, 14);
                    Label descriptionLabel = createLabel("Description: " + offre.getDescription(), "#555555", FontWeight.NORMAL, 14);
                    descriptionLabel.setWrapText(true);
                    descriptionLabel.setMaxWidth(300); // Limit the width for better readability

                    Label expirationDateLabel = createLabel("Expiration Date: " + offre.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "#555555", FontWeight.NORMAL, 14);
                    Label salaryLabel = createLabel("Salary: " + offre.getSalary(), "#555555", FontWeight.NORMAL, 14);
                    Label languageLabel = createLabel("Language: " + offre.getLanguage(), "#555555", FontWeight.NORMAL, 14);
                    Label experienceLabel = createLabel("Experience Level: " + offre.getExperienceLevel(), "#555555", FontWeight.NORMAL, 14);
                    Label typeLabel = createLabel("Type of Offer: " + offre.getTypeOffre(), "#555555", FontWeight.NORMAL, 14);

                    Button editButton = createButton("Edit", "#4CAF50", event -> handleEditButton(offre));
                    Button deleteButton = createButton("Delete", "#f44336", event -> handleDeleteButton(event,offre));
                    Button applicationButton = createButton("view appliers", "#4FA095", event -> handleApplicationButton(event,offre));

                    HBox buttonContainer = new HBox(10, editButton, deleteButton,applicationButton);
                    buttonContainer.setAlignment(Pos.CENTER_RIGHT);

                    cardContainer.getChildren().addAll(titleLabel, categoryLabel, descriptionLabel, expirationDateLabel, salaryLabel, languageLabel, experienceLabel, typeLabel, buttonContainer);
                    setGraphic(cardContainer);
                }
            }
        });
    }
    private Label createLabel(String text, String color, FontWeight weight, double fontSize) {
        Label label = new Label(text);
        label.setTextFill(Color.web(color));
        label.setFont(Font.font("Arial", weight, fontSize));
        return label;
    }

    // Helper method to create a styled button
    private Button createButton(String text, String color, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        return button;
    }

    private String getCategoryTitle(int categoryId) {
        try {
            return new GetJobsQuery().getCategoryTitle(categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private GlobalUser getGlobalUserInfo(int globalUserId) {
        GlobalUser globalUser = null;
        MySQLConnectors connection = new MySQLConnectors();

        String query = "SELECT * FROM global_user WHERE id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, globalUserId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve user information from the result set
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("name");

                    String description = resultSet.getString("description");
                    String email = resultSet.getString("email");

                    // Create a new GlobalUser object with the retrieved information
                    globalUser = new GlobalUser(id, username,description,email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return globalUser;
    }

    private List<Integer> getGlobalUserIdForOffre(int offreId) {
        List<Integer> globalUserIds = new ArrayList<>();

        MySQLConnectors connection = new MySQLConnectors();
        String query = "SELECT global_user_id FROM offre_global_user WHERE offre_id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, offreId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int globalUserId = resultSet.getInt("global_user_id");
                    globalUserIds.add(globalUserId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return globalUserIds;
    }

    private static void sendEmail(String recipientEmail, Offre selectedJob, GlobalUser applicant) {
        // Sender's email
        String senderEmail = "batoutbata5@gmail.com";
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            System.out.println("Recipient email is null or empty. Cannot send email.");
            return;
        }
        // Sender's password
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
            message.setSubject("Thank you for your application");

            // Create the PDF
            byte[] pdfBytes = generatePdf(selectedJob, applicant);

            // Create the email body
            String messageBody = "Dear " + applicant.getName() + ",\n\n";
            messageBody += "Thank you for applying to the job offer: " + selectedJob.getTitre() + ".\n\n";
            messageBody += "Please find attached your acceptance letter.\n\n";
            messageBody += "Best regards,\n";
            messageBody += "Your Company";

            // Create the email content
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messageBody);

            // Attach the PDF
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = (DataSource) new ByteArrayDataSource(pdfBytes, "application/pdf");
            attachmentPart.setDataHandler(new DataHandler((javax.activation.DataSource) source));
            attachmentPart.setFileName("AcceptanceLetter.pdf");

            // Combine the email body and attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            // Set the email content
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static byte[] generatePdf(Offre selectedJob, GlobalUser applicant) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Add title
            Paragraph title = new Paragraph("Acceptance Letter")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add greeting
            Paragraph greeting = new Paragraph("Dear " + applicant.getName() + ",")
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(greeting);

            // Add content
            Paragraph content = new Paragraph("Congratulations! We are pleased to inform you that your application for the job offer has been accepted.")
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(content);

            // Add offer details
            Paragraph offerDetails = new Paragraph()
                    .add("Offer Title: ")
                    .add(selectedJob.getTitre())
                    .add("\nOffer Creator: ")
                    .add(selectedJob.getDescription()); // Assuming creatorId is the session user ID
            document.add(offerDetails);

            // Add more information as needed

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }




    private void handleAcceptButton(Offre selectedJob, GlobalUser applicant) {
        // Get the email of the applicant
        String applicantEmail = applicant.getEmail();

        // Send the acceptance email
        sendEmail(applicantEmail,selectedJob,applicant);
    }



    private void handleApplicationButton(ActionEvent event, Offre selectedJob) {
        int offreId = selectedJob.getId();
        List<Integer> globalUserIds = getGlobalUserIdForOffre(offreId);

        if (!globalUserIds.isEmpty()) {
            Stage stage = new Stage();
            stage.setTitle("Global Users Information");

            VBox root = new VBox(10);
            root.setPadding(new Insets(20));

            Label titleLabel = new Label("Global Users Information for Offer: " + selectedJob.getTitre());

            for (int globalUserId : globalUserIds) {
                GlobalUser globalUser = getGlobalUserInfo(globalUserId);
                if (globalUser != null) {
                    Label idLabel = new Label("ID: " + globalUser.getId());
                    Label usernameLabel = new Label("Username: " + globalUser.getName());
                    Label description = new Label("Description: " + globalUser.getDescription());
                    Label emailLabel = new Label("Email: " + globalUser.getEmail());

                    // Add other user information labels here if needed

                    Button acceptButton = new Button("Accept");
                    acceptButton.setOnAction(e -> handleAcceptButton(selectedJob, globalUser));

                    VBox userInfoBox = new VBox(5);
                    userInfoBox.getChildren().addAll(idLabel, usernameLabel, description, emailLabel, acceptButton);
                    root.getChildren().add(userInfoBox);
                }
            }

            Scene scene = new Scene(new ScrollPane(root), 400, 300);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("No associated global user IDs found for this Offre.");
        }
    }



    private void handleDeleteButton(ActionEvent event, Offre selectedJob) {
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = selectedJob.getExpirationDate();
        if (currentDate.isAfter(expirationDate)) {
            DeleteJOBQuery deleteJobQuery = new DeleteJOBQuery();
            deleteJobQuery.deleteOffre(selectedJob.getId());
            jobList.remove(selectedJob);
            jobListView.setItems(jobList);
            System.out.println("Offre deleted successfully!");
        }  else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Delete Offre");
            alert.setHeaderText(null);
            alert.setContentText("Cannot delete offre. Expiration date not reached.");
            alert.showAndWait();
        }
    }
    private void handleEditButton(Offre offre) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Offre/editJob.fxml"));
        Parent root;
        try {
            root = loader.load();
            EditJobController controller = loader.getController();
            controller.initData(offre);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            jobListView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadJobs() {
        int userId = UserSession.getUser().getId();
        try {
            GetJobsQuery query = new GetJobsQuery();
            List<Offre> jobs = query.getApprovedJobs(userId);
            jobList.addAll(jobs);

            // Check if the user's role is either 1 or "business"
            int userRole = UserSession.getUser().getRoles(); // Assuming there's a method to get the user's role as an integer
            System.out.println("User Role: " + userRole); // Print the user's role for debugging


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowApplicationsForJob(Offre offre) {
        // Assuming you want to navigate to a new scene or page to display applications
        // You need to have a method to switch scenes or navigate to a new page in your application

        // Example: Navigate to a new scene called "ApplicationsScene.fxml"
      /**  try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationsScene.fxml"));
            Parent root = loader.load();

            // Access the controller of the new scene
            //ApplicationsController controller = loader.getController();

            // Pass any necessary data to the controller (optional)
            // Example: controller.initData(userId);

            // Get the current stage and set the new scene
           // Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           // stage.setScene(new Scene(root));
           // stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
       **/
    }



    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, jobList.size());
        jobListView.setItems(FXCollections.observableArrayList(jobList.subList(fromIndex, toIndex)));
        VBox pageContent = new VBox();
        pageContent.getChildren().add(jobListView);
        return pageContent;
    }
}