package advisor;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String accessServer;
        String resourceServer;
        if (args.length != 0 && args[0].contains("-access") && args[2].contains("-resource")) {
            accessServer = args[1];
            resourceServer = args[3];
        } else {
            accessServer = "https://accounts.spotify.com";
            resourceServer = "https://api.spotify.com";
        }
        View view = new View(accessServer, resourceServer, 5);
        view.render();
    }
}
