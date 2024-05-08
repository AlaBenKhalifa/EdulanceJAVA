package com.example.edulancejava.launcher;

import com.example.edulancejava.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
        if(fxmlLoader==null) System.out.println("failed to load");
        Screen screen=Screen.getPrimary();
        Scene scene = new Scene(fxmlLoader.load(),(screen.getBounds().getWidth()*2/3), screen.getBounds().getHeight()*2/3);
        stage.setTitle("Edulance");
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
        stage.setScene(scene);
        stage.show();
        stage.setScene(scene);

    }

    public static void main(String[] args) {
        launch();
    }

}