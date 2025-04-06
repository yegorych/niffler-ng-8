package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Slf4j
@ExtendWith(BrowserExtension.class)
public class LoginTest {
    private static final Config CFG = Config.getInstance();
    LoginPage loginPage;
    private final String username = "yegor";
    private final String password = "12345";

    @BeforeEach
    public void setup() {
        loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    }

    @Test
    @DisabledByIssue("3")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        loginPage.doLogin(username, password)
                .assertStatisticsIsVisible()
                .assertHistorySpendingIsVisible();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        loginPage.doLogin(randomUsername(), randomPassword());
        loginPage.isLoginPage().assertBadCredentials();

    }
}
