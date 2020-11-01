package advisor.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private static final String CLIENT_ID = "c272e24c020d428f848594eea7f5199d";
    private static final String CLIENT_SECRET = "583af1b361fb47598bd14c9f1fdf386c";
    private final HttpClient client = HttpClient.newBuilder().build();
    private boolean access = false;
    private final Map<String, String> categoriesId = new LinkedHashMap<>();
    private String accessToken;
    private final List<String> output = new ArrayList<>();
    private String resourceServer;
    private static final String ITEMS = "items";
    private static final String EXTERNAL_URLS = "external_urls";
    private static final String SPOTIFY = "spotify";

    public void accessToken(String accessServer, String code) throws IOException, InterruptedException {
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
        setAccess();
        System.out.println("Success!");
    }

    public List<String> getNewReleases() throws IOException, InterruptedException {
        JsonArray albumArray = JsonParser.parseString(sendGetRequest("new-releases"))
                .getAsJsonObject().getAsJsonObject("albums")
                .getAsJsonArray(ITEMS);
        List<String> musiciansNames = new ArrayList<>();
        output.clear();
        for (JsonElement album : albumArray.getAsJsonArray()) {
            for (JsonElement artist : album.getAsJsonObject().getAsJsonArray("artists")) {
                musiciansNames.add(artist.getAsJsonObject().get("name").getAsString());
            }
            output.add(album.getAsJsonObject().get("name").getAsString() + "\n" + musiciansNames.toString() + "\n" +
                    album.getAsJsonObject().get(EXTERNAL_URLS).getAsJsonObject().get(SPOTIFY).getAsString() + "\n");
            musiciansNames.clear();
        }
        return output;
    }

    public List<String> getFeatured() throws IOException, InterruptedException {
        JsonObject playlists = JsonParser.parseString(sendGetRequest("featured-playlists"))
                .getAsJsonObject()
                .getAsJsonObject("playlists");
        output.clear();
        for (JsonElement playlist : playlists.getAsJsonArray(ITEMS)) {
            output.add(playlist.getAsJsonObject().get("name").getAsString() + "\n" +
                    playlist.getAsJsonObject().get(EXTERNAL_URLS).getAsJsonObject().get(SPOTIFY).getAsString() + "\n");
        }
        return output;
    }

    public List<String> getCategories() throws IOException, InterruptedException {
        saveCategories();
        return new ArrayList<>(categoriesId.keySet());
    }

    private void saveCategories() throws IOException, InterruptedException {
        categoriesId.clear();
        JsonObject categories = JsonParser.parseString(sendGetRequest("categories"))
                .getAsJsonObject().getAsJsonObject("categories");
        for (JsonElement category : categories.getAsJsonArray(ITEMS)) {
            String categoryName = category.getAsJsonObject().get("name").getAsString();
            categoriesId.put(categoryName, category.getAsJsonObject().get("id").getAsString());
        }
    }

    public List<String> getPlaylists(String categoryName) throws IOException, InterruptedException {
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
            for (JsonElement x : playlists.getAsJsonArray(ITEMS)) {
                output.add(x.getAsJsonObject().get("name").getAsString() + "\n" +
                        x.getAsJsonObject().get(EXTERNAL_URLS).getAsJsonObject().get(SPOTIFY).getAsString() + "\n");
            }
        } else {
            System.out.println(jo.getAsJsonObject("error").get("message").getAsString());
        }
        return output;
    }

    private String sendGetRequest(String endpoint) throws IOException, InterruptedException {
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

    private void setAccess() {
        this.access = true;
    }

    private String getAccessToken() {
        return accessToken;
    }

    private void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getResourceServer() {
        return resourceServer;
    }

    public void setResourceServer(String resourceServer) {
        this.resourceServer = resourceServer;
    }
}
