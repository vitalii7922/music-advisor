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
        Advice advice = new Advice();
        while (true) {
            switch (reader.readLine()) {
                case "new":
                    if (Server.isAccess()) {
                        System.out.println("---NEW RELEASES---");
                        advice.getNewReleases().forEach(System.out::println);
                    } else {
                        System.out.println(DENY_ACCESS);
                        ;
                    }
                    break;
                case "featured":
                    if (Server.isAccess()) {
                        System.out.println("---FEATURED---");
                        advice.getFeatured().forEach(System.out::println);
                    } else {
                        System.out.println(DENY_ACCESS);
                    }
                    break;
                case "categories":
                    System.out.println("---CATEGORIES---");
                    if (Server.isAccess()) {
                        advice.getCategories().forEach(System.out::println);
                    } else {
                        System.out.println(DENY_ACCESS);
                        ;
                    }
                    break;
                case "playlists Mood":
                    if (Server.isAccess()) {
                        System.out.println("---MOOD PLAYLISTS---");
                        advice.getCategories().forEach(System.out::println);
                    } else {
                        System.out.println(DENY_ACCESS);
                        ;
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
