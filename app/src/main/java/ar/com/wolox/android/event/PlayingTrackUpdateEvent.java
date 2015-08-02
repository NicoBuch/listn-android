package ar.com.wolox.android.event;

/**
 * Created by nicolasbuchhalter on 01/08/15.
 */
public class PlayingTrackUpdateEvent {


    private final String artist;
    private final String track;
    private final boolean playing;

    public PlayingTrackUpdateEvent(String artist, String track, boolean playing){
        this.artist = artist;
        this.track = track;
        this.playing = playing;
    }

    public String getArtist() {
        return artist;
    }

    public boolean isPlaying() {
        return playing;
    }

    public String getTrack() {
        return track;
    }
}
