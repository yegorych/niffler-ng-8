package guru.qa.niffler.service.impl;


import guru.qa.niffler.api.UserdataSoapApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import jaxb.userdata.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class UserdataSoapClient extends RestClient {

  private static final Config CFG = Config.getInstance();
  private final UserdataSoapApi userdataSoapApi;

  public UserdataSoapClient() {
    super(CFG.userdataUrl(), false, SoapConverterFactory.create("niffler-userdata"), HttpLoggingInterceptor.Level.BODY);
    this.userdataSoapApi = create(UserdataSoapApi.class);
  }

  @NotNull
  @Step("Get current user info using SOAP")
  public UserResponse currentUser(CurrentUserRequest request) throws IOException {
    return Objects.requireNonNull(userdataSoapApi.currentUser(request).execute().body());
  }

  @NotNull
  @Step("Get friends page using SOAP")
  public UsersResponse friendsPage(FriendsPageRequest request) throws IOException {
    return Objects.requireNonNull(userdataSoapApi.friendsPage(request).execute().body());
  }

  @NotNull
  @Step("Get friends using SOAP")
  public UsersResponse friends(FriendsRequest request) throws IOException {
    return Objects.requireNonNull(userdataSoapApi.friends(request).execute().body());
  }

  @Step("Remove friend using SOAP")
  public void removeFriend(RemoveFriendRequest request) throws IOException {
    userdataSoapApi.removeFriend(request).execute();
  }

  @NotNull
  @Step("Accept invitation using SOAP")
  public UserResponse acceptInvitation(AcceptInvitationRequest request) throws IOException {
    return Objects.requireNonNull(userdataSoapApi.acceptInvitation(request).execute().body());
  }

  @NotNull
  @Step("Decline invitation using SOAP")
  public UserResponse declineInvitation(DeclineInvitationRequest request) throws IOException {
    return Objects.requireNonNull(userdataSoapApi.declineInvitation(request).execute().body());
  }

  @NotNull
  @Step("Send invitation using SOAP")
  public UserResponse sendInvitation(SendInvitationRequest request) throws IOException {
    return Objects.requireNonNull(userdataSoapApi.sendInvitation(request).execute().body());
  }


}
