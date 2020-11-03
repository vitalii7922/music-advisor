package advisor.view;

import advisor.controller.Controller;
import advisor.data.SpotifyData;
import advisor.pagination.PageTurner;
import advisor.pagination.TurningPages;
import advisor.server.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * render menu for a client
 */
public class View {
    private Controller controller = new Controller(); //Controller layer
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

    /**
     * shows menu options
     *
     * @throws IOException
     * @throws InterruptedException
     */
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
                default:
                    if (option.startsWith("playlists")) {
                        takePlaylists(option);
                    }
                    break;
            }
        }
    }

    /**
     * get new releases from spotify API and open first page
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void takeNewReleases() throws IOException, InterruptedException {
        if (controller.isAccess()) {
            output = controller.getNewReleases();
            pageTurner.setTurningMethods(new TurningPages(elementsNumber, output));
            pageTurner.turnPageForward();
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    /**
     * get featured music from spotify API and open first page if a client got a token from spotify API access server
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void takeFeatured() throws IOException, InterruptedException {
        if (controller.isAccess()) {
            output = controller.getFeatured();
            pageTurner.setTurningMethods(new TurningPages(elementsNumber, output));
            pageTurner.turnPageForward();
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    /**
     *
     * get categories of music from spotify API and open first page if a client got a token from spotify API access server
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void takeCategories() throws IOException, InterruptedException {
        if (controller.isAccess()) {
            output = controller.getCategories();
            pageTurner.setTurningMethods(new TurningPages(elementsNumber, output));
            pageTurner.turnPageForward();
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    /**
     * get a playlist according to categories from spotify API and open first page
     * if a client got a token from spotify API access server
     *
     * @param option
     * @throws IOException
     * @throws InterruptedException
     */
    private void takePlaylists(String option) throws IOException, InterruptedException {
        if (controller.isAccess()) {
            String[] command = option.split("\\s+");
            if (command.length > 1) {
                output = controller.getPlaylists(option.substring(command[0].length() + 1));
                pageTurner.setTurningMethods(new TurningPages(elementsNumber, output));
                pageTurner.turnPageForward();
            }
        } else {
            System.out.println(DENY_ACCESS);
        }
    }

    /**
     * authorization on Spotify API
     *
     * @param accessServer Spotify API access server URL
     * @return code for getting token
     * @throws IOException
     */
    private String doAuthentication(String accessServer) throws IOException {
        String url = accessServer + "/authorize?" +
                "client_id=" + SpotifyData.getClientId() +
                "&redirect_uri=http://localhost:8080&response_type=code";
        System.out.printf("use this link to request the access code:%n%s%n", url);
        System.out.println("waiting for code...");
        return Server.createAndStartServer();
    }
}
