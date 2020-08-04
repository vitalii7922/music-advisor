package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Server {
    private static HttpServer server = null;

    private Server() {

    }

    static {
        server = createServer();
    }

    private static HttpServer createServer() {
        HttpServer server = null;
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            HttpServer finalServer = server;
            server.createContext("/",
                    exchange -> {
                        try {
                            String query = exchange.getRequestURI().getQuery();
                            if (!query.equals("error=access_denied")) {
                                printMessage("Got the code. Return back to your program.", exchange);
                                processRequest(exchange.getRequestURI().getQuery());
                                finalServer.stop(1);
                            } else {
                                printMessage("Authorization code not found. Try again.", exchange);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return server;
    }

    static void startServer() {
        if (server != null) {
            server.start();
        }
    }

    private static void printMessage(String message, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, message.length());
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
    }

    private static void processRequest(String parameter) throws IOException, InterruptedException {
        System.out.println("code received");

        System.out.println("making http request for access_token...");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + Config.CLIENT_ID +
                                "&client_secret=" + Config.CLIENT_SECRET +
                                "&grant_type=authorization_code&" +
                                parameter +
                                "&redirect_uri=http://localhost:8080"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response:\n" + response.body());
    }
}
