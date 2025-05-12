package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PeoplePage {
    @Getter
    Header header = new Header();
    private static final ElementsCollection people = $$("#all tr");
    private static final ElementsCollection invitationRequests = people.exclude(Condition.tagName("button"));
    private final static SelenideElement searchClearButton = $("#input-clear");
    private final static SelenideElement searchInput = $("input[type='text']");



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
