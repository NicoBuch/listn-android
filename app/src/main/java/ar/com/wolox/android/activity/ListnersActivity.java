package ar.com.wolox.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.ListnApplication;
import ar.com.wolox.android.R;
import ar.com.wolox.android.adapter.UsersAdapter;
import ar.com.wolox.android.callback.WoloxCallback;
import ar.com.wolox.android.model.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListnersActivity extends ListnActivity {

    private ListView mUsersListView;
    private List<User> mUsers = new LinkedList<>();

    @Override
    protected int layout() {
        return R.layout.activity_listners;
    }

    @Override
    protected void setUi() {
        mUsersListView = (ListView) findViewById(R.id.list_view_users);

        mUsersListView.setAdapter(new UsersAdapter(mUsers));

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

    }
}
