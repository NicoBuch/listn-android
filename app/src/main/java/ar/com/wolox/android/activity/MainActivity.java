package ar.com.wolox.android.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ar.com.wolox.android.R;
import ar.com.wolox.android.utils.PreferencesUtils;

public class MainActivity extends ListnActivity {

    private Button mSearchButton;

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUi() {
        mSearchButton = (Button) findViewById(R.id.button_search);
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
