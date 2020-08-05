package advisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final String DENY_ACCESS = "Please, provide access for application.";
    private static String accessServer = null;

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (args.length != 0 && args[0].contains("-access")) {
            accessServer = args[1];
        } else {
            accessServer = "https://accounts.spotify.com";
        }
        while (true) {
            String option = reader.readLine();
            switch (option) {
                case "new":
                    if (Server.isAccess()) {
                        Server.getNewReleases();
                    } else {
                        System.out.println(DENY_ACCESS);
                    }
                    break;
                case "featured":
                    if (Server.isAccess()) {
                        Server.getFeatured();
                    } else {
                        System.out.println(DENY_ACCESS);
                    }
                    break;
                case "categories":
                    System.out.println("---CATEGORIES---");
                    if (Server.isAccess()) {
                        Server.getCategories();
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
                        Server.getPlaylists(option.split("\\s+")[1]);
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
