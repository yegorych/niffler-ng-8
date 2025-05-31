package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;


public class PeoplePage {
    private final SelenideDriver driver;
    @Getter
    Header header;
    private final ElementsCollection people;
    private final ElementsCollection invitationRequests;
    private final SelenideElement searchClearButton;
    private final SelenideElement searchInput;


    public PeoplePage(SelenideDriver driver) {
        this.driver = driver;
        this.header = new Header(driver);
        people = driver.$$("#all tr");
        invitationRequests = people.exclude(Condition.tagName("button"));
        searchClearButton = driver.$("#input-clear");
        searchInput = driver.$("input[type='text']");
    }

    private void findPeople(String username) {
        if (searchClearButton.has(visible)){
            searchClearButton.click();
        }
        searchInput.sendKeys(username);
        searchInput.pressEnter();
    }

    public PeoplePage assertHasInvitationRequests(String... usernames) {
        for (String username : usernames) {
            findPeople(username);
            invitationRequests.find(text(username)).should(Condition.visible);
        }
        return this;
    }



}
