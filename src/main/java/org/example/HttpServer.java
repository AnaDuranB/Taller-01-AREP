package org.example;

import java.net.*;
import java.io.*;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(35000);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            String requestedFile = "/index.html";
            boolean isFirstLine = true;

            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    String[] requestParts = inputLine.split(" ");
                    if (requestParts.length > 1) {
                        requestedFile = requestParts[1];
                    }
                    isFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            // Manejo de la solicitud
            if (requestedFile.startsWith("/api")) {
                handleRestApi(outputStream, requestedFile);
            } else {
                serveFile(outputStream, requestedFile);
            }

            outputStream.close();
            in.close();
            clientSocket.close();
        }
    }

    private static void handleRestApi(OutputStream outputStream, String path) throws IOException {
        String response;
        if (path.startsWith("/api/hello")) {
            response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n\r\n" +
                    "{\"message\": \"Hello, World!\"}";
        } else {
            response = "HTTP/1.1 404 Not Found\r\n\r\nAPI Not Found";
        }
        outputStream.write(response.getBytes());
    }

    private static void serveFile(OutputStream outputStream, String filePath) throws IOException {
        File file = new File("src/main/webapp" + filePath);
        if (!file.exists()) {
            String errorResponse = "HTTP/1.1 404 Not Found\r\n\r\nFile Not Found";
            outputStream.write(errorResponse.getBytes());
            return;
        }

        // Enviar encabezados HTTP
        String headers = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + getMimeType(filePath) + "\r\n" +
                "Content-Length: " + file.length() + "\r\n\r\n";
        outputStream.write(headers.getBytes());

        // Enviar contenido del archivo (binario o texto)
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    private static String getMimeType(String filePath) {
        if (filePath.endsWith(".html")) return "text/html";
        if (filePath.endsWith(".css")) return "text/css";
        if (filePath.endsWith(".js")) return "application/javascript";
        if (filePath.endsWith(".png")) return "image/png";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) return "image/jpeg";
        if (filePath.endsWith(".gif")) return "image/gif";
        if (filePath.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}
