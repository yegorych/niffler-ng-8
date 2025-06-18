package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.commons.lang.NotImplementedException;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Nonnull
public class LoginPage extends BasePage<LoginPage> {

  private final static String BAD_CREDENTIALS_ERROR_MESSAGE = "Неверные учетные данные пользователя";
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitBtn = $("button[type='submit']");
  private final SelenideElement registerBtn = $("a.form__register");

  @Nonnull
  public MainPage doLogin(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();
    return new MainPage();
  }

  @Nonnull
  @Step("go to registration page")
  public RegisterPage navigateToRegistration() {
    registerBtn.click();
    return new RegisterPage();

  }

  @Step("check bad credentials error")
  public void assertBadCredentials(){
    checkFormErrorMessage(BAD_CREDENTIALS_ERROR_MESSAGE);
  }

  @Nonnull
  @Step("check that this is Login page")
  public LoginPage isLoginPage(){
    usernameInput.should(visible);
    passwordInput.should(visible);
    return this;
  }

  @Override
  @Step("check that login page loaded")
  public LoginPage checkThatPageLoaded() {
    throw new NotImplementedException("This method has not been implemented yet");
  }
}
