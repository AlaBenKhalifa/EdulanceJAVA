package com.example.edulancejava.Controller.home;

import com.example.edulancejava.Connectors.Entities.GlobalUser;
import com.example.edulancejava.Connectors.UserSession;
import com.example.edulancejava.Controller.Complaint_Gestion.ComplaintController1;
import com.example.edulancejava.Controller.Course_Gestion.Course;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class home implements Initializable {
    @FXML
    private ImageView side_bar;

    @FXML
    private VBox slider;

    @FXML
    private BorderPane container;
    @FXML
    private Button home_nav;

    @FXML
    private Button job_nav;

    @FXML
    private Button jobAvailable_nav;

    @FXML
    private Button SignUp;
    @FXML
    private Button Course_front;

    @FXML
    private Button Complaint_front;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

                try {
                    FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/com/example/edulancejava/components/Welcome_page.fxml"));
                    HBox page = fxmlLoader.load();
                    Course cnt=fxmlLoader.getController();
                    container.setCenter(page);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(UserSession.getUser().getId() != -1)
                {
                    SignUp.setText("Log out");
                }
        home_nav.setOnMouseClicked(event -> {
            try {
                HBox page = FXMLLoader.load(getClass().getResource("/com/example/edulancejava/components/Welcome_page.fxml"));
                container.setCenter(page);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Course_front.setOnMouseClicked(event -> {
                    try {
                        container.setCenter(FXMLLoader.load(getClass().
                                getResource("/com/example/edulancejava/Course/Course.fxml")
                        ));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                );
        Complaint_front.setOnMouseClicked(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/Complaint/AddComplaint-view.fxml"));
            try {
                BorderPane page = fxmlLoader.load(); // Load as AnchorPane
                ComplaintController1 complaintController = fxmlLoader.getController();
                container.setCenter(page);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        job_nav.setOnMouseClicked(event -> {
            int userId = UserSession.getUser().getRoles();
            System.out.println("User ID: " + UserSession.getUser().getRoles());

            String resourcePath;

            if (userId == 0) {
                resourcePath = "/com/example/edulancejava/Offre/addJob.fxml";
            } else if (userId == 1) {
                resourcePath = "/com/example/edulancejava/Offre/addJob.fxml";
            } else {
                // Handle invalid user ID or other unexpected conditions
                System.err.println("Invalid user ID or unexpected condition");
                return;
            }

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourcePath));
                BorderPane page = fxmlLoader.load(); // Load as AnchorPane
                container.setCenter(page);
            } catch (IOException e) {
                // Handle FXML loading exception
                e.printStackTrace();
            }
        });


        jobAvailable_nav.setOnMouseClicked(event -> {
            int userId = UserSession.getUser().getRoles();
            System.out.println("User ID: " + UserSession.getUser().getRoles());

            String resourcePath;

            if (userId == 0) {
                resourcePath = "/com/example/edulancejava/Offre/displayjobBusiness.fxml";
            } else if (userId == 1) {
                resourcePath = "/com/example/edulancejava/Offre/displayjobFreelancer.fxml";
            } else {
                // Handle invalid user ID or other unexpected conditions
                System.err.println("Invalid user ID or unexpected condition");
                return;
            }

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourcePath));
                BorderPane page = fxmlLoader.load(); // Load as AnchorPane
                container.setCenter(page);
            } catch (IOException e) {
                // Handle FXML loading exception
                e.printStackTrace();
            }
        });



        SignUp.setOnMouseClicked(event -> {
            if(UserSession.getUser().getId() == -1) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/UI/Login.fxml"));
                try {

                    Parent root = fxmlLoader.load();

                    // Create a new scene with the loaded FXML
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) home_nav.getScene().getWindow();

                    stage.setScene(scene);
                    stage.show();



                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                GlobalUser u = new GlobalUser(-1);
                UserSession.setUser(u);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/edulancejava/home.fxml"));
                try {
                    Parent root = fxmlLoader.load();

                    // Create a new scene with the loaded FXML
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) home_nav.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();



                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });




        //*/



        int width_side_bar=170;
    slider.setMinSize(0,0);
    slider.setPrefWidth(0);

    side_bar.setRotate((side_bar.getRotate()+180)%360);
        side_bar.setOnMouseClicked(event -> {
            if(slider.getPrefWidth()==0)
                slider.setPrefWidth(width_side_bar);
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4),slider);
            slide.setFromX(side_bar.getRotate()!=0?-slider.getPrefWidth():slider.getTranslateX());
            slide.setToX(side_bar.getRotate()==0 ? -slider.getPrefWidth() : 0);
            slide.play();
            slide.setOnFinished(event1 -> {
                side_bar.setRotate((side_bar.getRotate()+180)%360);
                if(side_bar.getRotate()!=0)
                    slider.setPrefWidth(0);
            });

        });

    }
}
