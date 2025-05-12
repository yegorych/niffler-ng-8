package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendPage {
    private final static ElementsCollection friends = $$("#friends tr");
    private final static SelenideElement friendTab = $("a[href='/people/friends']");
    private final static SelenideElement searchInput = $("input[type='text']");
    private final static SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final static ElementsCollection requests = $$("#requests tr");
    private final static SelenideElement searchClearButton = $("#input-clear");


    public PeoplePage goToPeoplePage() {
        allPeopleTab.click();
        return new PeoplePage();
    }

    public FriendPage assertHasFriends(String... usernames) {
        for (String username : usernames) {
            findFriend(username);
            friends.findBy(text(username)).should(visible);
        }
        return this;
    }

    public FriendPage assertHasNoFriends() {
        friends.should(CollectionCondition.empty);
        return this;
    }

    public FriendPage assertHasRequests(String... usernames) {
        for (String username : usernames) {
            findFriend(username);
            requests.findBy(text(username)).should(visible);
        }
        return this;
    }

    private void findFriend(String username) {
        if (searchClearButton.has(visible)){
            searchClearButton.click();
        }
        searchInput.sendKeys(username);
        searchInput.pressEnter();
    }


    public FriendPage assertHasNoRequests() {
        requests.should(CollectionCondition.empty);
        return this;
    }



}
