package com.example.edulancejava.Connectors;

import com.example.edulancejava.Connectors.Entities.Complaint;
import com.example.edulancejava.Connectors.Entities.complaint_response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayQuery {




    public List<Complaint> getAllComplaints() {
        MySQLConnectors connection = new MySQLConnectors();
        List<Complaint> complaints = new ArrayList<>();
        String query = "SELECT * FROM complaint";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Complaint complaint = new Complaint();
                java.sql.Date date = resultSet.getDate("date");
                if (date != null) {
                    complaint.setDate(date);
                } else {

                    complaint.setDate(new Date());
                }
                complaint.setType(resultSet.getString("type"));
                complaint.setDescription(resultSet.getString("description"));
                complaint.setPriority(resultSet.getInt("priority"));
                complaint.setImagePath(resultSet.getString("image_path"));
                complaint.setId_reclamation(resultSet.getInt("id"));
                complaint.setStatus(resultSet.getString("status"));

                complaints.add(complaint);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return complaints;
    }




    public String getSubmissionTime(int complaintId) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "SELECT date FROM complaint WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Date submissionDate = resultSet.getTimestamp("date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return dateFormat.format(submissionDate);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving submission time: " + e.getMessage());
        }
        return null;
    }




    public void deleteResponse(int complaintId) {
        MySQLConnectors connection = new MySQLConnectors();
        String queryDelete = "DELETE FROM complaint_response WHERE id_complaint_id = ?";
        String queryUpdateStatus = "UPDATE complaint SET status = 'In progress: ' WHERE id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statementDelete = conn.prepareStatement(queryDelete);
             PreparedStatement statementUpdateStatus = conn.prepareStatement(queryUpdateStatus)) {

            // Delete the response
            statementDelete.setInt(1, complaintId);
            int rowsAffected = statementDelete.executeUpdate();

            // Update the status of the complaint
            statementUpdateStatus.setInt(1, complaintId);
            statementUpdateStatus.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Response deleted successfully for Complaint ID: " + complaintId);
                System.out.println("Status updated to 'In progress: ' for Complaint ID: " + complaintId);
            } else {
                System.out.println("No response deleted for Complaint ID: " + complaintId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting response or updating status: " + e.getMessage());
        }
    }

    public void updateReponse(complaint_response reponse) {

        try {
            String requete = "UPDATE complaint_response SET response=? Where id =?";
            PreparedStatement pst =new MySQLConnectors().getCnx().prepareStatement(requete);

            pst.setString(1, reponse.getResponse());
            pst.setInt(2,reponse.getId());
            pst.executeUpdate();
            System.out.println("Reclamation modifiée");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }



    public void deleteComplaint(Complaint complaintToDelete) {
        MySQLConnectors connection = new MySQLConnectors();
        String query = "DELETE FROM complaint WHERE id = ?";
        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintToDelete.getId_reclamation());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Complaint deleted successfully!");
            } else {
                System.out.println("No complaint deleted. The complaint with ID " +
                        complaintToDelete.getId_reclamation() + " was not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting complaint: " + e.getMessage());
        }
    }




    public String getComplaintResponse(int complaintId) {
        MySQLConnectors connection = new MySQLConnectors();

        String response = null;
        String query = "SELECT response FROM complaint_response WHERE id_complaint_id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response = resultSet.getString("response");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }
}
