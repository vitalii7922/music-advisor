# Music advisor
Music advisor is an application based on Spotify API. Using this application you can get lists of featured music, new releases, 
etc. via Spotify API.<br/>
This application utilizes OAuth for getting access from Spotify API access server. 
The main entity of OAuth is the token, the secret code that should be sent with an HTTP request to API, 
so that the service is sure that you have enough rights to get information from API. <br/>
Before starting the application you should go to the https://developer.spotify.com and create your application.
To create an application:
1. you should select Dashboard tab on the site, log in to Spotify, and click the button Create an App.
Specify redirect_uri as http://localhost:8080<br/>
When application is created you get client_id and client_secret.<br/>
2. create SpotifyData.class in task/src/data package with fields CLIENT_ID, CLIENT_SECRET and getters respectively.<br/>

Example:
```Java
public class SpotifyData {
    private static final String CLIENT_ID = "aaaaaa9999123124njndskjg324";
    private static final String CLIENT_SECRET = "asdausih324235njknk34543";

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getClientSecret() {
        return CLIENT_SECRET;
    }
}
```
## Description
Spotify API is based on REST principles.

Console commands | URL|Description |
------------ | ----|-------------
featured  | https://api.spotify.com/v1/browse/featured-playlists|list of Spotify featured playlists with their links fetched from API
new | https://api.spotify.com/v1/browse/new-releases| list of new albums with artists and links on Spotify
categories  | https://api.spotify.com/v1/browse/categories| list of all available categories on Spotify (just their names)
playlists C_NAME  | https://api.spotify.com/v1/browse/categories/{category_id}/playlists| where C_NAME — name of category. List contains playlists of this category and their links on Spotify

Used technologies
- JDK v. 11.0.4

## Run music advisor
gradle run
## Examples
- Log in Spotify API with command __auth__.<br/>
Clicking the link a client will be redirected to Spotify application and suggested authorization. After logging in a client 
will get especial code for getting token which will be used for requests to API. 
![1](https://user-images.githubusercontent.com/51421459/98034293-d15ca680-1e27-11eb-9945-72c31affde56.jpg)
- Use commands from __description__ section to interact with API<br/>
To turn pages use commands __prev__ and __next__
![3](https://user-images.githubusercontent.com/51421459/98037663-fbfd2e00-1e2c-11eb-8249-48150e6598f2.jpg)