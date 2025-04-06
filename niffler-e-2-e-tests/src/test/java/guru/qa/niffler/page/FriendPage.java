package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendPage {
    private final static ElementsCollection friends = $$("#friends tr");
    private final static SelenideElement friendTab = $("a[href='/people/friends']");
    private final static SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final static ElementsCollection requests = $$("#requests tr");

    public PeoplePage goToPeoplePage() {
        allPeopleTab.click();
        return new PeoplePage();
    }

    public FriendPage assertHasFriend(String username) {
        friends.findBy(text(username)).should(visible);
        return this;
    }

    public FriendPage assertHasNoFriends() {
        friends.should(CollectionCondition.empty);
        return this;
    }

    public FriendPage assertHasRequest(String username) {
        requests.findBy(text(username)).should(visible);
        return this;
    }

    public FriendPage assertHasNoRequests() {
        requests.should(CollectionCondition.empty);
        return this;
    }



}
