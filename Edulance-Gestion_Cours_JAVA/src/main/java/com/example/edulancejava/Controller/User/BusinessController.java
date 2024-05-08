package com.example.edulancejava.Controller.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.edulancejava.Connectors.Entities.Business;
import com.example.edulancejava.Connectors.MySQLConnectors;


public class BusinessController
{
    int newGlobalUserId = 0;
    public void add(Business user) throws SQLException {
        ArrayList<Integer> id = new ArrayList<>();
        MySQLConnectors connection = new MySQLConnectors();
        String query = "INSERT INTO global_user (name, family_name, image, phone_number, email, nationality, language, reputation, description, password,Roles,Archieve) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,1,'ACTIVE')";
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
        query = "INSERT INTO business (id,web_site) VALUES (?,?)";
        try (Connection co = connectio.getCnx();
             PreparedStatement statementt = co.prepareStatement(query)){
            statementt.setInt(1, newGlobalUserId);
            statementt.setString(2,"");
            statementt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Business> show()
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<Business> users = new ArrayList<>();
        String query = "SELECT global_user.*,business.* from global_user INNER JOIN business on global_user.id = business.id where global_user.roles = 1 and global_user.archieve = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1,"ACTIVE");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Business user = new Business();
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
                user.setWebSite(resultSet.getString("web_site"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return users;
    }

    public void update(Business user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE global_user "
                + "SET Name = ?, Family_Name = ?, Phone_Number = ?, Email = ?, Language = ?, Description = ?, Nationality = ?, Image = ? "
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
        query = "UPDATE business "
                + "SET web_site = ? "
                + "WHERE Id = ?";
        try (Connection con = connectio.getCnx();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, user.getWebSite());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void delete(Business user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "DELETE FROM business WHERE Id = ?";
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

    public void delact(Business user) {
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

    public List<Business> showdel()
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<Business> users = new ArrayList<>();
        String query = "SELECT * FROM global_user where roles = 1 and Archieve = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "Deleted");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Business user = new Business();
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

    public void activate(Business user) {
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
}
