package advisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static boolean access = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String serverUrl = args[1];
        Advice advice = new Advice();
        while (true) {
            switch (reader.readLine()) {
                case "new":
                    if (access) {
                        System.out.println("---NEW RELEASES---");
                        advice.getNewReleases().forEach(System.out::println);
                    } else {
                        printMessage();
                    }
                    break;
                case "featured":
                    if (access) {
                        System.out.println("---FEATURED---");
                        advice.getFeatured().forEach(System.out::println);
                    } else {
                        printMessage();
                    }
                    break;
                case "categories":
                    if (access) {
                        System.out.println("---CATEGORIES---");
                        advice.getCategories().forEach(System.out::println);
                    } else {
                        printMessage();
                    }
                    break;
                case "playlists Mood":
                    if (access) {
                        System.out.println("---MOOD PLAYLISTS---");
                        advice.getCategories().forEach(System.out::println);
                    } else {
                        printMessage();
                    }
                    break;
                case "auth":
                    doAuthentication(serverUrl);
//                    Server.accessToken(serverUrl);
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    return;
                default:
                    break;
            }
        }
    }

    private static void printMessage() {
        System.out.println("Please, provide access for application.");
    }


    private static void doAuthentication(String serverUrl) throws InterruptedException, IOException {
        access = true;
        Server.createAndStartServer(serverUrl);
        String URL = "https://accounts.spotify.com/authorize?" +
                "client_id=c272e24c020d428f848594eea7f5199d&" +
                "redirect_uri=" + serverUrl + "&response_type=code";
        System.out.printf("use this link to request the access code:%n%s%n", URL);
        System.out.println("waiting for code...");
//        Thread.sleep(5000);
//        System.out.println("---SUCCESS---");
    }



}
