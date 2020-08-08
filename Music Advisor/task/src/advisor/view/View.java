package advisor.view;
import advisor.controller.Controller;
import advisor.server.Server;
import advisor.pagination.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class View {
    private final Controller controller = new Controller();
    private static final String DENY_ACCESS = "Please, provide access for application.";
    private String accessServer;
    private String resourceServer;
    private List<String> output;
    private int elementsNumber;
    private final PageTurner pageTurner = new PageTurner();

    public View(String accessServer, String resourceServer, int elementsNumber) {
        this.accessServer = accessServer;
        this.resourceServer = resourceServer;
        this.elementsNumber = elementsNumber;
    }

    public void render() throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        controller.setResourceServer(resourceServer);
        while (true) {
            String option = reader.readLine();
            switch (option) {
                case "new":
                    takeNewReleases();
                    break;
                case "featured":
                    takeFeatured();
                    break;
                case "categories":
                    takeCategories();
                    break;
                case "auth":
                    controller.accessToken(accessServer, doAuthentication(accessServer));
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    return;
                case "next":
                    pageTurner.turnPageForward();
                    break;
                case "prev":
                    pageTurner.turnPageBackward();
                    break;
                case "test":
                    test();
                    break;
                default:
                    if (option.startsWith("playlists")) {
                        takePlaylists(option);
                    }
                    break;
            }
        }
    }

    private void test() {
        List<String> hello = new ArrayList<>();
        hello.add("hello1\n");
        hello.add("hello2\n");
        output = hello;
        pageTurner.setTurningMethods(new TurningPagesCategories(elementsNumber, output));
        pageTurner.turnPageForward();
    }

    private void takeNewReleases() throws IOException, InterruptedException {
        if (controller.isAccess()) {
            output = controller.getNewReleases();
            pageTurner.setTurningMethods(new TurningPagesNew(elementsNumber, output));
            pageTurner.turnPageForward();
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    private void takeFeatured() throws IOException, InterruptedException {
        if (controller.isAccess()) {
            output = controller.getFeatured();
            pageTurner.setTurningMethods(new TurningPagesFeatured(elementsNumber, output));
            pageTurner.turnPageForward();
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    private void takeCategories() throws IOException, InterruptedException {
        if (controller.isAccess()) {
            output = controller.getCategories();
            pageTurner.setTurningMethods(new TurningPagesCategories(elementsNumber, output));
            pageTurner.turnPageForward();
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    private void takePlaylists(String option) throws IOException, InterruptedException {
        if (controller.isAccess()) {
            String[] command = option.split("\\s+");
            if (command.length > 1) {
                output = controller.getPlaylists(option.substring(command[0].length() + 1));
                pageTurner.setTurningMethods(new TurningPagesPlaylists(elementsNumber, output));
                pageTurner.turnPageForward();
            }
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    private String doAuthentication(String accessServer) throws IOException {
        String url = accessServer + "/authorize?" +
                "client_id=c272e24c020d428f848594eea7f5199d&" +
                "redirect_uri=http://localhost:8080&response_type=code";
        System.out.printf("use this link to request the access code:%n%s%n", url);
        System.out.println("waiting for code...");
        return Server.createAndStartServer();
    }
}
