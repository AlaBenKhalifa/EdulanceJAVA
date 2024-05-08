package com.example.edulancejava;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Home_Controller  implements Initializable {
    @FXML
    private AnchorPane sidebar_click;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sidebar_click.setOnMouseClicked(event -> {
            System.out.println("ola");
            System.exit(0);
        });
    }
}
