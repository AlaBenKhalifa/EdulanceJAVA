package com.example.edulancejava.Connectors;

import com.example.edulancejava.Connectors.Entities.Complaint;
import com.example.edulancejava.Connectors.MySQLConnectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditQuery {
    public void updateComplaint(Complaint complaint) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE complaint SET type = ?, description = ?, priority = ?, date = ?, image_path = ? WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, complaint.getType());
            statement.setString(2, complaint.getDescription());
            statement.setInt(3, complaint.getPriority());
            statement.setDate(4, new java.sql.Date(complaint.getDate().getTime()));
            statement.setString(5, complaint.getImagePath());
            statement.setInt(6, complaint.getId_reclamation());
            statement.executeUpdate();
            System.out.println("Complaint updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating complaint: " + e.getMessage());
        }
    }
}
