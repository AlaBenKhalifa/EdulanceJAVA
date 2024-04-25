package com.example.edulancejava.Connectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.edulancejava.Connectors.Entities.Category;
import com.example.edulancejava.Connectors.Entities.Offre;

public class GetJobsQuery {
    public List<Offre> getJobs() throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();
        List<Offre> jobs = new ArrayList<>();
        String query = "SELECT o.*, c.titre AS categorietitre FROM Offre o JOIN categorie c ON o.id_category_id = c.id";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Offre offre = new Offre();
                offre.setId(resultSet.getInt("id"));
                offre.setTitre(resultSet.getString("titre"));
                offre.setTypeOffre(resultSet.getString("type_offre"));
                offre.setExperienceLevel(resultSet.getString("experience_level"));
                offre.setSalary(resultSet.getString("salary"));
                offre.setExpirationDate(resultSet.getDate("expiration_date").toLocalDate());
                offre.setLanguage(resultSet.getString("language"));
                offre.setDescription(resultSet.getString("description"));
                offre.setCategoryId(resultSet.getInt("id_category_id"));
                offre.setCategorie(resultSet.getString("categorietitre"));
                System.out.println("Category Title: " + resultSet.getString("categorietitre")); // Debug output


                jobs.add(offre);
            }
        }

        return jobs;
    }

    public String getCategoryTitle(int categoryId) throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();
        String categoryTitle = null;
        String query = "SELECT titre FROM categorie WHERE id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    categoryTitle = resultSet.getString(1);
                }
            }
        }
        System.out.println(categoryTitle);
        return categoryTitle;
    }

    public int getCategoryId(String categoryTitle) throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();
        int categoryId = -1; // Default value if category ID is not found
        String query = "SELECT id FROM categorie WHERE titre = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, categoryTitle);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    categoryId = resultSet.getInt(1);
                }
            }
        }

        return categoryId;
    }

    public List<String> getAllCategoryTitles() throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();
        List<String> categoryTitles = new ArrayList<>();
        String query = "SELECT titre FROM categorie";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String categoryTitle = resultSet.getString("titre");
                categoryTitles.add(categoryTitle);
            }
        }
        return categoryTitles;
    }
}

