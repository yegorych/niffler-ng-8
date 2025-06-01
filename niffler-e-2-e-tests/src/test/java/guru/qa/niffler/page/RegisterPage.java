package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {
    private static final String USERNAME_LENGTH_ERROR_MESSAGE = "Allowed username length should be from 3 to 50 characters";
    private static final String USERNAME_EXISTS_ERROR_MESSAGE = "Username `%s` already exists";
    private static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Allowed password length should be from 3 to 12 characters";
    private static final String PASSWORD_EQUAL_ERROR_MESSAGE = "Passwords should be equal";

    private final SelenideElement username = $("input#username");
    private final SelenideElement password = $("input#password");
    private final SelenideElement passwordSubmit = $("input#passwordSubmit");
    private final SelenideElement signUpBtn = $("button.form__submit");
    private final SelenideElement signInBtn = $("a.form_sign-in");


    @Nonnull
    public RegisterPage setUsername(String username) {
        this.username.sendKeys(username);
        return this;
    }
    @Nonnull
    public RegisterPage setPassword(String password) {
        this.password.sendKeys(password);
        return this;
    }
    @Nonnull
    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        this.passwordSubmit.sendKeys(passwordSubmit);
        return this;
    }
    @Nonnull
    public RegisterPage submitRegistration() {
        signUpBtn.click();
        return this;
    }
    @Nonnull
    public LoginPage goToLoginPage() {
        signInBtn.click();
        return new LoginPage();
    }

    public void assertRegistrationSuccess(){
        signInBtn.should(Condition.visible);
    }

    @Nonnull
    public RegisterPage assertUsernameLength(){
        $(byText(USERNAME_LENGTH_ERROR_MESSAGE)).should(Condition.visible);
        return this;
    }

    public void assertUsernameUniq(String username){
        $(byText(USERNAME_EXISTS_ERROR_MESSAGE.formatted(username))).should(Condition.visible);
    }

    @Nonnull
    public RegisterPage assertPasswordLength(){
        $(byText(PASSWORD_LENGTH_ERROR_MESSAGE)).should(Condition.visible);
        return this;
    }

    public void assertPasswordEqual(){
        $(byText(PASSWORD_EQUAL_ERROR_MESSAGE)).should(Condition.visible);
    }








}
