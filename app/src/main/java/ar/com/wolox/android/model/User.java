package ar.com.wolox.android.model;

import java.util.List;

/**
 * Created by nicolasbuchhalter on 01/08/15.
 */
public class User {
    String trackUri;
    String spotifyId;
    String distance;
    String name;

    public User(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
