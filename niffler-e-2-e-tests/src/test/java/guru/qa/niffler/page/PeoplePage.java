package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class PeoplePage {
    @Getter
    Header header = new Header();
    private static final ElementsCollection people = $$("#all tr");
    private static final ElementsCollection invitationRequests = people.exclude(Condition.tagName("button"));

    public PeoplePage assertHasInvitationRequest(String username) {
        invitationRequests.find(text(username)).should(Condition.visible);
        return this;
    }



}
