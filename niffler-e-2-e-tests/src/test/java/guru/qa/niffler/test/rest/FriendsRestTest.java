package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

@RestTest
public class FriendsRestTest {

  @RegisterExtension
  static ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

  private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

  @User(friends = 1, incomeInvitations = 2)
  @ApiLogin
  @Test
  void friendsAndIncomeInvitationsShouldBeReturnedFromGateway(@Token String bearerToken) {
    final List<UserJson> responseBody = gatewayApiClient.allFriends("Bearer " + bearerToken, null);
    Assertions.assertEquals(3, responseBody.size());
  }
}
