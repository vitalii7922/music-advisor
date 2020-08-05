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
    static final String CLIENT_ID = "c272e24c020d428f848594eea7f5199d";
    static final String CLIENT_SECRET = "583af1b361fb47598bd14c9f1fdf386c";
    private static Server server;
    private static String code = "";
    private static boolean access = false;
    private static String accessToken;

    private Server() {
    }

    static void createAndStartServer() throws IOException {
        if (server == null) {
            HttpServer httpServer = null;
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(8080), 0);
            httpServer.start();
            server = new Server();
            httpServer.createContext("/",
                    exchange -> {
                        String query = exchange.getRequestURI().getQuery();
                        if (query != null && query.contains("code")) {
                            code = query;
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
            httpServer.stop(1);
        }
    }

    private static void renderMessage(String message, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, message.length());
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
    }

    static void accessToken(String accessServer) throws IOException, InterruptedException {
        System.out.println("code received");
        System.out.println("making http request for access_token...");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(accessServer + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + CLIENT_ID +
                                "&client_secret=" + CLIENT_SECRET +
                                "&grant_type=authorization_code&" +
                                code +
                                "&redirect_uri=http://localhost:8080"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response:\n" + response.body());
        setAccess(true);
        System.out.println("Success!");
    }

    public static boolean isAccess() {
        return access;
    }

    public static void setAccess(boolean access) {
        Server.access = access;
    }
}
