package com.example.edulancejava;

import com.example.edulancejava.Connectors.DisplayQuery;
import com.example.edulancejava.Connectors.Entities.complaint_response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class UpdateResponseController implements Initializable {
    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextField responseTextField;

    private complaint_response selectedReponse; // Ajoutez cet attribut pour stocker la reponse sélectionnée


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Vérifiez si une réponse est sélectionnée
        if (selectedReponse != null) {
            // Remplissez les champs avec les valeurs de la réponse sélectionnée
            responseTextField.setText(selectedReponse.getResponse());
            statusComboBox.setValue(selectedReponse.getStatus());
        }

    }

    public void setReponseToUpdate(complaint_response reponse) {
        if (reponse != null) {
            selectedReponse = reponse;
            responseTextField.setText(reponse.getResponse());
            // Vous pouvez remplir d'autres champs avec les valeurs de la réponse si nécessaire
        }
    }

    @FXML
    private void updateReponse(ActionEvent event) {
        // Vérifier
        if (selectedReponse == null) {
            showAlert("Aucune reponse à mettre à jour.");
            return;
        }

        // Récupérer les nouvelles valeurs des champs
        String description = responseTextField.getText();


        // Vérifier si les champs description et type sont vides
        if (description.isEmpty()) {
            showAlert("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        // Mettre à jour la reponse
        selectedReponse.setResponse(description);
        selectedReponse.setDate(new Date()); // Mettre à jour la date de réponse


        // Appeler la méthode updateReponse de  service Reponse
        DisplayQuery reponseService = new DisplayQuery();
        reponseService.updateReponse(selectedReponse);


    }


    public void switchToAnotherView2(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/ApplicationViewComplaints.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int complaintId;


    public void setComplaintId(int idReclamation) {
        this.complaintId = idReclamation;
    }



}

