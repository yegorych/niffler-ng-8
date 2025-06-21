package guru.qa.niffler.api;

import guru.qa.niffler.model.rest.UserJson;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface UserApi {
    @POST("/internal/invitations/send")
    Call<Void> sendInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @POST("/internal/invitations/accept")
    Call<Void> acceptInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @POST("/internal/invitations/decline")
    Call<Void> declineInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @DELETE("/internal/friends/remove")
    Call<Void> removeFriend(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @GET("/internal/users/current")
    Call<UserJson> current(@Query("username") String username);

    @GET("/internal/users/all")
    Call<List<UserJson>> all(@Query("username") String username);
}
