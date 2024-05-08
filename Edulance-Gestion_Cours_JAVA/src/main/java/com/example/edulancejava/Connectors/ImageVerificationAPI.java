package com.example.edulancejava.Connectors;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageVerificationAPI {

    public boolean sendRequest(String s) throws IOException {
        //File file = new File("C:\\Users\\alaed\\IdeaProjects\\Edulance\\src\\main\\resources\\com\\example\\edulance\\Images\\422062543_3754449101545315_7941869759346075097_n.jpg");
        //File file = new File("C:\\Users\\alaed\\Downloads\\download (1).jpg");
        File file = new File(s);
        String url = "https://api.sightengine.com/1.0/check-workflow.json";
        String apiUser = "466759344";
        String apiSecret = "3k7oUV6CvFNPysFRfBkmn5osfBwAPBgX";
        String workflow = "wfl_fH6gCavJhLXk6wPX4aSTX";
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream outputStream = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))) {

            // Add media
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"media\"; filename=\"" + file.getName() + "\"").append("\r\n");
            writer.append("Content-Type: image/jpeg").append("\r\n");
            writer.append("\r\n");
            writer.flush();

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }

            writer.append("\r\n").flush();

            // Add workflow
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"workflow\"").append("\r\n");
            writer.append("\r\n");
            writer.append(workflow).append("\r\n");

            // Add api_user
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"api_user\"").append("\r\n");
            writer.append("\r\n");
            writer.append(apiUser).append("\r\n");

            // Add api_secret
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"api_secret\"").append("\r\n");
            writer.append("\r\n");
            writer.append(apiSecret).append("\r\n");

            // End boundary
            writer.append("--" + boundary + "--").append("\r\n");
            writer.flush();
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println(parseResponse(response.toString()));
            System.out.println(response.toString());
            connection.disconnect();
            return parseResponse(response.toString());
        }


    }

    public boolean parseResponse(String response) {

        int Index = response.indexOf("\"reject\"");
        if (Index != -1) {
            return false;
        }
        else{
            return true;
        }
    }
}
