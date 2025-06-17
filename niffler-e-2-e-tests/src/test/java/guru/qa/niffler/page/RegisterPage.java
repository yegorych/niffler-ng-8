package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selectors.byText;


public class RegisterPage {
    private static final String USERNAME_LENGTH_ERROR_MESSAGE = "Allowed username length should be from 3 to 50 characters";
    private static final String USERNAME_EXISTS_ERROR_MESSAGE = "Username `%s` already exists";
    private static final String PASSWORD_LENGTH_ERROR_MESSAGE = "Allowed password length should be from 3 to 12 characters";
    private static final String PASSWORD_EQUAL_ERROR_MESSAGE = "Passwords should be equal";

    private final SelenideElement username;
    private final SelenideElement password;
    private final SelenideElement passwordSubmit;
    private final SelenideElement signUpBtn;
    private final SelenideElement signInBtn;
    private final SelenideDriver driver;

    public RegisterPage(SelenideDriver driver) {
        this.driver = driver;
        username = driver.$("input#username");
        password = driver.$("input#password");
        passwordSubmit = driver.$("input#passwordSubmit");
        signUpBtn = driver.$("button.form__submit");
        signInBtn = driver.$("a.form_sign-in");
    }

    public RegisterPage setUsername(String username) {
        this.username.sendKeys(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        this.password.sendKeys(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        this.passwordSubmit.sendKeys(passwordSubmit);
        return this;
    }

    public RegisterPage submitRegistration() {
        signUpBtn.click();
        return this;
    }

    public LoginPage goToLoginPage() {
        signInBtn.click();
        return new LoginPage(driver);
    }

    public RegisterPage assertRegistrationSuccess(){
        signInBtn.should(Condition.visible);
        return this;
    }

    public RegisterPage assertUsernameLength(){
        driver.$(byText(USERNAME_LENGTH_ERROR_MESSAGE)).should(Condition.visible);
        return this;
    }

    public RegisterPage assertUsernameUniq(String username){
        driver.$(byText(USERNAME_EXISTS_ERROR_MESSAGE.formatted(username))).should(Condition.visible);
        return this;
    }

    public RegisterPage assertPasswordLength(){
        driver.$(byText(PASSWORD_LENGTH_ERROR_MESSAGE)).should(Condition.visible);
        return this;
    }

    public RegisterPage assertPasswordEqual(){
        driver.$(byText(PASSWORD_EQUAL_ERROR_MESSAGE)).should(Condition.visible);
        return this;
    }








}
