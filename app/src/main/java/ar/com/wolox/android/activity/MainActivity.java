package ar.com.wolox.android.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ar.com.wolox.android.R;
import ar.com.wolox.android.utils.PreferencesUtils;
import ar.com.wolox.android.utils.SpotifyUtils;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ListnActivity {

    private Button mSearchButton;
    private TextView mNameText;

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUi() {
        mSearchButton = (Button) findViewById(R.id.button_search);
        mNameText = (TextView) findViewById(R.id.text_name_me);
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
                mNameText.setText(userPrivate.display_name);
            }

            @Override
            public void failure(RetrofitError error) {
                mNameText.setText("NO FUNCIONO LA MIERDA ESTA");
            }
        });
    }

    @Override
    protected void init() {
        if(PreferencesUtils.getAccessToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Log.d("LOGGED", "ALREADY LOGGED");

    }
}
