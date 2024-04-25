package com.example.edulancejava.Connectors;

import com.example.edulancejava.Connectors.Entities.Offre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AddJobQuery {
    public void addOffre(Offre newOffre, int categoryId,String categorytitre) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "INSERT INTO offre (titre, type_offre, experience_level, salary, expiration_date, description, language, id_category_id, categorietitre) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, newOffre.getTitre());
            statement.setString(2, newOffre.getTypeOffre());
            statement.setString(3, newOffre.getExperienceLevel());
            statement.setString(4, newOffre.getSalary());
            statement.setDate(5, java.sql.Date.valueOf(newOffre.getExpirationDate()));
            statement.setString(6, newOffre.getDescription());
            statement.setString(7, newOffre.getLanguage());
            statement.setInt(8, categoryId); // Set the category ID
            statement.setString(9, categorytitre); // Set the category title

            statement.executeUpdate();

            System.out.println("Offre added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding offre: " + e.getMessage());
        }
    }
}


