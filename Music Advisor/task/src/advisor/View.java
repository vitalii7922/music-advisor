package advisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class View {
    private final Controller controller = new Controller();
    private static final String DENY_ACCESS = "Please, provide access for application.";

    public void render(String accessServer, String resourceServer) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String option = reader.readLine();
            switch (option) {
                case "new":
                    takeNewReleases(resourceServer);
                    break;
                case "featured":
                    takeFeatured(resourceServer);
                    break;
                case "categories":
                    takeCategories(resourceServer);
                    break;
                case "auth":
                    controller.accessToken(accessServer, doAuthentication(accessServer));
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    return;
                default:
                    takePlaylists(resourceServer, option);
                    break;
            }
        }
    }

    private void takeNewReleases(String resourceServer) throws IOException, InterruptedException {
        if (controller.isAccess()) {
            controller.getNewReleases(resourceServer);
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    private void takeFeatured(String resourceServer) throws IOException, InterruptedException {
        if (controller.isAccess()) {
            controller.getFeatured(resourceServer);
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    private void takeCategories(String resourceServer) throws IOException, InterruptedException {
        if (controller.isAccess()) {
            controller.getFeatured(resourceServer);
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    private void takePlaylists(String resourceServer, String option) throws IOException, InterruptedException {
        if (option.startsWith("playlists") && controller.isAccess()) {
            String[] command = option.split("\\s+");
            if (command.length > 1) {
                controller.getPlaylists(option.substring(command[0].length() + 1), resourceServer);
            }
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    private String doAuthentication(String accessServer) throws IOException {
        String URL = accessServer + "/authorize?" +
                "client_id=c272e24c020d428f848594eea7f5199d&" +
                "redirect_uri=http://localhost:8080&response_type=code";
        System.out.printf("use this link to request the access code:%n%s%n", URL);
        System.out.println("waiting for code...");
        return Server.createAndStartServer(controller, accessServer);
    }
}
