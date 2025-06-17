package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import lombok.Getter;
import static com.codeborne.selenide.Condition.text;

public class Header {
    private final SelenideElement menu;
    private final ElementsCollection menuItems;
    @Getter
    private final SelenideElement profilePhoto;
    private SelenideDriver driver;

    public Header(SelenideDriver driver) {
        this.driver = driver;
        menu = driver.$("[aria-label='Menu']");
        menuItems = driver.$$("[role='menuitem']");
        profilePhoto = driver.$("header .MuiAvatar-root");
    }

    public ProfilePage goToProfilePage() {
        menuItems.findBy(text("Profile")).click();
        return new ProfilePage(driver);
    }

    public PeoplePage goToPeoplePage() {
        menuItems.findBy(text("All People")).click();
        return new PeoplePage(driver);
    }

    public FriendPage goToFriendPage() {
        menuItems.findBy(text("Friends")).click();
        return new FriendPage(driver);
    }

    public Header openMenu(){
        menu.click();
        return this;
    }

}
