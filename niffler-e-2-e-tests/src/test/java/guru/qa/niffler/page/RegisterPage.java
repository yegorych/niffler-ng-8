package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.commons.lang.NotImplementedException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {
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
    @Step("set username")
    public RegisterPage setUsername(String username) {
        this.username.sendKeys(username);
        return this;
    }
    @Nonnull
    @Step("set password")
    public RegisterPage setPassword(String password) {
        this.password.sendKeys(password);
        return this;
    }
    @Nonnull
    @Step("set password submit")
    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        this.passwordSubmit.sendKeys(passwordSubmit);
        return this;
    }
    @Nonnull
    @Step("submit registration")
    public RegisterPage submitRegistration() {
        signUpBtn.click();
        return this;
    }
    @Nonnull
    @Step("go to login page")
    public LoginPage goToLoginPage() {
        signInBtn.click();
        return new LoginPage();
    }

    @Step("check that registration was successful")
    public void assertRegistrationSuccess(){
        signInBtn.should(Condition.visible);
    }

    @Nonnull
    @Step("check username length")
    public RegisterPage assertUsernameLength(){
        return checkFormErrorMessage(USERNAME_LENGTH_ERROR_MESSAGE);
    }

    @Step("check username uniq")
    public void assertUsernameUniq(String username){
        checkFormErrorMessage(USERNAME_EXISTS_ERROR_MESSAGE.formatted(username));
    }

    @Nonnull
    @Step("check password length")
    public RegisterPage assertPasswordLength(){
        return checkFormErrorMessage(PASSWORD_LENGTH_ERROR_MESSAGE);
        //$(byText(PASSWORD_LENGTH_ERROR_MESSAGE)).should(Condition.visible);
    }

    @Step("check error about password mismatch is visible")
    public void assertPasswordEqual(){
        checkFormErrorMessage(PASSWORD_EQUAL_ERROR_MESSAGE);
    }


    @Override
    public RegisterPage checkThatPageLoaded() {
        throw new NotImplementedException("This method has not been implemented yet");
    }
}
