package ar.com.wolox.android;

import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import ar.com.wolox.android.callback.WoloxCallback;
import ar.com.wolox.android.event.PlayingTrackUpdateEvent;
import ar.com.wolox.android.model.Track;
import ar.com.wolox.android.model.UserUpdate;
import ar.com.wolox.android.service.UserService;
import ar.com.wolox.android.service.interceptor.SecuredRequestInterceptor;
import ar.com.wolox.android.utils.PreferencesUtils;
import ar.com.wolox.android.utils.SpotifyUtils;
import de.greenrobot.event.EventBus;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class ListnApplication extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "ListnApplication";
    public static final String CURRENT_TRACK = "track";
    public static final String CURRENT_ARTIST = "artist";
    public static final String CURRENT_ALBUM = "album";
    public static final String CURRENT_PLAYING = "playing";
    private static final String SPOTIFY_USER_ID = "id";

    private static ListnApplication sApplication;
    private static RequestInterceptor sSecureRequestInterceptor;
    private static UserService sUserService;

    static {
        buildRestServices();
    }

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    public static void buildRestServices() {
        sSecureRequestInterceptor = new SecuredRequestInterceptor();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        RestAdapter apiaryAdapter = new RestAdapter.Builder()
                .setEndpoint(Configuration.APIARY_ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(sSecureRequestInterceptor)
                .build();
        RestAdapter apiAdapter = new RestAdapter.Builder()
                .setEndpoint(Configuration.API_ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(sSecureRequestInterceptor)
                .build();
        sUserService = apiAdapter.create(UserService.class);
    }

    public static UserService getsUserService() {
        return sUserService;
    }

    @Override
    public void onConnected(Bundle bundle) {
        boolean mRequestingLocationUpdates = true;
        Log.e(TAG, "CONNECTION GOOD");
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "CONNECTION SUSPENDED");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "CONNECTION FAILED");
    }

    public static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2)
                throws JsonParseException {
            String originalDate = element.toString();
            char[] dateChar = originalDate.toCharArray();
            char[] dateCharConverted = Arrays.copyOfRange(dateChar, 1, dateChar.length - 1);
            String date = new String(dateCharConverted);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                return formatter.parse(date);
            } catch (ParseException e3) {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    originalDate = originalDate.substring(1, originalDate.length() - 2);
                    return formatter.parse(originalDate);
                } catch (ParseException e4) {
                    throw new JsonParseException(e4);
                }
            }
        }
    }

    public static ListnApplication getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        createLocationRequest();
        sApplication = this;
        registerMediaReceiver();
    }

    private void registerMediaReceiver() {
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");

        iF.addAction("com.htc.music.metachanged");

        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.spotify.music.metadatachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");
        iF.addAction("com.spotify.music.playbackstatechanged");

        registerReceiver(mReceiver, iF);
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public GoogleApiClient getMGoogleApiClient(){
        return mGoogleApiClient;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onLocationChanged(Location location) {

        if(PreferencesUtils.getSpotifyUserId() == null){
            return;
        }
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        UserUpdate mUserUpdate = new UserUpdate();
        mUserUpdate.setLatitude(location.getLatitude());
        mUserUpdate.setLongitude(location.getLongitude());

        sUserService.updateUsers(PreferencesUtils.getSpotifyUserId(), mUserUpdate, new WoloxCallback<Response>(){

            @Override
            public void success(Response o, Response response) {
                Log.d(TAG, "User Updated");
            }
        });

    }

    private String mAlbumImage;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        private EventBus bus = EventBus.getDefault();

        @Override
        public void onReceive(Context context, Intent intent) {

            final boolean playing = intent.getBooleanExtra("playing", false);

            String id = intent.getStringExtra("id").split(":")[2];
            Log.d(TAG, "URI:" + intent.getStringExtra("id"));
            SpotifyUtils.getSpotifyApi().getService().getTrack(id, new Callback<kaaes.spotify.webapi.android.models.Track>() {
                @Override
                public void success(kaaes.spotify.webapi.android.models.Track track, Response response) {
                    SharedPreferences.Editor editor = getSharedPreferences("ListnApp", MODE_PRIVATE).edit();
                    editor.putString(CURRENT_TRACK, track.name);
                    editor.putString(CURRENT_ARTIST, track.artists.get(0).name);
                    editor.putBoolean(CURRENT_PLAYING, playing);
                    editor.putString(CURRENT_ALBUM, track.album.images.get(0).url);
                    editor.commit();

                    bus.post(new PlayingTrackUpdateEvent());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            UserUpdate mUserUpdate = new UserUpdate();

            sUserService.updateUsers(PreferencesUtils.getSpotifyUserId(), mUserUpdate, new WoloxCallback<Response>() {

                @Override
                public void success(Response o, Response response) {
                    Log.d(TAG, "User Updated");
                }
            });



        }
    };
}
