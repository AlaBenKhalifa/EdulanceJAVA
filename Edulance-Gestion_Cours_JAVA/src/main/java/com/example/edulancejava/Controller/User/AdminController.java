package com.example.edulancejava.Controller.User;

import com.example.edulancejava.Connectors.Entities.Admin;
import com.example.edulancejava.Connectors.MySQLConnectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    int newGlobalUserId = 0;
    public void add(Admin user) throws SQLException {
        ArrayList<Integer> id = new ArrayList<>();
        MySQLConnectors connection = new MySQLConnectors();
        String query = "INSERT INTO global_user (name, family_name, image, phone_number, email, nationality, language, reputation, description, password,Roles,Archieve) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,3,'ACTIVE')";
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
        MySQLConnectors connecti = new MySQLConnectors();
        query = "INSERT INTO expert (id) VALUES (?)";
        try (Connection c = connecti.getCnx();
             PreparedStatement statementt = c.prepareStatement(query)){
            statementt.setInt(1, newGlobalUserId);
            statementt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        MySQLConnectors connec = new MySQLConnectors();
        query = "INSERT INTO admin (id) VALUES (?)";
        try (Connection cont = connec.getCnx();
             PreparedStatement statementt = cont.prepareStatement(query)){
            statementt.setInt(1, newGlobalUserId);
            statementt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Admin> show()
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<Admin> users = new ArrayList<>();
        String query = "SELECT * FROM global_user where roles = 3 and Archieve = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "ACTIVE");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Admin user = new Admin();
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

    public List<Admin> showdel()
    {
        MySQLConnectors connection = new MySQLConnectors();
        ArrayList<Admin> users = new ArrayList<>();
        String query = "SELECT * FROM global_user where roles = 3 and Archieve = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "Deleted");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Admin user = new Admin();
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

    public void update(Admin user) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "UPDATE global_user "
                + "SET Name = ?, Family_Name = ?, Phone_Number = ?, Email = ?, Password = ?, Language = ?, Description = ?, Nationality = ?, Image = ? "
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
            statement.setString(9, user.getImage());
            statement.setInt(10, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public void delact(Admin user) {
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

    public void activate(Admin user) {
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

    public void delete(Admin user) {
        MySQLConnectors connectionnn = new MySQLConnectors();
        String query = "DELETE FROM admin WHERE Id = ?";
        try (Connection con = connectionnn.getCnx();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
        MySQLConnectors connectio = new MySQLConnectors();
        query = "DELETE FROM expert WHERE Id = ?";
        try (Connection conn = connectio.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
        MySQLConnectors connection = new MySQLConnectors();
        query = "DELETE FROM normal_user WHERE Id = ?";
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
