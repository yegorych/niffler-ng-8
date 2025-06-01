package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendPage {
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

    public void assertHasFriends(String... usernames) {
        for (String username : usernames) {
            findFriend(username);
            friends.findBy(text(username)).should(visible);
        }
    }

    public void assertHasNoFriends() {
        friends.should(CollectionCondition.empty);
    }

    public void assertHasRequests(String... usernames) {
        for (String username : usernames) {
            findFriend(username);
            requests.findBy(text(username)).should(visible);
        }
    }

    private void findFriend(String username) {
        if (searchClearButton.has(visible)){
            searchClearButton.click();
        }
        searchInput.sendKeys(username);
        searchInput.pressEnter();
    }


    public void assertHasNoRequests() {
        requests.should(CollectionCondition.empty);
    }



}
