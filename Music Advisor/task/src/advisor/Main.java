package advisor;

import advisor.view.View;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String accessServer;
        String resourceServer;
        int elementsNumber;
        if (args.length != 0 && args[0].contains("-access") && args[2].contains("-resource") && args[4].contains("-page")) {
            accessServer = args[1];
            resourceServer = args[3];
            elementsNumber = Integer.parseInt(args[5]);
        } else {
            accessServer = "https://accounts.spotify.com";
            resourceServer = "https://api.spotify.com";
            elementsNumber = 5;
        }
        View view = new View(accessServer, resourceServer, elementsNumber);
        view.render();
    }
}
