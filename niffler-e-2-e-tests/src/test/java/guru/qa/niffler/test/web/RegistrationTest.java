package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
    private static final Config CFG = Config.getInstance();
    public static Faker faker = Faker.instance();
    RegisterPage registerPage;
    
    @BeforeEach
    public void before() {
        registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .navigateToRegistration();
    }

    @Test
    void shouldRegisterNewUser() {
        String username = generateUsername();
        String password = generatePassword();

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
                .setUsername(generateUsername())
                .setPassword(generatePassword())
                .setPasswordSubmit(generatePassword())
                .submitRegistration()
                .assertPasswordEqual();
    }

    private String generateUsername() {
        return faker.name().username();
    }
    private String generatePassword() {
        return faker.internet().password();
    }

    
}
