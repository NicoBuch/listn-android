package ar.com.wolox.android.model;

/**
 * Created by nicolasbuchhalter on 02/08/15.
 */
public class UserUpdate {
    String name;
    double longitude;
    double latitude;
    Track track;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
