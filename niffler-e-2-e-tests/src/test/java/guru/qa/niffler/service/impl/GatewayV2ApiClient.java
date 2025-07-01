package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayV2Api;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.pageable.RestResponsePage;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class GatewayV2ApiClient extends RestClient {

  private final GatewayV2Api gatewayApi;

  public GatewayV2ApiClient() {
    super(CFG.gatewayUrl());
    this.gatewayApi = create(GatewayV2Api.class);
  }

  @Step("Get all friends & income invitations using /api/v2/friends/all endpoint")
  @Nonnull
  public RestResponsePage<UserJson> allFriends(String bearerToken,
                                               int page,
                                               int size,
                                               @Nullable String searchQuery) {
    final Response<RestResponsePage<UserJson>> response;
    try {
      response = gatewayApi.allFriends(bearerToken, page, size, searchQuery)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return requireNonNull(response.body());
  }
}
