package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class Header {
    private final SelenideElement self = $("#root header");
    private final SelenideElement menu = self.$("[aria-label='Menu']");
    private final ElementsCollection menuItems = self.parent().parent().$$("[role='menuitem']");
    private final Dialog dialog = new Dialog();

    @Nonnull
    @Step("Go to profile page")
    public ProfilePage toProfilePage() {
        menuItems.findBy(text("Profile")).click();
        return new ProfilePage();
    }

    @Nonnull
    @Step("Go to All People page")
    public PeoplePage toAllPeoplesPage() {
        menuItems.findBy(text("All People")).click();
        return new PeoplePage();
    }

    @Nonnull
    @Step("Go to Friends page")
    public FriendPage toFriendsPage() {
        menuItems.findBy(text("Friends")).click();
        return new FriendPage();
    }

    @Nonnull
    @Step("Go to Login Page")
    public LoginPage signOut() {
        menuItems.findBy(text("Sign out")).click();
        dialog.clickButton("Log out");
        return new LoginPage();
    }

    @Nonnull
    @Step("Go to edit Spending Page")
    public EditSpendingPage addSpendingPage(){
        self.$("a[href='/spending']").click();
        return new EditSpendingPage();
    }

    @Nonnull
    @Step("Go to main page")
    public MainPage toMainPage() {
        self.$("a[href='/main']").click();
        return new MainPage();
    }

    @Nonnull
    @Step("Open menu")
    public Header openMenu(){
        menu.click();
        return this;
    }

}
