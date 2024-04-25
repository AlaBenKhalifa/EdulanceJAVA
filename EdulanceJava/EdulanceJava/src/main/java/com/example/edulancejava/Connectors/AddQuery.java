package com.example.edulancejava.Connectors;
import com.example.edulancejava.Connectors.Entities.Complaint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AddQuery {
    public void addComplaint(Complaint newComplaint, String image_path) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "INSERT INTO complaint (type, description, priority,date,image_path,status) VALUES (?,?,?,?, ?, ?)";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, newComplaint.getType());
            statement.setString(2, newComplaint.getDescription());
            statement.setInt(3, newComplaint.getPriority());
            Date currentDate = new Date();
            statement.setTimestamp(4, new java.sql.Timestamp(currentDate.getTime()));
            statement.setString(5, image_path);
            statement.setString(6, "Sent");

            statement.executeUpdate();

            System.out.println("Complaint added successfully!");
            DisplayQuery displayQuery = new DisplayQuery();
            List<Complaint> updatedComplaints = displayQuery.getAllComplaints();

            // Example: Print each complaint
            for (Complaint complaint : updatedComplaints) {
                System.out.println(complaint.toString());
            }
        } catch (SQLException e) {
            System.out.println("Error adding complaint: " + e.getMessage());
        }
    }}
