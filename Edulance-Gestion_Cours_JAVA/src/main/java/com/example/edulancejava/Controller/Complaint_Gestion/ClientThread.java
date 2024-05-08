package com.example.edulancejava.Controller.Complaint_Gestion;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientThread extends Thread {
    private final DatagramSocket socket;
    private final TextArea messageArea;
    private final String identifier;
    private final TextArea otherMessageArea;

    public ClientThread(DatagramSocket socket, TextArea messageArea, String identifier, TextArea otherMessageArea) {
        this.socket = socket;
        this.messageArea = messageArea;
        this.identifier = identifier;
        this.otherMessageArea = otherMessageArea;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Client received: " + message);

                updateMessageArea(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMessageArea(String message) {
        // Extract sender's identifier and message from the received message
        String[] parts = message.split(":");
        String sender = parts[0].trim();
        String actualMessage = parts[1].trim();

        // Append message to TextArea
        // Update UI on JavaFX Application Thread
        Platform.runLater(() -> {
            // Display the message in the sender's message area
            if (sender.equals(identifier)) {
                messageArea.appendText(actualMessage + "\n");
            } else {
                // Display the message in the other user's message area
                otherMessageArea.appendText(actualMessage + "\n");
            }
        });
    }
}
