package advisor;

import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Controller {
    static final String CLIENT_ID = "c272e24c020d428f848594eea7f5199d";
    static final String CLIENT_SECRET = "583af1b361fb47598bd14c9f1fdf386c";
    private final HttpClient client = HttpClient.newBuilder().build();
    private boolean access = false;
    private final Map<String, String> categoriesId = new LinkedHashMap<>();
    private String accessToken;
    private final List<String> output = new ArrayList<>();
    private String resourceServer;

    void accessToken(String accessServer, String code) throws IOException, InterruptedException {
        System.out.println("code received");
        System.out.println("making http request for access_token...");
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(accessServer + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + CLIENT_ID +
                                "&client_secret=" + CLIENT_SECRET +
                                "&grant_type=authorization_code&" +
                                code +
                                "&redirect_uri=http://localhost:8080"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        setAccessToken(JsonParser.parseString(response.body()).getAsJsonObject().get("access_token").getAsString());
        setAccess(true);
        System.out.println("Success!");
    }

    List<String> getNewReleases() throws IOException, InterruptedException {
        JsonObject jo = JsonParser.parseString(sendGetRequest("new-releases")).getAsJsonObject();
        JsonObject albums = jo.getAsJsonObject("albums");
        JsonArray albumArray = albums.getAsJsonArray("items");
        List<String> musiciansNames = new ArrayList<>();
        output.clear();
        for (JsonElement album : albumArray.getAsJsonArray()) {
            for (JsonElement artist : album.getAsJsonObject().getAsJsonArray("artists")) {
                musiciansNames.add(artist.getAsJsonObject().get("name").getAsString());
            }
            output.add(album.getAsJsonObject().get("name").getAsString() + "\n" + musiciansNames.toString() + "\n" +
                    album.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
            musiciansNames.clear();
        }
        return output;
    }

    List<String> getFeatured() throws IOException, InterruptedException {
        JsonObject jo = JsonParser.parseString(sendGetRequest("featured-playlists")).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("playlists");
        output.clear();
        for (JsonElement playlist : playlists.getAsJsonArray("items")) {
            output.add(playlist.getAsJsonObject().get("name").getAsString() + "\n" +
                    playlist.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
        }
        return output;
    }

    List<String> getCategories() throws IOException, InterruptedException {
        saveCategories();
        return new ArrayList<>(categoriesId.keySet());
    }

    void saveCategories() throws IOException, InterruptedException {
        categoriesId.clear();
        JsonObject jo = JsonParser.parseString(sendGetRequest("categories")).getAsJsonObject();
        JsonObject categories = jo.getAsJsonObject("categories");
        for (JsonElement category : categories.getAsJsonArray("items")) {
            String categoryName = category.getAsJsonObject().get("name").getAsString();
            categoriesId.put(categoryName, category.getAsJsonObject().get("id").getAsString());
        }
    }

    List<String> getPlaylists(String categoryName) throws IOException, InterruptedException {
        saveCategories();
        String categoryId;
        output.clear();
        if (categoriesId.containsKey(categoryName)) {
            categoryId = categoriesId.get(categoryName);
        } else {
            System.out.println("Unknown category name.");
            return output;
        }
        JsonObject jo = JsonParser
                .parseString(sendGetRequest("categories/" + categoryId + "/playlists"))
                .getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("playlists");
        if (playlists != null) {
            for (JsonElement x : playlists.getAsJsonArray("items")) {
                output.add(x.getAsJsonObject().get("name").getAsString() + "\n" +
                        x.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n");
            }
        } else {
            System.out.println(jo.getAsJsonObject("error").get("message").getAsString());
        }
        return output;
    }

    String sendGetRequest(String endpoint) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + getAccessToken())
                .uri(URI.create(resourceServer + "/v1/browse/" + endpoint))
                .GET()
                .build();
        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getResourceServer() {
        return resourceServer;
    }

    public void setResourceServer(String resourceServer) {
        this.resourceServer = resourceServer;
    }
}
