package ar.com.wolox.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ar.com.wolox.android.R;
import ar.com.wolox.android.adapter.UsersAdapter;
import ar.com.wolox.android.model.User;

public class ListnersActivity extends ListnActivity {

    private ListView mUsersListView;

    @Override
    protected int layout() {
        return R.layout.activity_listners;
    }

    @Override
    protected void setUi() {
        mUsersListView = (ListView) findViewById(R.id.list_view_users);

        ///// Mocked Users////////////////

        List<User> mUsers = new ArrayList<User>();
        mUsers.add(new User("Nico"));
        mUsers.add(new User("Fefe"));
        mUsers.add(new User("JM"));
        mUsers.add(new User("Ranti"));
        mUsers.add(new User("Fagus"));
        mUsers.add(new User("Wilson"));
        mUsers.add(new User("Santi"));

        ///////////////////////////////


        mUsersListView.setAdapter(new UsersAdapter(mUsers));
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void populate() {

    }

    @Override
    protected void init() {

    }
}
