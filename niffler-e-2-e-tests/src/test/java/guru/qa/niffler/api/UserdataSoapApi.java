package guru.qa.niffler.api;


import jaxb.userdata.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserdataSoapApi {

  @Headers(value = {
      "Content-type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> currentUser(@Body CurrentUserRequest request);

  @Headers(value = {
      "Content-type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> allUsers(@Body AllUsersRequest request);

  @Headers(value = {
          "Content-type: text/xml",
          "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> friendsPage(@Body FriendsPageRequest request);

  @Headers(value = {
          "Content-type: text/xml",
          "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> friends(@Body FriendsRequest request);


  @Headers(value = {
          "Content-type: text/xml",
          "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<Void> removeFriend(@Body RemoveFriendRequest request);

  @Headers(value = {
          "Content-type: text/xml",
          "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> acceptInvitation(@Body AcceptInvitationRequest request);

  @Headers(value = {
          "Content-type: text/xml",
          "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> declineInvitation(@Body DeclineInvitationRequest request);

  @Headers(value = {
          "Content-type: text/xml",
          "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> sendInvitation(@Body SendInvitationRequest request);

}
