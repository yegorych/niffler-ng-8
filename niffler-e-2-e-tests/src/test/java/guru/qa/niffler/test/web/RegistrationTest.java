package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {
    private static final Config CFG = Config.getInstance();
    RegisterPage registerPage;
    
    @BeforeEach
    public void before() {
        registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .navigateToRegistration();
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
    void shouldNotRegisterUserWithExistingUsername() {
        String username = "duck";
        String password = "12345";
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
