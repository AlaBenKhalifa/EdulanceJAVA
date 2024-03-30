package com.example.edulancejava.Connectors;

import com.example.edulancejava.Connectors.Entities.Business;
import com.example.edulancejava.Connectors.Entities.NormalUser;

import java.io.NotActiveException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NormalUserController {
    public void add(NormalUser user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "INSERT INTO normal_user (name, family_name, image, phone_number, email, nationality, language, reputation, description, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getFamilyName());
            statement.setString(3, user.getImage()); // Assuming image path
            statement.setInt(4, user.getPhoneNumber());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getNationality());
            statement.setString(7, user.getLanguage());
            statement.setInt(8, user.getReputation());
            statement.setString(9, user.getDescription());
            statement.setString(10, user.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<NormalUser> show()
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<NormalUser> users = new ArrayList<>();
        String query = "SELECT * FROM normal_user";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                NormalUser user = new NormalUser();
                user.setName(resultSet.getString("Name"));
                user.setFamilyName(resultSet.getString("Family_Name"));
                user.setPhoneNumber(resultSet.getInt("Phone_Number"));
                user.setEmail(resultSet.getString("Email"));
                user.setPassword(resultSet.getString("Password"));
                user.setLanguage(resultSet.getString("Language"));
                user.setDescription(resultSet.getString("Description"));
                user.setId(resultSet.getInt("Id"));
                user.setNationality(resultSet.getString("Nationality"));

                users.add(user);

            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return users;
    }

    public void update(NormalUser user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE normal_user "
                + "SET Name = ?, Family_Name = ?, Phone_Number = ?, Email = ?, Password = ?, Language = ?, Description = ?, Nationality = ? "
                + "WHERE Id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getFamilyName());
            statement.setInt(3, user.getPhoneNumber());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getLanguage());
            statement.setString(7, user.getDescription());
            statement.setString(8, user.getNationality());
            statement.setInt(9, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void delete(NormalUser user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "DELETE FROM normal_user WHERE Id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

}
