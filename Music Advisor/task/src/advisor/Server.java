package advisor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private static Server server;
    private static String code = "";

    private Server() {
    }

    static String createAndStartServer(Controller controller, String accessServer) throws IOException {
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
        return code;
    }

    private static void renderMessage(String message, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, message.length());
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
    }
}
