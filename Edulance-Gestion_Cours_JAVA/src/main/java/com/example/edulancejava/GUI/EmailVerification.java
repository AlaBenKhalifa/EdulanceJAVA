package com.example.edulancejava.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailVerification {

    @FXML
    private TextField Codee;

    @FXML
    private Label lblImagePath;

    @FXML
    private Button saveButton;

    private int code;

    private String email;

    private boolean saved = false;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void initData(int c,String e){
        code = c;
        sendEmail(e,String.valueOf(code));
        System.out.println(code);
        System.out.println(e);
    }

    @FXML
    void handleSave(ActionEvent event) {
        int co = Integer.parseInt(Codee.getText());
        if(co == code){
            System.out.println(Codee.getText());
            System.out.println(code);
            saved = true;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Code");
            alert.showAndWait();
        }
    }

    public static void sendEmail(String recipientEmail,String code) {
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
                    code +
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

    public boolean isSaved() {
        return saved;
    }
}