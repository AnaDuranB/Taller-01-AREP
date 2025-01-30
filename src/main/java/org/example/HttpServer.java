package org.example;
import org.example.Component;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServer {
    private static final List<Component> components = new ArrayList<>();

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

        }

    }
    private static void handleRequest(Socket clientSocket) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream out = clientSocket.getOutputStream();

        String requestLine = in.readLine();
        if (requestLine == null) return;

        System.out.println("Solicitud: " + requestLine);
        String[] requestParts = requestLine.split(" ");
        String method = requestParts[0];
        String path = requestParts[1];

        if (path.startsWith("/api/components")){
            handleApiRequest(method, path, in, out);
        } else {
            serveStaticFile(path, out);
        }

        in.close();
        out.close();
        clientSocket.close();
    }

    static void handleApiRequest(String method, String path, BufferedReader in, OutputStream out) throws IOException {
        System.out.println("Método recibido: " + method + ", Ruta: " + path);
        String response;
        if (method.equals("GET")) {
            response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + components;
        } else if (method.equals("POST")) {
            String body = readRequestBody(in);
            System.out.println("Cuerpo recibido: " + body);

            try {
                body = body.trim();
                if (!body.startsWith("{") || !body.endsWith("}")) {
                    throw new IllegalArgumentException("Formato JSON inválido");
                }

                Map<String, String> data = parseJson(body);
                if (data.containsKey("name") && data.containsKey("type") && data.containsKey("price")) {
                    components.add(new Component(data.get("name"), data.get("type"), Double.parseDouble(data.get("price"))));
                    response = "HTTP/1.1 201 Created\r\nContent-Type: text/plain\r\n\r\nComponent added";
                } else {
                    response = "HTTP/1.1 400 Bad Request\r\n\r\nMissing fields";
                }
            } catch (Exception e) {
                response = "HTTP/1.1 400 Bad Request\r\n\r\nInvalid JSON format";
            }
        } else {
            response = "HTTP/1.1 405 Method Not Allowed\r\n\r\n";
        }
        out.write(response.getBytes());
        out.flush();
    }

    private static String readRequestBody(BufferedReader in) throws IOException {
        StringBuilder body = new StringBuilder();
        int contentLength = 0;

        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.substring(15).trim());
            }
        }

        if (contentLength > 0) {
            char[] buffer = new char[contentLength];
            in.read(buffer, 0, contentLength);
            body.append(buffer);
        }

        String jsonBody = body.toString();
        System.out.println("JSON Recibido: " + jsonBody);
        return jsonBody;
    }


    private static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.substring(1, json.length() - 1);
        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");
                map.put(key, value);
            }
        }
        return map;
    }

    private static void serveStaticFile(String path, OutputStream out) throws IOException {

        if (path.equals("/")) path = "/index.html";
        File file = new File("src/main/webapp" + path);
        if (file.exists() && !file.isDirectory()) {
            String contentType = getContentType(path);
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            out.write(("HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\n\r\n").getBytes());
            out.write(fileBytes);
        } else {
            out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
        }
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }
}