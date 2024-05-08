package com.example.edulancejava.Connectors;
import com.example.edulancejava.Connectors.Entities.Complaint;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.List;
import java.util.Properties;


public class AddQuery {
    public void addComplaint(Complaint newComplaint, String image_path, int id) {
        MySQLConnectors connection = new MySQLConnectors();

        String query = "INSERT INTO complaint (type, description, priority,date,image_path,status,id_user_id) VALUES (?,?,?,?, ?, ?,?)";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, newComplaint.getType());
            statement.setString(2, newComplaint.getDescription());
            statement.setInt(3, newComplaint.getPriority());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            statement.setString(5, image_path);
            statement.setString(6, "Sent");
            statement.setInt(7, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating complaint failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int complaintId = generatedKeys.getInt(1);
                    String userEmail = getUserEmailById(newComplaint.getUserId());

                    sendEmail(userEmail);
                } else {
                    throw new SQLException("Creating complaint failed, no ID obtained.");
                }
            }

            System.out.println("Complaint added successfully!");
            DisplayQuery displayQuery = new DisplayQuery();
            List<Complaint> updatedComplaints = displayQuery.getAllComplaints();

            for (Complaint complaint : updatedComplaints) {
                System.out.println(complaint.toString());
            }
        } catch (SQLException e) {
            System.out.println("Error adding complaint: " + e.getMessage());
        }
    }




    public String getUserEmailById(int id) {
        String email = null;
        String query = "SELECT email FROM global_user WHERE id = ?";
        try (Connection conn = MySQLConnectors.getCnx();
             PreparedStatement statement = conn.prepareStatement(query))
        {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                    System.out.println("Retrieved email: " + email);
                } else {
                    System.out.println("No email found for user with ID: " + id);
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
            message.setSubject("Thank you for your feedback");
            message.setText("Dear Customer,\n\nThank you for your feedback. We appreciate your input.\n\nBest regards,\nYour Company");

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}