package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(BrowserExtension.class)
public class LoginTest {
    private static final Config CFG = Config.getInstance();
    public static Faker faker = Faker.instance();
    LoginPage loginPage;
    private final String username = "yegor";
    private final String password = "12345";

    @BeforeEach
    public void setup() {
        loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        loginPage.doLogin(username, password)
                .assertStatisticsIsVisible()
                .assertHistorySpendingIsVisible();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        loginPage.doLogin(faker.name().username(), faker.internet().password());
        loginPage.isLoginPage().assertBadCredentials();

    }
}
