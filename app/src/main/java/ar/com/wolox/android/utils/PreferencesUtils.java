package ar.com.wolox.android.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ar.com.wolox.android.ListnApplication;

/**
 * Created by nicolasbuchhalter on 01/08/15.
 */
public class PreferencesUtils {

    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String SPOTIFY_USER_ID = "spotifyUserId";

    public static void setAccessToken(String accessToken) {
        getSharedPreferences().edit()
                .putString(ACCESS_TOKEN_KEY, accessToken)
                .commit();
    }

    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(ListnApplication.getInstance());
    }

    public static String getAccessToken() {
        return getSharedPreferences().getString(ACCESS_TOKEN_KEY, null);
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
