package com.example.edulancejava.Connectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteJOBQuery {

    public void deleteOffre(int offreId) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "DELETE FROM offre WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, offreId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Offre deleted successfully!");
            } else {
                System.out.println("No offre found with ID: " + offreId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting offre: " + e.getMessage());
        }
    }
}
