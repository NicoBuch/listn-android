package ar.com.wolox.android.utils;

import kaaes.spotify.webapi.android.SpotifyApi;

/**
 * Created by nicolasbuchhalter on 01/08/15.
 */
public class SpotifyUtils {
    private static SpotifyApi sApi;
    public static SpotifyApi getSpotifyApi(){
        if(sApi == null){
            sApi = new SpotifyApi();
            sApi.setAccessToken(PreferencesUtils.getAccessToken());
        }

        return sApi;
    }
}
