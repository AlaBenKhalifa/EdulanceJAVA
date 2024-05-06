package com.example.edulance.Services;

import com.example.edulance.Entities.NormalUser;
import com.example.edulance.Tools.MySQLConnectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NormalUserController {

    int newGlobalUserId = 0;
    public void add(NormalUser user) throws SQLException {
        ArrayList<Integer> id = new ArrayList<>();
        MySQLConnectors connection = new MySQLConnectors();
        String query = "INSERT INTO global_user (name, family_name, image, phone_number, email, nationality, language, reputation, description, password,Roles,Archieve) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,0,'ACTIVE')";
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
        query = "SELECT MAX(id) FROM global_user;";
        MySQLConnectors connectionn = new MySQLConnectors();
        try (Connection connn = connectionn.getCnx();
             PreparedStatement statement = connn.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                newGlobalUserId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(newGlobalUserId);
        MySQLConnectors connectio = new MySQLConnectors();
        query = "INSERT INTO normal_user (id,score) VALUES (?,0)";
        try (Connection co = connectio.getCnx();
             PreparedStatement statementt = co.prepareStatement(query)){
            statementt.setInt(1, newGlobalUserId);
            statementt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<NormalUser> show()
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<NormalUser> users = new ArrayList<>();
        String query = "SELECT global_user.*, normal_user.* FROM global_user INNER JOIN normal_user ON global_user.id = normal_user.id where global_user.roles = 0 and global_user.Archieve = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "ACTIVE");
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
                user.setImage(resultSet.getString("Image"));
                user.setScore(resultSet.getInt("Score"));

                users.add(user);

            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return users;
    }

    public List<NormalUser> showdel()
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<NormalUser> users = new ArrayList<>();
        String query = "SELECT * FROM global_user where roles = 0 and Archieve = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "Deleted");
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
                user.setImage(resultSet.getString("Image"));

                users.add(user);

            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return users;
    }

    public void update(NormalUser user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE global_user "
                + "SET Name = ?, Family_Name = ?, Phone_Number = ?, Email = ?, Language = ?, Description = ?, Nationality = ?,Image = ? "
                + "WHERE Id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getFamilyName());
            statement.setInt(3, user.getPhoneNumber());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getLanguage());
            statement.setString(6, user.getDescription());
            statement.setString(7, user.getNationality());
            statement.setString(8, user.getImage());
            statement.setInt(9, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
        MySQLConnectors connectio = new MySQLConnectors();
        query = "UPDATE normal_user "
                + "SET Score = ? "
                + "WHERE Id = ?";
        try (Connection con = connectio.getCnx();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, user.getScore());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void delact(NormalUser user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE global_user "
                + "SET Archieve = ? "
                + "WHERE Id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "Deleted");
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void activate(NormalUser user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE global_user "
                + "SET Archieve = ? "
                + "WHERE Id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "ACTIVE");
            statement.setInt(2, user.getId());
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
        MySQLConnectors connectionn = new MySQLConnectors();
        query = "DELETE FROM global_user WHERE Id = ?";
        try (Connection con = connectionn.getCnx();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

}
