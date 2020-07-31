package advisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Advice advice = new Advice();
        while (true) {
            switch (reader.readLine()) {
                case "new":
                    System.out.println("---NEW RELEASES---");
                    advice.getNewReleases().forEach(System.out::println);
                    break;
                case "featured":
                    System.out.println("---FEATURED---");
                    advice.getFeatured().forEach(System.out::println);
                    break;
                case "categories":
                    System.out.println("---CATEGORIES---");
                    advice.getCategories().forEach(System.out::println);
                    break;
                case "playlists Mood":
                    System.out.println("---MOOD PLAYLISTS---");
                    advice.getCategories().forEach(System.out::println);
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    return;
                default:
                    break;
            }
        }
    }
}
