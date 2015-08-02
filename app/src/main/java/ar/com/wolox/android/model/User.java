package ar.com.wolox.android.model;

import java.util.List;

/**
 * Created by nicolasbuchhalter on 01/08/15.
 */
public class User {
    String spotifyId;
    String distance;
    String name;
    Track track;
    int likes;

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public Track getTrack() {
        return track;
    }

    public int getLikes() {
        return likes;
    }

    public String getSpotifyId() { return spotifyId; }
}
