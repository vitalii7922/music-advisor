package advisor;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        View view = new View();
        String accessServer = null;
        String resourceServer = null;
        if (args.length != 0 && args[0].contains("-access") && args[2].contains("-resource")) {
            accessServer = args[1];
            resourceServer = args[3];
        } else {
            accessServer = "https://accounts.spotify.com";
            resourceServer = "https://api.spotify.com";
        }
        view.render(accessServer, resourceServer);
    }
}
