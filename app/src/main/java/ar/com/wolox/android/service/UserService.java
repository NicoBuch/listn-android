package ar.com.wolox.android.service;

import java.util.List;

import ar.com.wolox.android.model.User;
import ar.com.wolox.android.model.UserUpdate;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by nicolasbuchhalter on 02/08/15.
 */
public interface UserService {

    @GET("/users")
    public void getUsers(@Query("spotify_id") String spotifyId, Callback<List<User>> cb);

    @POST("/users/{spotify_id}")
    public void updateUsers(@Path("spotify_id") String spotify_id, @Body UserUpdate body, Callback<Response> cb);
}
