package com.example.edulancejava.Connectors;

import com.example.edulancejava.Connectors.Entities.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCategoryQuery {
    public void addCategory(Category newCategory) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "INSERT INTO categorie (titre) VALUES (?)";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, newCategory.getTitre());

            statement.executeUpdate();

            System.out.println("Category added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }
}
