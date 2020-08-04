package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Server {
    //    private static HttpServer server = null;
    private static Server server;
    private static String code = "";

    private Server() {
    }

    static void createAndStartServer(String serverUrl) throws IOException {
        if (server == null) {
            int index = serverUrl.lastIndexOf(':') + 1;
            HttpServer httpServer = null;
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(Integer.parseInt(serverUrl.substring(index))), 0);
            httpServer.start();
            server = new Server();
            HttpServer finalServer = httpServer;
            httpServer.createContext("/",
                    exchange -> {
                        String query = exchange.getRequestURI().getQuery();
                        if (query != null && query.contains("code")) {
                            code = query;
                            try {
                                accessToken(serverUrl);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            renderMessage("Got the code. Return back to your program.", exchange);
                        } else {
                            renderMessage("Not found authorization code. Try again.", exchange);
                        }
                    }
            );
            while (code.equals("")) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            finalServer.stop(1);
        }
    }

/*    static void startServer(String serverUrl) {
        if (server != null) {
            server.start();
        }
    }*/

    private static void renderMessage(String message, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, message.length());
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
    }

    static void accessToken(String serverUrl) throws IOException, InterruptedException {
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
                                code +
                                "&redirect_uri=" + serverUrl))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response:\n" + response.body());
    }
}
