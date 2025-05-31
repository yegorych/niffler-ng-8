package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;


public class FriendPage {
    private final ElementsCollection friends;
    private final SelenideElement friendTab;
    private final SelenideElement searchInput;
    private final SelenideElement allPeopleTab;
    private final ElementsCollection requests;
    private final SelenideElement searchClearButton;
    private final SelenideDriver driver;

    public FriendPage(SelenideDriver driver) {
        this.driver = driver;
        this.friends = driver.$$("#friends tr");
        this.friendTab = driver.$("a[href='/people/friends']");
        this.searchInput = driver.$("input[type='text']");
        this.allPeopleTab = driver.$("a[href='/people/all']");
        this.requests = driver.$$("#requests tr");
        this.searchClearButton = driver.$("#input-clear");
    }

    public PeoplePage goToPeoplePage() {
        allPeopleTab.click();
        return new PeoplePage(driver);
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
