package ar.com.wolox.android.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ar.com.wolox.android.ListnApplication;

/**
 * Created by nicolasbuchhalter on 01/08/15.
 */
public class PreferencesUtils {

    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String CURRENT_TRACK = "track";
    private static final String CURRENT_ARTIST = "artist";
    private static final String CURRENT_ALBUM = "album";
    private static final String CURRENT_PLAYING = "playing";
    private static final String SPOTIFY_USER_ID = "spotifyUserId";

    public static void setAccessToken(String accessToken) {
        getSharedPreferences().edit()
                .putString(ACCESS_TOKEN_KEY, accessToken)
                .commit();
    }

    public static void setCurrentMusicInfo(String track, String artist, String album, Boolean playing) {
        getSharedPreferences().edit()
            .putString(CURRENT_TRACK, track)
            .putString(CURRENT_ARTIST, artist)
            .putBoolean(CURRENT_PLAYING, playing)
            .putString(CURRENT_ALBUM, album)
                .commit();
    }

    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(ListnApplication.getInstance());
    }

    public static String getAccessToken() {
        return getSharedPreferences().getString(ACCESS_TOKEN_KEY, null);
    }

    public static String getCurrentTrack() {
        return getSharedPreferences().getString(CURRENT_TRACK, null);
    }

    public static String getCurrentAlbum() {
        return getSharedPreferences().getString(CURRENT_ALBUM, null);
    }

    public static String getCurrentArtist() {
        return getSharedPreferences().getString(CURRENT_ARTIST, null);
    }

    public static Boolean getCurrentPlaying() {
        return getSharedPreferences().getBoolean(CURRENT_PLAYING, false);
    }
    public static String getSpotifyUserId(){
        return getSharedPreferences().getString(SPOTIFY_USER_ID, null);
    }

    public static void setSpotifyUserId(String spotifyUserId) {
        getSharedPreferences().edit()
                .putString(SPOTIFY_USER_ID, spotifyUserId)
                .commit();
    }

}
