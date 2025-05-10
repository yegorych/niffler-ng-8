package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Friendship;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;


@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User(friendships = @Friendship(
            count = 1,
            status = FriendshipStatus.FRIEND
    ))
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToFriendPage()
                .assertHasFriends(user.testData().friends().stream().map(UserJson::username).toList());
    }


    @Test
    @User
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToFriendPage()
                .assertHasNoFriends();
    }

    @Test
    @User(friendships = @Friendship(
            count = 3,
            status = FriendshipStatus.INVITE_SENT
    ))
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToFriendPage()
                .assertHasRequests(user.testData().friendshipAddressees().stream().map(UserJson::username).toList());
    }

    @Test
    @User(friendships = @Friendship(
            count = 3,
            status = FriendshipStatus.INVITE_RECEIVED
    ))
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToPeoplePage()
                .assertHasInvitationRequests(user.testData().friendshipRequests().stream().map(UserJson::username).toList());
    }






}
