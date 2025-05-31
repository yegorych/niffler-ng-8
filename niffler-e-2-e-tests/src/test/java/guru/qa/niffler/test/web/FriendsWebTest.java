package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.ConverterBrowser;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.Browser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

import static guru.qa.niffler.utils.SelenideUtils.chromeConfig;


public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();
    @RegisterExtension
    private final static BrowserExtension browserExtension = new BrowserExtension();
    private final SelenideDriver driver = new SelenideDriver(chromeConfig);


    @Test
    @User(friends = 3)
    void friendShouldBePresentInFriendsTable(UserJson user) {
        driver.open(CFG.frontUrl());
        browserExtension.drivers().add(driver);
        new LoginPage(driver)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToFriendPage()
                .assertHasFriends(
                        user.testData().friends()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }


    @Test
    @User
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        driver.open(CFG.frontUrl());
        browserExtension.drivers().add(driver);
        new LoginPage(driver)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToFriendPage()
                .assertHasNoFriends();
    }

    @Test
    @User(incomeInvitations = 1)
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        driver.open(CFG.frontUrl());
        browserExtension.drivers().add(driver);
        new LoginPage(driver)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToFriendPage()
                .assertHasRequests(
                        user.testData().friendshipAddressees()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }

    @Test
    @User(outcomeInvitations = 1)
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        driver.open(CFG.frontUrl());
        browserExtension.drivers().add(driver);
        new LoginPage(driver)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToPeoplePage()
                .assertHasInvitationRequests(
                        user.testData().friendshipRequests()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }




    @ParameterizedTest
    @User(friends = 3)
    @EnumSource(Browser.class)
    void crossBrowserTest(@ConvertWith(ConverterBrowser.class) SelenideDriver newDriver, UserJson user) {
        browserExtension.drivers().add(newDriver);
        newDriver.open(CFG.frontUrl());
        new LoginPage(newDriver)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToFriendPage()
                .assertHasFriends(
                        user.testData().friends()
                                .stream()
                                .map(UserJson::username)
                                .toArray(String[]::new)
                );
    }






}
