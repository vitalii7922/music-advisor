package advisor;

import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller {
    static final String CLIENT_ID = "c272e24c020d428f848594eea7f5199d";
    static final String CLIENT_SECRET = "583af1b361fb47598bd14c9f1fdf386c";
    private final HttpClient client = HttpClient.newBuilder().build();
    private boolean access = false;
    private final Map<String, String> categoriesId = new HashMap<>();
    private String accessToken;
    private List<String> output;

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

    void getNewReleases(String resourceServer) throws IOException, InterruptedException {
        JsonObject jo = JsonParser.parseString(sendGetRequest("new-releases", resourceServer)).getAsJsonObject();
        JsonObject albums = jo.getAsJsonObject("albums");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray albumArray = albums.getAsJsonArray("items");
//        System.out.println(gson.toJson(album));
        List<JsonElement> result = IntStream.range(0, albumArray.size())
                .mapToObj(albumArray::get)
                .collect(Collectors.toList());
//        result.stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList()).forEach(System.out::println);
//        result.forEach(System.out::println);
        List<String> musiciansNames = new ArrayList<>();
        for (JsonElement album : albumArray.getAsJsonArray()) {
            System.out.println(album.getAsJsonObject().get("name").getAsString());
            for (JsonElement artist : album.getAsJsonObject().getAsJsonArray("artists")) {
//                System.out.println("[" + x.getAsJsonObject().get("name").getAsString() + "]");
                musiciansNames.add(artist.getAsJsonObject().get("name").getAsString());
            }
            System.out.println(musiciansNames);
            musiciansNames.clear();
            System.out.println(album.getAsJsonObject().get("external_urls")
                    .getAsJsonObject().get("spotify").getAsString());
            System.out.println();
        }
    }

    void getFeatured(String resourceServer) throws IOException, InterruptedException {
        JsonObject jo = JsonParser.parseString(sendGetRequest("featured-playlists", resourceServer)).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("playlists");
        for (JsonElement playlist : playlists.getAsJsonArray("items")) {
            System.out.println(playlist.getAsJsonObject().get("name").getAsString());
            System.out.println(playlist.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString());
            System.out.println();
        }
    }

    List<String> getCategories(String resourceServer) throws IOException, InterruptedException {
        saveCategories(resourceServer);
        return new ArrayList<>(categoriesId.keySet());
    }

    void saveCategories(String resourceServer) throws IOException, InterruptedException {
        JsonObject jo = JsonParser.parseString(sendGetRequest("categories", resourceServer)).getAsJsonObject();
        JsonObject categories = jo.getAsJsonObject("categories");
        for (JsonElement category : categories.getAsJsonArray("items")) {
            String categoryName = category.getAsJsonObject().get("name").getAsString();
            categoriesId.put(categoryName, category.getAsJsonObject().get("id").getAsString());
        }
    }

    void getPlaylists(String categoryName, String resourceServer) throws IOException, InterruptedException {
        saveCategories(resourceServer);
        String categoryId;
        if (categoriesId.containsKey(categoryName)) {
            categoryId = categoriesId.get(categoryName);
        } else {
            System.out.println("Unknown category name.");
            return;
        }
        JsonObject jo = JsonParser
                .parseString(sendGetRequest("categories/" + categoryId + "/playlists", resourceServer))
                .getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("playlists");
        if (playlists != null) {
            for (JsonElement x : playlists.getAsJsonArray("items")) {
                System.out.println(x.getAsJsonObject().get("name").getAsString());
                System.out.println(x.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString());
                System.out.println();
            }
        } else {
            System.out.println(jo.getAsJsonObject("error").get("message").getAsString());
        }
    }

    String sendGetRequest(String endpoint, String resource) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + getAccessToken())
                .uri(URI.create(resource + "/v1/browse/" + endpoint))
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
}
