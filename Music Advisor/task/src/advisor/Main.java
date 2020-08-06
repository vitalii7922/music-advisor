package advisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    private static final String DENY_ACCESS = "Please, provide access for application.";
    private static String accessServer = null;
    private static String resourceServer = null;

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (args.length != 0 && args[0].contains("-access")) {
            accessServer = args[1];
        } else if (args.length != 0 && args[0].contains("-resource")){
            resourceServer = args[1];
        } else {
            accessServer = "https://accounts.spotify.com";
            resourceServer = "https://api.spotify.com";
        }
        while (true) {
            String option = reader.readLine();
            switch (option) {
                case "new":
                    if (Server.isAccess()) {
                        Server.getNewReleases(resourceServer);
                    } else {
                        System.out.println(DENY_ACCESS);
                    }
                    break;
                case "featured":
                    if (Server.isAccess()) {
                        Server.getFeatured(resourceServer);
                    } else {
                        System.out.println(DENY_ACCESS);
                    }
                    break;
                case "categories":
                    if (Server.isAccess()) {
                        Server.getCategories(resourceServer);
                    } else {
                        System.out.println(DENY_ACCESS);
                    }
                    break;
                case "auth":
                    doAuthentication();
                    Server.accessToken(accessServer);
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    return;
                default:
                    if (option.startsWith("playlists") && Server.isAccess()) {
                        String[] command = option.split("\\s+");
                        if (command.length == 2) {
                            Server.getPlaylists(option.split("\\s+")[1], resourceServer);
                        }
                    } else {
                        System.out.println(DENY_ACCESS);
                    }
                    break;
            }
        }
    }


    private static void doAuthentication() throws IOException {
        String URL = accessServer + "/authorize?" +
                "client_id=c272e24c020d428f848594eea7f5199d&" +
                "redirect_uri=http://localhost:8080&response_type=code";
        System.out.printf("use this link to request the access code:%n%s%n", URL);
        System.out.println("waiting for code...");
        Server.createAndStartServer();
    }
}
