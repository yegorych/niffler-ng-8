package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;


import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static guru.qa.niffler.utils.SelenideUtils.chromeConfig;


public class RegistrationTest {
    private static final Config CFG = Config.getInstance();
    RegisterPage registerPage;

    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();
    private final SelenideDriver driver = new SelenideDriver(chromeConfig);
    
    @BeforeEach
    public void before() {
        driver.open(CFG.frontUrl(), LoginPage.class);
        registerPage = new LoginPage(driver).navigateToRegistration();
        browserExtension.drivers().add(driver);
    }

    @Test
    void shouldRegisterNewUser() {
        String username = randomUsername();
        String password = randomPassword();

        registerPage
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .assertRegistrationSuccess();
    }

    @Test
    @User
    void shouldNotRegisterUserWithExistingUsername(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        registerPage
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .assertUsernameUniq(username);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        registerPage
                .setUsername(randomUsername())
                .setPassword(randomPassword())
                .setPasswordSubmit(randomPassword())
                .submitRegistration()
                .assertPasswordEqual();
    }
}
