package com.example.edulancejava.Connectors;

import com.example.edulancejava.Connectors.Entities.Complaint;
import com.example.edulancejava.Connectors.Entities.Offre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditJobQuery {

    public void updateOffre(Offre offre) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE offre SET titre = ?, type_offre = ?, experience_level = ?, salary = ?, expiration_date = ?, description = ?, language = ? WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, offre.getTitre());
            statement.setString(2, offre.getTypeOffre());
            statement.setString(3, offre.getExperienceLevel());
            statement.setString(4, offre.getSalary());
            statement.setDate(5, java.sql.Date.valueOf(offre.getExpirationDate()));
            statement.setString(6, offre.getDescription());
            statement.setString(7, offre.getLanguage());
            statement.setInt(8, offre.getId()); // Assuming you have an ID field in Offre class


            System.out.println("Offre updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating offre: " + e.getMessage());
        }
    }

}
