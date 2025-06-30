package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Slf4j
@WebTest
public class LoginTest {
    private static final Config CFG = Config.getInstance();
    LoginPage loginPage;

    @BeforeEach
    public void setup() {
        loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    }

    @Test
    //@DisabledByIssue("3")
    @User
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        loginPage.doLogin(user.username(), user.testData().password())
                .submit(new MainPage())
                .assertStatisticsIsVisible()
                .assertHistorySpendingIsVisible();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        loginPage.doLogin(randomUsername(), randomPassword()).submit(null);
        loginPage.isLoginPage().assertBadCredentials();

    }
}
