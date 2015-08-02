package ar.com.wolox.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import ar.com.wolox.android.ListnApplication;
import ar.com.wolox.android.R;
import ar.com.wolox.android.model.User;
import retrofit.http.POST;

/**
 * Created by nicolasbuchhalter on 01/08/15.
 */
public class UsersAdapter extends BaseAdapter {

    private final List<User> mUsers;

    public UsersAdapter(List<User> list){
       mUsers = list;
    }
    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public User getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mUsers.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem item;

        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) ListnApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_users, parent, false);

            item = new ViewHolderItem();

            item.textViewItem = (TextView) convertView.findViewById(R.id.username_text_view);
            convertView.setTag(item);

        }
        else {

            item = (ViewHolderItem) convertView.getTag();

        }

        User mUser = getItem(position);
        item.textViewItem.setText(mUser.getName());
        item.textViewItem.setTag(mUser.hashCode());

        return convertView;
    }

    private static class ViewHolderItem {
        TextView textViewItem;

    }
}
