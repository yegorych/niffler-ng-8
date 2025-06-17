package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {
    @Getter
    Header header = new Header();
    private final ElementsCollection people = $$("#all tr");
    private final ElementsCollection invitationRequests = people.exclude(Condition.tagName("button"));
    private final SelenideElement searchClearButton = $("#input-clear");
    private final SelenideElement searchInput = $("input[type='text']");


    @Step("check invitation requests from {0}")
    public void assertHasInvitationRequests(String... usernames) {
        Selenide.sleep(5000);
        for (String username : usernames) {
            findPeople(username);
            invitationRequests.find(text(username)).should(Condition.visible);
        }
    }

    private void findPeople(String username) {
        if (searchClearButton.has(visible)){
            searchClearButton.click();
        }
        searchInput.sendKeys(username);
        searchInput.pressEnter();
    }


    @Override
    @Step("check that people page loaded")
    public PeoplePage checkThatPageLoaded() {
        return null;
    }
}
