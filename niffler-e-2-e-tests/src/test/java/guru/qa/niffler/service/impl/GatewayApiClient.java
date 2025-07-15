package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class GatewayApiClient extends RestClient {

  private final GatewayApi gatewayApi;

  public GatewayApiClient() {
    super(CFG.gatewayUrl());
    this.gatewayApi = create(GatewayApi.class);
  }

  @Step("Get all friends & income invitations using /api/friends/all endpoint")
  @Nonnull
  public List<UserJson> allFriends(String bearerToken,
                                   @Nullable String searchQuery) {
    return requireNonNull(executeCall(gatewayApi.allFriends(bearerToken, searchQuery)));
  }

  @Step("Get all users & outcome invitations using /api/users/all endpoint")
  @Nonnull
  public List<UserJson> allUsers(String bearerToken,
                                   @Nullable String searchQuery) {
    return requireNonNull(executeCall(gatewayApi.allUsers(bearerToken, searchQuery)));
  }

  @Step("Get all users & outcome invitations using /api/users/all endpoint")
  public void removeFriend(String bearerToken, @Nullable String targetName){
    executeCall(gatewayApi.removeFriend(bearerToken, targetName));
  }

  @Step("Get all users & outcome invitations using /api/users/all endpoint")
  @Nonnull
  public UserJson sendInvitation(String  bearerToken, String targetName){
    return requireNonNull(executeCall(gatewayApi.sendInvitation(bearerToken, new FriendJson(targetName))));
  }

  @Step("Get all users & outcome invitations using /api/users/all endpoint")
  @Nonnull
  public UserJson acceptInvitation(String bearerToken, String targetName){
    return requireNonNull(executeCall(gatewayApi.acceptInvitation(bearerToken, new FriendJson(targetName))));
  }

  @Step("Get all users & outcome invitations using /api/users/all endpoint")
  @Nonnull
  public UserJson declineInvitation(String bearerToken, String targetName){
    return requireNonNull(executeCall(gatewayApi.declineInvitation(bearerToken, new FriendJson(targetName))));
  }






}
