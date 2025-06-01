package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class Header {
    private final SelenideElement self = $("#root header");
    private final SelenideElement menu = self.$("[aria-label='Menu']");
    private final ElementsCollection menuItems = self.$$("[role='menuitem']");
    @Getter
    private final SelenideElement profilePhoto = self.$("header .MuiAvatar-root");

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
