package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;


@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User(friends = 3)
    @ApiLogin
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(FriendPage.URL, FriendPage.class)
                .assertHasFriends(
                        user.testData().friends()
                        .stream()
                        .map(UserJson::username)
                        .toArray(String[]::new)
                );
    }

    @Test
    @User
    @ApiLogin
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(FriendPage.URL, FriendPage.class)
                .assertHasNoFriends();
    }

    @Test
    @User(incomeInvitations = 1)
    @ApiLogin
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(FriendPage.URL, FriendPage.class)
                .assertHasRequests(
                        user.testData().incomeInvitations()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }

    @Test
    @User(outcomeInvitations = 1)
    @ApiLogin
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(PeoplePage.URL, PeoplePage.class)
                .assertHasInvitationRequests(
                        user.testData().outcomeInvitations()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }




    @Test
    @User(incomeInvitations = 1)
    @ApiLogin
    void acceptFriendRequestTest(UserJson user) {
        String friendshipRequestName = user.testData().incomeInvitations()
                .stream()
                .map(UserJson::username)
                .findFirst().orElseThrow();

        Selenide.open(FriendPage.URL, FriendPage.class)
                .assertHasRequests(friendshipRequestName)
                .acceptFriendRequest(friendshipRequestName)
                .checkAlertMessage(String.format("Invitation of %s accepted", friendshipRequestName));
    }

    @Test
    @User(incomeInvitations = 1)
    @ApiLogin
    void declineFriendRequestTest(UserJson user) {
        String friendshipRequestName = user.testData().incomeInvitations()
                .stream()
                .map(UserJson::username)
                .findFirst().orElseThrow();

        Selenide.open(FriendPage.URL, FriendPage.class)
                .assertHasRequests(friendshipRequestName)
                .declineFriendsRequest(friendshipRequestName)
                .checkAlertMessage(String.format("Invitation of %s is declined", friendshipRequestName));
    }






}
