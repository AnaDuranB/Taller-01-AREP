package org.example;

import java.net.*;
import java.io.*;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(35000);
        System.out.println("Servidor HTTP iniciado en el puerto 35000...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            String requestedFile = "/index.html";
            String method = "GET";
            boolean isFirstLine = true;

            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    String[] requestParts = inputLine.split(" ");
                    if (requestParts.length > 1) {
                        method = requestParts[0];  // Captura el método (GET o POST)
                        requestedFile = requestParts[1];
                    }
                    isFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            System.out.println("Solicitud recibida: " + method + " " + requestedFile);

            if (requestedFile.startsWith("/api")) {
                handleRestApi(inputStream, outputStream, requestedFile, method);
            } else {
                serveFile(outputStream, requestedFile);
            }

            outputStream.close();
            in.close();
            clientSocket.close();
        }
    }

    private static void handleRestApi(InputStream inputStream, OutputStream outputStream, String path, String method) throws IOException {
        String response;

        if (path.startsWith("/api/hello")) {
            if (method.equals("POST")) {
                // Leer el cuerpo de la solicitud
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder body = new StringBuilder();
                String line;

                // Leer las cabeceras y luego el cuerpo
                boolean isBody = false;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) { // Línea vacía separa las cabeceras del cuerpo
                        isBody = true;
                        continue;
                    }
                    if (isBody) {
                        body.append(line);
                    }
                }

                // Depurar: Imprimir el cuerpo recibido
                System.out.println("Cuerpo recibido en POST: " + body.toString());

                // Construir respuesta JSON
                response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        "{\"response\": \"Datos recibidos correctamente: " + body.toString() + "\"}";
            } else {
                // Respuesta para métodos GET
                response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json\r\n\r\n" +
                        "{\"message\": \"Hola desde el servidor Java\"}";
            }
        } else {
            response = "HTTP/1.1 404 Not Found\r\n\r\nAPI No Encontrada";
        }

        outputStream.write(response.getBytes());
    }




    private static void serveFile(OutputStream outputStream, String filePath) throws IOException {
        File file = new File("src/main/webapp" + filePath);
        if (!file.exists()) {
            String errorResponse = "HTTP/1.1 404 Not Found\r\n\r\nArchivo No Encontrado";
            outputStream.write(errorResponse.getBytes());
            return;
        }

        String headers = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + getMimeType(filePath) + "\r\n" +
                "Content-Length: " + file.length() + "\r\n\r\n";
        outputStream.write(headers.getBytes());

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
