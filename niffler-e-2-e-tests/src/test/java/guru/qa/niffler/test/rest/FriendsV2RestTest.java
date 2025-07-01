package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayV2ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.data.domain.Page;

@RestTest
public class FriendsV2RestTest {

  @RegisterExtension
  static ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

  private final GatewayV2ApiClient gatewayV2ApiClient = new GatewayV2ApiClient();

  @User(friends = 1, incomeInvitations = 2)
  @ApiLogin
  @Test
  void friendsAndIncomeInvitationsShouldBeReturnedFromGateway(@Token String bearerToken) {
    final Page<UserJson> responseBody = gatewayV2ApiClient.allFriends(
        "Bearer " + bearerToken,
        0,
        10,
        null
    );
    Assertions.assertEquals(3, responseBody.getContent().size());
  }
}
