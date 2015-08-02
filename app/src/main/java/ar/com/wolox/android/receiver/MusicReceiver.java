package ar.com.wolox.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import ar.com.wolox.android.ListnApplication;
import ar.com.wolox.android.activity.ListnActivity;
import ar.com.wolox.android.callback.WoloxCallback;
import ar.com.wolox.android.event.PlayingTrackUpdateEvent;
import ar.com.wolox.android.model.UserUpdate;
import ar.com.wolox.android.utils.PreferencesUtils;
import ar.com.wolox.android.utils.SpotifyUtils;
import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by santi on 8/2/15.
 */
public class MusicReceiver extends BroadcastReceiver {
    private static final String TAG = "MusicReceiver";
    private EventBus bus = EventBus.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        final boolean playing = intent.getBooleanExtra("playing", false);

        String id = intent.getStringExtra("id");
        if (id != null) {
            id = id.split(":")[2];
            SpotifyUtils.getSpotifyApi().getService().getTrack(id, new Callback<Track>() {
                @Override
                public void success(kaaes.spotify.webapi.android.models.Track track, Response response) {
                    PreferencesUtils.setCurrentMusicInfo(
                            track.name,
                            track.artists.get(0).name,
                            track.album.images.get(0).url,
                            playing
                    );

                    bus.post(new PlayingTrackUpdateEvent());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            UserUpdate mUserUpdate = new UserUpdate();


            ListnApplication.getsUserService().updateUsers("lala", mUserUpdate, new WoloxCallback<Response>() {

                @Override
                public void success(Response o, Response response) {
                    Log.d(TAG, "User Updated");
                }
            });
        }




    }
}
