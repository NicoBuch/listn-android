package ar.com.wolox.android.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import ar.com.wolox.android.R;
import ar.com.wolox.android.utils.PreferencesUtils;

public class MainActivity extends ListnActivity {

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUi() {
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void populate() {

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
