package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UserdataSoapClient;
import jaxb.userdata.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@SoapTest
public class SoapUsersTest {

  private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();

  @Test
  @User
  void currentUserTest(UserJson user) throws IOException {
    CurrentUserRequest request = new CurrentUserRequest();
    request.setUsername(user.username());
    UserResponse response = userdataSoapClient.currentUser(request);
    Assertions.assertEquals(
        user.username(),
        response.getUser().getUsername()
    );
  }

  @Test
  @User(friends = 3)
  void friendsPageTest(UserJson user) throws IOException {
    FriendsPageRequest request = new FriendsPageRequest();
    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(0);
    pageInfo.setSize(10);
    request.setUsername(user.username());
    request.setPageInfo(pageInfo);
    UsersResponse response = userdataSoapClient.friendsPage(request);
    Assertions.assertEquals(
            user.testData().friends().size(),
            response.getUser().size()
    );
  }

  @Test
  @User(friends = 3)
  void friendsTest(UserJson user) throws IOException {
    String expectedFriendUsername = user.testData().friends().getLast().username();
    FriendsRequest request = new FriendsRequest();
    request.setUsername(user.username());
    request.setSearchQuery(expectedFriendUsername);
    UsersResponse response = userdataSoapClient.friends(request);
    Assertions.assertEquals(1, response.getUser().size());
    Assertions.assertEquals(
            expectedFriendUsername,
            response.getUser().getFirst().getUsername()
    );
  }


  @Test
  @User(friends = 1)
  void removeFriendTest(UserJson user) throws IOException {
    RemoveFriendRequest request = new RemoveFriendRequest();
    request.setUsername(user.username());
    request.setFriendToBeRemoved(user.testData().friends().getLast().username());
    userdataSoapClient.removeFriend(request);

    FriendsRequest friendsRequest = new FriendsRequest();
    friendsRequest.setUsername(user.username());
    UsersResponse response = userdataSoapClient.friends(friendsRequest);

    Assertions.assertTrue(response.getUser().isEmpty());
  }

  @Test
  @User(incomeInvitations = 1)
  void acceptInvitationTest(UserJson user) throws IOException {
    String incomeInvitationFriend = user.testData().incomeInvitationsUsernames().getLast();

    AcceptInvitationRequest request = new AcceptInvitationRequest();
    request.setUsername(user.username());
    request.setFriendToBeAdded(incomeInvitationFriend);

    UserResponse response = userdataSoapClient.acceptInvitation(request);
    Assertions.assertEquals(FriendshipStatus.FRIEND, response.getUser().getFriendshipStatus());

  }

  @Test
  @User(incomeInvitations = 1)
  void declineInvitationTest(UserJson user) throws IOException {
    String incomeInvitationFriend = user.testData().incomeInvitationsUsernames().getLast();

    DeclineInvitationRequest request = new DeclineInvitationRequest();
    request.setUsername(user.username());
    request.setInvitationToBeDeclined(incomeInvitationFriend);

    UserResponse response = userdataSoapClient.declineInvitation(request);
    Assertions.assertEquals(FriendshipStatus.VOID, response.getUser().getFriendshipStatus());

  }

  @Test
  @User(friends = 1)
  void sendInvitationTest(UserJson user) throws IOException {
    String friendUsername = user.testData().friends().getLast().username();
    RemoveFriendRequest request = new RemoveFriendRequest();
    request.setUsername(user.username());
    request.setFriendToBeRemoved(friendUsername);
    userdataSoapClient.removeFriend(request);


    SendInvitationRequest sendInvitationRequest = new SendInvitationRequest();
    sendInvitationRequest.setUsername(user.username());
    sendInvitationRequest.setFriendToBeRequested(friendUsername);
    UserResponse response = userdataSoapClient.sendInvitation(sendInvitationRequest);

    Assertions.assertEquals(FriendshipStatus.INVITE_SENT, response.getUser().getFriendshipStatus());
  }
}
