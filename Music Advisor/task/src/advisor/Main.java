package advisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static boolean access = false;
    private static final String URL = "https://accounts.spotify.com/authorize?" +
            "client_id=c272e24c020d428f848594eea7f5199d&" +
            "redirect_uri=http://localhost:8080&response_type=code";
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
                    authentication();
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


    private static void authentication() {
            access = true;
            System.out.println(URL);
            System.out.println("---SUCCESS---");
    }
}
