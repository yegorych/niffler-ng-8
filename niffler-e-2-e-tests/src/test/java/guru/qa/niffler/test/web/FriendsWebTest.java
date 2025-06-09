package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;


@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User(friends = 3)
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .toFriendsPage()
                .assertHasFriends(
                        user.testData().friends()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }


    @Test
    @User
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .toFriendsPage()
                .assertHasNoFriends();
    }

    @Test
    @User(incomeInvitations = 1)
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .toFriendsPage()
                .assertHasRequests(
                        user.testData().friendshipAddressees()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }

    @Test
    @User(outcomeInvitations = 1)
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .toAllPeoplesPage()
                .assertHasInvitationRequests(
                        user.testData().friendshipRequests()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }




    @Test
    @User(incomeInvitations = 1)
    void acceptFriendRequestTest(UserJson user) {
        String friendshipRequestName = user.testData().friendshipAddressees()
                .stream()
                .map(UserJson::username)
                .findFirst().orElseThrow();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .toFriendsPage()
                .assertHasRequests(friendshipRequestName)
                .acceptFriendRequest(friendshipRequestName)
                .checkAlertMessage(String.format("Invitation of %s accepted", friendshipRequestName));
    }

    @Test
    @User(incomeInvitations = 1)
    void declineFriendRequestTest(UserJson user) {
        String friendshipRequestName = user.testData().friendshipAddressees()
                .stream()
                .map(UserJson::username)
                .findFirst().orElseThrow();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .toFriendsPage()
                .assertHasRequests(friendshipRequestName)
                .declineFriendsRequest(friendshipRequestName)
                .checkAlertMessage(String.format("Invitation of %s is declined", friendshipRequestName));
    }






}
