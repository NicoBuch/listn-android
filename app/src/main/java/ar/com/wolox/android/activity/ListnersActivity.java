package ar.com.wolox.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.Configuration;
import ar.com.wolox.android.ListnApplication;
import ar.com.wolox.android.R;
import ar.com.wolox.android.adapter.UsersAdapter;
import ar.com.wolox.android.callback.WoloxCallback;
import ar.com.wolox.android.model.User;
import ar.com.wolox.android.utils.PreferencesUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListnersActivity extends ListnActivity implements UsersAdapter.UserClickListener,Player.InitializationObserver {

    private ListView mUsersListView;
    private List<User> mUsers = new LinkedList<>();
    private Player mPlayer;

    @Override
    protected int layout() {
        return R.layout.activity_listners;
    }

    @Override
    protected void setUi() {
        mUsersListView = (ListView) findViewById(R.id.list_view_users);

        mUsersListView.setAdapter(new UsersAdapter(mUsers, this));

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void populate() {
        ListnApplication.getInstance().getsUserService().getUsers("lala", new WoloxCallback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                mUsers.addAll(users);
                ((UsersAdapter) mUsersListView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void init() {
//        Config config = new Config(this, PreferencesUtils.getAccessToken(), Configuration.CLIENT_ID);
//        Spotify.getPlayer(config,this,this);
    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.setData(Uri.parse(
                "spotify:track:" + user.getTrack().getId()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onInitialized(Player player) {
        mPlayer = player;
    }

    @Override
    public void onError(Throwable throwable) {

    }
}
