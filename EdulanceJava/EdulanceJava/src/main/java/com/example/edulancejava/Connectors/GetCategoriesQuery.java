package com.example.edulancejava.Connectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.edulancejava.Connectors.Entities.Category;

public class GetCategoriesQuery {
    public List<Category> getCategories() throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setTitre(resultSet.getString("titre"));
                categories.add(category);
            }
        }

        return categories;
    }
}
