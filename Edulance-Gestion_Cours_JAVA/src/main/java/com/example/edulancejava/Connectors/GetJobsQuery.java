package com.example.edulancejava.Connectors;

import com.example.edulancejava.Connectors.Entities.Offre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetJobsQuery {
    public List<Offre> getApprovedJobs(int userId) throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();
        List<Offre> jobs = new ArrayList<>();
        String query = "SELECT o.*, c.titre AS categorietitre FROM Offre o JOIN categorie c ON o.id_category_id = c.id WHERE o.approved = true AND id_creator_id = ?"; // Adjust this query according to your database schema

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

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
                jobs.add(offre);
            }
        }

        return jobs;
    }

    public List<Offre> getNotApprovedJobs() throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();
        List<Offre> jobs = new ArrayList<>();
        String query = "SELECT o.*, c.titre AS categorietitre FROM Offre o JOIN categorie c ON o.id_category_id = c.id WHERE o.approved = false"; // Adjust this query according to your database schema

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
                jobs.add(offre);
            }
        }

        return jobs;
    }

    public List<Offre> getAllApprovedJobsForRoleZero() throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();

        List<Offre> jobs = new ArrayList<>();

        String sql = "SELECT * FROM offre WHERE approved = 1 AND id_creator_id IN (SELECT id FROM global_user WHERE Roles = 0)";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){

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
                jobs.add(offre);
            }
        }

        return jobs;
    }


    public List<Offre> getAllApprovedJobsForRole1() throws SQLException {
        MySQLConnectors connection = new MySQLConnectors();

        List<Offre> jobs = new ArrayList<>();

        String sql = "SELECT * FROM offre WHERE approved = 1 AND id_creator_id IN (SELECT id FROM global_user WHERE Roles = 1)";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){

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
                jobs.add(offre);
            }
        }

        return jobs;
    }






















    public void updateOffre(Offre offre) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE Offre SET titre = ?, type_offre = ?, experience_level = ?, salary = ?, expiration_date = ?, description = ?, language = ?, approved = ? WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, offre.getTitre());
            statement.setString(2, offre.getTypeOffre());
            statement.setString(3, offre.getExperienceLevel());
            statement.setString(4, offre.getSalary());
            statement.setDate(5, java.sql.Date.valueOf(offre.getExpirationDate()));
            statement.setString(6, offre.getDescription());
            statement.setString(7, offre.getLanguage());
            statement.setBoolean(8, offre.isApproved());
            statement.setInt(9, offre.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Offre updated successfully!");
            } else {
                System.out.println("Failed to update Offre. No rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating Offre: " + e.getMessage());
        }
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

