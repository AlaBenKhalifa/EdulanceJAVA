package com.example.edulancejava.Connectors;

import com.example.edulancejava.Connectors.Entities.Complaint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayQuery {
    public List<Complaint> getAllComplaints() {
        MySQLConnectors connection = new MySQLConnectors();
        List<Complaint> complaints = new ArrayList<>();
        String query = "SELECT * FROM complaint";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Complaint complaint = new Complaint();
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
                complaint.setId_reclamation(resultSet.getInt("id"));

                complaints.add(complaint);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return complaints;
    }
    public void deleteComplaint(Complaint complaintToDelete) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "DELETE FROM complaint WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintToDelete.getId_reclamation());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Complaint deleted successfully!");
            } else {
                System.out.println("No complaint deleted. The complaint with ID " +
                        complaintToDelete.getId_reclamation() + " was not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting complaint: " + e.getMessage());
        }
    }
}
