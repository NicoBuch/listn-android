package ar.com.wolox.android.activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;
import com.squareup.picasso.Picasso;

import ar.com.wolox.android.Configuration;
import ar.com.wolox.android.ListnApplication;
import ar.com.wolox.android.R;
import ar.com.wolox.android.event.PlayingTrackUpdateEvent;
import ar.com.wolox.android.utils.PreferencesUtils;
import ar.com.wolox.android.utils.SpotifyUtils;
import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ListnActivity implements Player.InitializationObserver, PlayerStateCallback {

    private EventBus bus = EventBus.getDefault();

    private View mSearchButton;

    private TextView mNameText;
    private static String TAG = "MainActivity";
    private TextView mTrackName;
    private TextView mTrackArtist;
    private ImageView mAlbumImageView;

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUi() {
        mSearchButton = findViewById(R.id.button_search);
        //mNameText = (TextView) findViewById(R.id.text_name_me);
        mTrackName = (TextView) findViewById(R.id.track_name);
        mTrackArtist = (TextView) findViewById(R.id.track_artist);
        mAlbumImageView = (ImageView) findViewById(R.id.home_album_image);
    }

    @Override
    protected void setListeners() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListnersActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void populate() {
        SpotifyApi api = SpotifyUtils.getSpotifyApi();
        SpotifyService service =  api.getService();
        service.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                //mNameText.setText(userPrivate.display_name);
            }

            @Override
            public void failure(RetrofitError error) {
                //mNameText.setText("NO FUNCIONO LA MIERDA ESTA");
            }
        });
        //Config config = new Config(this, PreferencesUtils.getAccessToken(), Configuration.CLIENT_ID);
        //Spotify.getPlayer(config, ListnApplication.getInstance(), this);
        mTrackName.setText(ListnApplication.getInstance().getmTrack());
        mTrackArtist.setText(ListnApplication.getInstance().getmArtist());
        Picasso.with(getApplicationContext()).load("http://i.forbesimg.com/media/lists/companies/google_416x416.jpg")
                .error(R.drawable.home_like_button_on)
                .noFade()
                .into(mAlbumImageView);


    }

    @Override
    protected void init() {
        if(PreferencesUtils.getAccessToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Log.d("ACCES_TOKEN", PreferencesUtils.getAccessToken());
        Log.d("LOGGED", "ALREADY LOGGED");
        bus.register(this);

    }

    @Override
    public void onInitialized(Player player) {
        player.getPlayerState(this);
    }

    @Override
    public void onError(Throwable throwable) {

        Log.e(TAG, "FALLO EL PLAYER");
    }

    @Override
    public void onPlayerState(PlayerState playerState) {
        String trackId = playerState.trackUri;
        Log.d(TAG, "TRACK ID: " + trackId + ", PLAYING: " + playerState.playing);
        if(!playerState.playing) {
            return;
        }
        SpotifyUtils.getSpotifyApi().getService().getTrack(playerState.trackUri, new Callback<Track>() {
            @Override
            public void success(Track track, Response response) {
                updateTrackView(track);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "FALLO EL Pedido al track");
            }
        });
    }

    private void updateTrackView(Track track) {
        mTrackName.setText(track.name);
        mTrackArtist.setText(track.artists.get(0).name);
    }

    public void onEvent(PlayingTrackUpdateEvent event){
        if(event.isPlaying()) {
            mTrackArtist.setText(event.getArtist());
            mTrackName.setText(event.getTrack());
        }
        else{
            mTrackArtist.setText("");
            mTrackName.setText("CURRENTLY NOT PLAYING ANYTHING");
        }
    }

}

