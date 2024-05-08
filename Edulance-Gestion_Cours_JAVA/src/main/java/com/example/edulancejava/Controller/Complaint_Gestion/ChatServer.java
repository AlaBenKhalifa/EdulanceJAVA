package com.example.edulancejava.Controller.Complaint_Gestion;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private static final int PORT = 8001;

    private DatagramSocket socket;
    private List<ClientInfo> clients = new ArrayList<>();

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }

    public void start() {
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("Server started on port " + PORT);
            receiveMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Server received: " + message);

                if (message.startsWith("init;")) {
                    int userId = Integer.parseInt(message.substring(5));
                    ClientInfo client = new ClientInfo(userId, packet.getAddress(), packet.getPort());
                    clients.add(client);
                } else {
                    sendMessageToAllClients(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToAllClients(String message) throws IOException {
        byte[] data = message.getBytes();
        for (ClientInfo client : clients) {
            DatagramPacket packet = new DatagramPacket(data, data.length, client.getAddress(), client.getPort());
            socket.send(packet);
        }
    }

    private static class ClientInfo {
        private int userId;
        private InetAddress address;
        private int port;

        public ClientInfo(int userId, InetAddress address, int port) {
            this.userId = userId;
            this.address = address;
            this.port = port;
        }

        public int getUserId() {
            return userId;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }
    }
}

