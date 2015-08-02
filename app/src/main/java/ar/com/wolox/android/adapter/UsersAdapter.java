package ar.com.wolox.android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;
import com.squareup.picasso.Picasso;

import java.util.List;

import ar.com.wolox.android.Configuration;
import ar.com.wolox.android.ListnApplication;
import ar.com.wolox.android.R;
import ar.com.wolox.android.callback.WoloxCallback;
import ar.com.wolox.android.model.User;
import ar.com.wolox.android.transformation.CircleTransform;
import ar.com.wolox.android.utils.PreferencesUtils;
import ar.com.wolox.android.utils.SpotifyUtils;
import kaaes.spotify.webapi.android.models.UserPublic;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.POST;

/**
 * Created by nicolasbuchhalter on 01/08/15.
 */
public class UsersAdapter extends BaseAdapter {

    private final List<User> mUsers;
    private final UserClickListener mUserClickListener;
    private String TAG = "UserAdapter";

    public UsersAdapter(List<User> list, UserClickListener userClickListener){
       mUsers = list;
        mUserClickListener = userClickListener;
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

        final ViewHolderItem item;

        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) ListnApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_users, parent, false);


            item = new ViewHolderItem();
            convertView.setTag(item);

            item.textViewUsername = (TextView) convertView.findViewById(R.id.username_text_view);
            item.textViewDistance = (TextView) convertView.findViewById(R.id.distance_text_view);
            item.textViewTrackname = (TextView) convertView.findViewById(R.id.track_name_text_view);
            item.textViewLikesCount = (TextView) convertView.findViewById(R.id.likes_count_text_view);
            item.imageViewUser = (ImageView) convertView.findViewById(R.id.image_user);
            item.imageStartMusic = (ImageView) convertView.findViewById(R.id.start_music_image);



        }
        else {

            item = (ViewHolderItem) convertView.getTag();

        }

        final User mUser = getItem(position);

        item.textViewUsername.setText(mUser.getName());
        item.textViewDistance.setText(mUser.getDistance() + " Away");
        if (mUser.getTrack() != null) {
            item.textViewTrackname.setText(mUser.getTrack().getName() + " - " + mUser.getTrack().getArtist());
        }
        item.textViewLikesCount.setText(String.valueOf(mUser.getLikes()));
        item.imageStartMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUserClickListener != null) {
                    mUserClickListener.onUserClick(mUser);
                }
            }
        });

        SpotifyUtils.getSpotifyApi().getService().getUser(mUser.getSpotifyId(), new WoloxCallback<UserPublic>() {

            @Override
            public void success(UserPublic userPublic, Response response) {

                Picasso.with(ListnApplication.getInstance()).load(userPublic.images.get(0).url)
                        .transform(new CircleTransform())
                        .error(R.drawable.home_like_button_on)
                        .noFade()
                        .into(item.imageViewUser);

            }
        });

        return convertView;
    }

    public static interface UserClickListener {
        public void onUserClick(User user);
    }

    private static class ViewHolderItem {
        TextView textViewUsername;
        TextView textViewDistance;
        TextView textViewTrackname;
        TextView textViewLikesCount;
        ImageView imageViewUser;
        ImageView imageStartMusic;
    }
}
