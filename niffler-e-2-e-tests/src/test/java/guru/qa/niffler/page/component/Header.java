package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Header {
    private final SelenideElement menu = $("[aria-label='Menu']");
    private final ElementsCollection menuItems = $$("[role='menuitem']");
    @Getter
    private final SelenideElement profilePhoto = $("header .MuiAvatar-root");

    public ProfilePage goToProfilePage() {
        menuItems.findBy(text("Profile")).click();
        return new ProfilePage();
    }

    public PeoplePage goToPeoplePage() {
        menuItems.findBy(text("All People")).click();
        return new PeoplePage();
    }

    public FriendPage goToFriendPage() {
        menuItems.findBy(text("Friends")).click();
        return new FriendPage();
    }

    public Header openMenu(){
        menu.click();
        return this;
    }

}
