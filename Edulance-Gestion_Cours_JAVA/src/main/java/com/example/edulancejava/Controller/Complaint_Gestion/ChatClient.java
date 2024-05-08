package com.example.edulancejava.Controller.Complaint_Gestion;

import com.example.edulancejava.Controller.Complaint_Gestion.ClientThread;
import com.example.edulancejava.Connectors.MySQLConnectors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ChatClient extends Application {


    private void showChatRequestNotification(String requesterName) {
        Alert notification = new Alert(Alert.AlertType.INFORMATION);
        notification.setTitle("Chat Request");
        notification.setHeaderText("New chat request from: " + requesterName);
        notification.setContentText("Would you like to accept this chat request?");

        ButtonType acceptButton = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        notification.getButtonTypes().setAll(acceptButton, cancelButton);

        Optional<ButtonType> result = notification.showAndWait();
        if (result.isPresent() && result.get() == acceptButton) {
            handleAcceptRequest();
        }
    }
    private void handleAcceptRequest() {
        launchChatClient();
    }
    private void launchChatClient() {
        try {
            // Launch the ChatClient application
            ChatClient chatClient = new ChatClient();
            Stage stage = new Stage();
            chatClient.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private static final DatagramSocket socket1;
    private static final DatagramSocket socket2;

    private static final InetAddress address;

    private static String identifier1 = null;
    private static String identifier2 = null;

    private static final int SERVER_PORT = 8001; // send to server

    private static final TextArea messageArea1 = new TextArea();
    private static final TextArea messageArea2 = new TextArea();


    private static final TextField inputBox1 = new TextField();
    private static final TextField inputBox2 = new TextField();

    static {
        try {
            socket1 = new DatagramSocket(); // init to any available port
            socket2 = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        launch(); // launch GUI
    }

    @Override
    public void start(Stage primaryStage) {
        showLoginDialog(primaryStage, socket1, messageArea1, inputBox1, socket2, messageArea2, inputBox2);
    }

    private void startClientThread(DatagramSocket socket, TextArea messageArea, String identifier, TextArea messageAreaOtherUser) {
        ClientThread clientThread = new ClientThread(socket, messageArea, identifier, messageAreaOtherUser);
        clientThread.start();
    }

    private void showLoginDialog(Stage primaryStage, DatagramSocket socket1, TextArea messageArea1, TextField inputBox1, DatagramSocket socket2, TextArea messageArea2, TextField inputBox2) {
        inputBox1.setOnAction(e -> {
            if (!inputBox1.getText().isEmpty()) {
                showChatRequestNotification(inputBox1.getText());
            }
        });

        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter your ID");

        Alert loginDialog = new Alert(Alert.AlertType.CONFIRMATION);
        loginDialog.setTitle("Login");
        loginDialog.setHeaderText("Enter your ID to login");
        loginDialog.getDialogPane().setContent(userIdField);

        Optional<ButtonType> result = loginDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String userId = userIdField.getText();
            if (!userId.isEmpty()) {
                String userName = retrieveUserName(Integer.parseInt(userId));
                if (userName != null) {
                    if (identifier1 == null) {
                        identifier1 = userName;
                        setupChat(primaryStage, identifier1, socket1, messageArea1, inputBox1, socket2, messageArea2, inputBox2);
                        startClientThread(socket1, messageArea1, identifier1, messageArea2);
                        showLoginDialog(new Stage(), socket1, messageArea1, inputBox1, socket2, messageArea2, inputBox2);
                    } else if (identifier2 == null) {
                        identifier2 = userName;
                        setupChat(new Stage(), identifier2, socket2, messageArea2, inputBox2, socket1, messageArea1, inputBox1);
                        startClientThread(socket2, messageArea2, identifier2, messageArea1);
                    }
                } else {
                    showAlert("Invalid User ID", "User with ID " + userId + " does not exist.");
                    showLoginDialog(primaryStage, socket1, messageArea1, inputBox1, socket2, messageArea2, inputBox2);
                }
            }
        } else {
            primaryStage.close();
        }
    }

    public String retrieveUserName(int userId) {
        MySQLConnectors connection = new MySQLConnectors();
        String userName = null;
        String query = "SELECT name FROM global_user WHERE id = ?";

        try (Connection conn = connection.getCnx();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userName = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userName;
    }

    private void setupChat(Stage stage, String identifier, DatagramSocket socket, TextArea messageArea, TextField inputBox, DatagramSocket otherSocket, TextArea otherMessageArea, TextField otherInputBox) {
        messageArea.setMaxWidth(500);
        messageArea.setEditable(false);
        inputBox.setMaxWidth(500);
        inputBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage(inputBox, socket, identifier, messageArea, otherMessageArea);
            }
        });

        // Apply stylesheet to the scene
        Scene scene = new Scene(new VBox(35, messageArea, inputBox), 550, 300);
       // scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Apply inline styles to UI elements
        messageArea.getStyleClass().add("message-area");
        inputBox.getStyleClass().add("input-box");

        stage.setScene(scene);
        stage.setTitle("Chat Client - " + identifier);
        stage.show();

        // Start the client thread
        startClientThread(socket, messageArea, identifier, otherMessageArea);
    }


    private void sendMessage(TextField inputBox, DatagramSocket socket, String identifier, TextArea messageArea, TextArea otherMessageArea) {
        if (inputBox.getText().isEmpty()) return;
        if (inputBox.getText().equalsIgnoreCase("exit")) System.exit(0);

        String message = identifier + ": " + inputBox.getText(); // message to send
        byte[] msg = message.getBytes(); // convert to bytes
        inputBox.setText(""); // remove text from input box

        // append message to messageArea
        messageArea.appendText(message + "\n");
        otherMessageArea.appendText(message + "\n");

        // create a packet & send
        DatagramPacket send = new DatagramPacket(msg, msg.length, address, SERVER_PORT);
        try {
            socket.send(send);
            System.out.println("Message sent from client: " + message); // Add debug statement
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
