package com.example.edulancejava.Controller.Complaint_Gestion;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class GeminiChatbot extends Application {

    private static final String GEMINI_API_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    private static final String GOOGLE_API_KEY = "AIzaSyBmmZfYnc4K1sHQNkznvmBzwwLjmzJAavE";

    private TextArea chatArea;
    private TextField inputField;
    private Button sendButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gemini Chatbot");

        // UI components
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setStyle("-fx-font-size: 14px; -fx-text-fill: #333; -fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        inputField = new TextField();
        inputField.setPromptText("Type your message here...");
        inputField.setStyle("-fx-font-size: 14px; -fx-text-fill: #333; -fx-background-color: #fff; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendMessage());
        sendButton.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-background-color: #007bff; -fx-border-color: transparent; -fx-border-width: 1px; -fx-padding: 8px 16px; -fx-border-radius: 5px;");

        // Layout
        VBox chatBox = new VBox(chatArea, inputField, sendButton);
        chatBox.setSpacing(10);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: #fff; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        BorderPane root = new BorderPane();
        root.setCenter(chatBox);
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Scene
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            chatArea.appendText("You: " + userInput + "\n");

            try {
                String aiResponse = getAIResponse(userInput);
                chatArea.appendText("Chatbot: " + aiResponse + "\n\n");
            } catch (Exception ex) {
                chatArea.appendText("Chatbot: Sorry, I encountered an error while processing your request.\n\n");
                ex.printStackTrace();
            }

            inputField.clear();
        }
    }

    private String getAIResponse(String userInput) {
        HttpResponse<JsonNode> response = Unirest.post(GEMINI_API_ENDPOINT)
                .header("Content-Type", "application/json")
                .queryString("key", GOOGLE_API_KEY)
                .body("{\"contents\":[{\"parts\":[{\"text\":\"" + userInput + "\"}]}]}")
                .asJson();

        String aiResponse = response.getBody().getObject().getJSONArray("candidates")
                .getJSONObject(0).getJSONObject("content")
                .getJSONArray("parts").getJSONObject(0)
                .getString("text");

        return aiResponse;
    }

    public static void main(String[] args) {
        launch(args);
    }
}