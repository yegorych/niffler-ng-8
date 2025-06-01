package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Nonnull
public class LoginPage {

  private final static String BAD_CREDENTIALS_ERROR_MESSAGE = "Неверные учетные данные пользователя";
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitBtn = $("button[type='submit']");
  private final SelenideElement registerBtn = $("a.form__register");
  private final SelenideElement errorMessage = $(".form__error");

  @Nonnull
  public MainPage doLogin(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();
    return new MainPage();
  }

  @Nonnull
  public RegisterPage navigateToRegistration() {
    registerBtn.click();
    return new RegisterPage();

  }

  public void assertBadCredentials(){
    errorMessage
            .should(visible)
            .should(text(BAD_CREDENTIALS_ERROR_MESSAGE));
  }

  @Nonnull
  public LoginPage isLoginPage(){
    usernameInput.should(visible);
    passwordInput.should(visible);
    return this;
  }
}
