package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.commons.lang.NotImplementedException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendPage extends BasePage<FriendPage> {
    public static final String URL = CFG.frontUrl() + "people/friends";

    private final static ElementsCollection friends = $$("#friends tr");
    private final static SelenideElement friendTab = $("a[href='/people/friends']");
    private final static SelenideElement searchInput = $("input[type='text']");
    private final static SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final static ElementsCollection requests = $$("#requests tr");
    private final static SelenideElement searchClearButton = $("#input-clear");


    @Nonnull
    public PeoplePage goToPeoplePage() {
        allPeopleTab.click();
        return new PeoplePage();
    }

    @Step("check that user has friends {0}")
    public void assertHasFriends(String... usernames) {
        for (String username : usernames) {
            findFriend(username);
            friends.findBy(text(username)).should(visible);
        }
    }

    @Step("check that user has no friends")
    public void assertHasNoFriends() {
        friends.should(CollectionCondition.empty);
    }

    @Step("check that user has friend requests from {0}")
    @Nonnull
    public FriendPage assertHasRequests(String... usernames) {
        for (String username : usernames) {
            findFriend(username);
            requests.findBy(text(username)).should(visible);
        }
        return this;
    }

    @Step("check that user has no friend requests")
    public void assertHasNoRequests() {
        requests.should(CollectionCondition.empty);
    }

    @Nonnull
    @Step("accept friend requests from {0}")
    public FriendPage acceptFriendRequest(String... friendNames) {
        for (String friendName : friendNames) {
            findFriend(friendName);
            requests.first().$$("button").find(text("Accept")).click();
        }
        return this;
    }

    @Nonnull
    @Step("decline friend requests form {0}")
    public FriendPage declineFriendsRequest(String... friendNames) {
        for (String friendName : friendNames) {
            findFriend(friendName);
            requests.first().$$("button").find(text("Decline")).click();
            clickDialogBtn("Decline");
        }
        return this;
    }


    @Override
    @Step("check that friend page loaded")
    public FriendPage checkThatPageLoaded() {
        throw new NotImplementedException("This method has not been implemented yet");
    }

    private void findFriend(String username) {
        if (searchClearButton.has(visible)){
            searchClearButton.click();
        }
        searchInput.sendKeys(username);
        searchInput.pressEnter();
    }
}
