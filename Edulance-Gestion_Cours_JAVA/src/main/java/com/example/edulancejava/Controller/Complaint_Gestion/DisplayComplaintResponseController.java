package com.example.edulancejava.Controller.Complaint_Gestion;

import com.example.edulancejava.Connectors.DisplayQuery;
import com.example.edulancejava.Connectors.Entities.Complaint;
import com.example.edulancejava.Connectors.Entities.complaint_response;
import com.example.edulancejava.Connectors.MySQLConnectors;
import com.example.edulancejava.UpdateResponseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class DisplayComplaintResponseController implements Initializable {
    @FXML
    private Label complaintDateLabel;

    @FXML
    private Label complaintIdLabel;

    @FXML
    private Label complaintResponseLabel;

    private DisplayQuery displayQuery;
    private int complaintId;


    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;




    @FXML
    private TableView<Complaint> complaintsTable;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayQuery = new DisplayQuery();

        String complaintResponse = getComplaintResponse(complaintId);

        if (complaintResponse != null) {
            complaintResponseLabel.setText("Response: " + complaintResponse);
            if (getResponseVu(complaintId)) {
                deleteButton.setVisible(false);
                editButton.setVisible(false);
            }else{
                deleteButton.setVisible(true);
                editButton.setVisible(true);
            }
        } else {
            complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);
            // Rendre les boutons "Edit" et "Delete" invisibles si aucune réponse n'est trouvée
            deleteButton.setVisible(false);
            editButton.setVisible(false);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        complaintDateLabel.setText("Date: " + currentDate);
    }
    /*
    public boolean isSeenByUser(int complaintId) {
        boolean isSeenByUser = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Établir une connexion à la base de données
            connection = new MySQLConnectors().getCnx();

            // Requête SQL pour sélectionner l'attribut "vu" de la réclamation avec l'ID donné
            String query = "SELECT  seenByUser  FROM complaint WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, complaintId);

            // Exécution de la requête
            resultSet = preparedStatement.executeQuery();

            // Si un enregistrement est trouvé, récupérer la valeur de l'attribut "vu"
            if (resultSet.next()) {
                isSeenByUser = resultSet.getBoolean(" isSeenByUser");
            }
        } catch (SQLException e) {
            System.out.println("Error getting response vu: " + e.getMessage());
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return  isSeenByUser;
    }
*/


    public boolean getResponseVu(int complaintId) {
        boolean vu = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = new MySQLConnectors().getCnx();
            String query = "SELECT seenByUser FROM complaint WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, complaintId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                vu = resultSet.getBoolean("seenByUser");
            }
        } catch (SQLException e) {
            System.out.println("Error getting response vu: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return vu;
    }







    public String getComplaintResponse(int complaintId)
    {
        return displayQuery.getComplaintResponse(complaintId);
    }


    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
        complaintIdLabel.setText("Complaint ID: " + complaintId);

        String complaintResponse = getComplaintResponse(complaintId);

        if (complaintResponse != null) {
            complaintResponseLabel.setText("Response: " + complaintResponse);
            if (getResponseVu(complaintId)) {
                deleteButton.setVisible(false);
                editButton.setVisible(false);
            } else{
                deleteButton.setVisible(true);
                editButton.setVisible(true);
            }
        } else {
            complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);
            deleteButton.setVisible(false);
            editButton.setVisible(false);

        }

    }



    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        // Récupérer la réponse de la réclamation associée à l'ID complaintId
        String complaintResponse = getComplaintResponse(complaintId);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this response?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Supprimer la réponse de la base de données
                if (complaintResponse != null) {
                    displayQuery.deleteResponse(complaintId);
                    // Mettre à jour l'affichage pour indiquer que la réponse a été supprimée
                    complaintResponseLabel.setText(""); // Effacer le contenu du label
                    complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);


                }
            }
        });

    }



    @FXML
    private void handleEditButtonAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Update");
        alert.setHeaderText("Are you sure you want to update this response?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Charger la nouvelle interface depuis le fichier FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/UpdateResponse.fxml"));
                    Parent root = loader.load();

                    // Obtenez le contrôleur de la nouvelle interface
                    UpdateResponseController controller = loader.getController();

                    // Passez la réponse à mettre à jour au contrôleur de la nouvelle interface
                    // Récupérer la réponse associée à l'ID de la réclamation
                    complaint_response selectedReponse = getReponseById(complaintId); // Vous devez implémenter getReponseById pour récupérer la réponse associée à l'ID
                    controller.setReponseToUpdate(selectedReponse);

                    // Affichez la nouvelle interface dans une nouvelle fenêtre ou dans la même fenêtre en utilisant un Stage différent
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        displayQuery = new DisplayQuery();
        // Initialize the UI with the complaint ID
        complaintIdLabel.setText("Complaint ID: " + complaintId);

        // Récupérer la réponse de la réclamation
        String complaintResponse = getComplaintResponse(complaintId);


        // Récupérer les détails de la réponse
        String reponseDetails = getReponseDetails(complaintId);

        // Afficher les détails de la réponse
        complaintResponseLabel.setText(reponseDetails);

        // Afficher la réponse s'il y en a une, sinon afficher un message indiquant qu'aucune réponse n'a été trouvée
        if (complaintResponse != null) {
            complaintResponseLabel.setText("Response: " + complaintResponse);
        } else {
            complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);
        }
    }

    public complaint_response getReponseById(int complaintId) {
        MySQLConnectors connection = new MySQLConnectors();
        complaint_response  reponse = null;
        String query = "SELECT * FROM  complaint_response WHERE id_complaint_id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                reponse = new complaint_response();
                reponse.setId(resultSet.getInt("id"));
                reponse.setResponse(resultSet.getString("response"));
                // Assurez-vous de récupérer tous les autres attributs nécessaires de la réponse depuis le ResultSet
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon vos besoins
        }

        return reponse;
    }


    public String getReponseDetails(int complaintId) {

        return getReponseDetailss(complaintId);
    }



    public String getReponseDetailss(int complaintId) {
        MySQLConnectors connection = new MySQLConnectors();

        String details = null;
        String query = "SELECT * FROM complaint_response WHERE id_complaint_id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les détails de la réclamation à partir du ResultSet
                String id = resultSet.getString("id");
                String status = resultSet.getString("status");
                String description = resultSet.getString("response");
                Date dateReponse = resultSet.getDate("date");

                // Construire la chaîne de détails de la réclamation
                StringBuilder sb = new StringBuilder();
                sb.append("id: ").append(id).append("\n");
                sb.append("status: ").append(status).append("\n");
                sb.append("description: ").append(description).append("\n");
                sb.append("Date: ").append(dateReponse).append("\n");


                // Affecter la chaîne de détails
                details = sb.toString();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return details;
    }




}











