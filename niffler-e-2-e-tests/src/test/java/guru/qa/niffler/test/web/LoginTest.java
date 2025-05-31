package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static guru.qa.niffler.utils.SelenideUtils.chromeConfig;

@Slf4j
public class LoginTest {
    private static final Config CFG = Config.getInstance();
    LoginPage loginPage;

    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();
    private final SelenideDriver driver = new SelenideDriver(chromeConfig);

    @BeforeEach
    public void setup() {
        driver.open(CFG.frontUrl(), LoginPage.class);
        loginPage = new LoginPage(driver);
        browserExtension.drivers().add(driver);
    }

    @Test
    @DisabledByIssue("3")
    @User
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        loginPage.doLogin(user.username(), user.testData().password())
                .assertStatisticsIsVisible()
                .assertHistorySpendingIsVisible();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        loginPage.doLogin(randomUsername(), randomPassword());
        loginPage.isLoginPage().assertBadCredentials();

    }
}
