package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;


public class LoginPage {

  private final static String BAD_CREDENTIALS_ERROR_MESSAGE = "Неверные учетные данные пользователя";
  private final SelenideElement usernameInput;
  private final SelenideElement passwordInput;
  private final SelenideElement submitBtn;
  private final SelenideElement registerBtn;
  private final SelenideElement errorMessage;
  private final SelenideDriver driver;

  public LoginPage(SelenideDriver driver) {
    this.driver = driver;
    usernameInput = driver.$("input[name='username']");
    passwordInput = driver.$("input[name='password']");
    submitBtn = driver.$("button[type='submit']");
    registerBtn = driver.$("a.form__register");
    errorMessage = driver.$(".form__error");

  }

  public MainPage doLogin(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();
    return new MainPage(driver);
  }

  public RegisterPage navigateToRegistration() {
    registerBtn.click();
    return new RegisterPage(driver);

  }

  public LoginPage assertBadCredentials(){
    errorMessage
            .should(visible)
            .should(text(BAD_CREDENTIALS_ERROR_MESSAGE));
    return this;
  }

  public LoginPage isLoginPage(){
    usernameInput.should(visible);
    passwordInput.should(visible);
    return this;
  }
}
