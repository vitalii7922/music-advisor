package advisor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Advice {
    private List<String> newReleases = Arrays.asList("Mountains [Sia, Diplo, Labrinth]",
            "Runaway [Lil Peep]",
            "The Greatest Show [Panic! At The Disco]",
            "All Out Life [Slipknot]");

    private List<String> featured = Arrays.asList("Mellow Morning",
            "Wake Up and Smell the Coffee",
            "Monday Motivation",
            "Songs to Sing in the Shower");

    private List<String> categories = Arrays.asList("Top Lists",
            "Pop",
            "Mood",
            "Latin");

    private List<String> moodPlaylists = Arrays.asList("Walk Like A Badass",
            "Rage Beats",
            "Arab Mood Booster",
            "Sunday Stroll");


    public List<String> getNewReleases() {
        return newReleases;
    }

    public void setNewReleases(List<String> newReleases) {
        this.newReleases = newReleases;
    }

    public List<String> getFeatured() {
        return featured;
    }

    public void setFeatured(List<String> featured) {
        this.featured = featured;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getMoodPlaylists() {
        return moodPlaylists;
    }

    public void setMoodPlaylists(List<String> moodPlaylists) {
        this.moodPlaylists = moodPlaylists;
    }
}
