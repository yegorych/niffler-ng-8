package guru.qa.niffler.test.rest;

import com.github.tomakehurst.wiremock.common.filemaker.FilenameTemplateModel;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.client.UsersClient;
import guru.qa.niffler.service.impl.GatewayApiClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static guru.qa.niffler.model.FriendshipStatus.FRIEND;
import static guru.qa.niffler.model.FriendshipStatus.INVITE_RECEIVED;

@RestTest
public class FriendsRestTest {

  @RegisterExtension
  static ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

  private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
  private final UsersClient usersClient = new UsersApiClient();


  @User(incomeInvitations = 1, friends = 1)
  @ApiLogin
  @Test
  void friendsAndIncomeInvitationsShouldBeReturnedFromGateway(UserJson user,  @Token String bearerToken) {
    final List<UserJson> responseBody = gatewayApiClient.allFriends(bearerToken, null);
    Assertions.assertAll(
            () -> Assertions.assertEquals(
                    responseBody.stream()
                            .filter(userJson -> FRIEND.equals(userJson.friendshipStatus()))
                            .toArray().length,
                    user.testData().friends().size()),

            () -> Assertions.assertEquals(
                    responseBody.stream()
                            .filter(userJson -> INVITE_RECEIVED.equals(userJson.friendshipStatus()))
                            .toArray().length,
                    user.testData().incomeInvitations().size())
    );
  }

  @User(friends = 1)
  @ApiLogin
  @Test
  void friendsShouldBeDeletedByGateway(UserJson user,  @Token String bearerToken) {
    user.testData().friends().forEach(friend-> gatewayApiClient.removeFriend(bearerToken, friend.username()));
    final List<UserJson> friendsList = gatewayApiClient.allFriends(bearerToken, null);
    Assertions.assertTrue(friendsList.isEmpty());
  }

  @User(incomeInvitations = 2)
  @ApiLogin
  @Test
  void incomeInvitationsShouldBeAcceptedByGateway(UserJson user,  @Token String bearerToken) {
    final int expectedFriends = user.testData().incomeInvitations().size();
    user.testData().incomeInvitations().forEach(income-> gatewayApiClient.acceptInvitation(bearerToken, income.username()));
    final List<UserJson> friendsList = gatewayApiClient.allFriends(bearerToken, null).stream()
            .filter(userJson -> FRIEND.equals(userJson.friendshipStatus())).toList();
    Assertions.assertEquals(expectedFriends, friendsList.size());
  }

  @User(incomeInvitations = 2)
  @ApiLogin
  @Test
  void incomeInvitationsShouldBeDeclinedByGateway(UserJson user,  @Token String bearerToken) {
    user.testData().incomeInvitations().forEach(income-> gatewayApiClient.declineInvitation(bearerToken, income.username()));
    final List<UserJson> friendsList = gatewayApiClient.allFriends(bearerToken, null);
    Assertions.assertTrue(friendsList.isEmpty());
  }

  @User
  @ApiLogin
  @Test
  void incomeAndOutcomeInvitationsShouldBeCreatedByGateway(UserJson user,  @Token String bearerToken) {
    String randomUsername = RandomDataUtils.randomUsername();
    String randomPassword = "12345";
    UserJson targetUser = usersClient.createUser(randomUsername, randomPassword);

    gatewayApiClient.sendInvitation(bearerToken, targetUser.username());

    Assertions.assertAll(
            () -> Assertions.assertEquals(
                    targetUser.username(),
                    usersClient.getOutcomeInvitations(user.username()).getFirst().username()
            ),
            () -> Assertions.assertEquals(
                    user.username(),
                    usersClient.getIncomeInvitations(targetUser.username()).getFirst().username()
            )
    );
  }
}
