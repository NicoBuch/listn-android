package ar.com.wolox.android.model;

/**
 * Created by nicolasbuchhalter on 02/08/15.
 */
public class Track {

    String name;
    String artist;
    String spotifyId;
    boolean playing;

    public void setId(String id){
        this.spotifyId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getId() {
        return spotifyId;
    }
}
