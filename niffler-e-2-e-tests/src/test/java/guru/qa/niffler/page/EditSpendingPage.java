package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;
import org.apache.commons.lang.NotImplementedException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

  public static final String URL = CFG.frontUrl() + "spending";

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement submitBtn = $("#save");
  private final SelenideElement categoryInput = $("#category");
  @Getter
  Header header = new Header();

  @Nonnull
  @Step("edit spending description")
  public EditSpendingPage editDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  @Nonnull
  @Step("edit spending amount")
  public EditSpendingPage editAmount(String amount) {
    amountInput.clear();
    amountInput.setValue(amount);
    return this;
  }

  @Nonnull
  @Step("edit name of spending category")
  public EditSpendingPage editCategory(String category) {
    categoryInput.clear();
    categoryInput.setValue(category);
    return this;
  }


  @Nonnull
  @Step("submit changes")
  public MainPage submit() {
    submitBtn.click();
    return new MainPage();
  }

  @Override
  @Step("check that edit spending page loaded")
  public EditSpendingPage checkThatPageLoaded() {
    throw new NotImplementedException("This method has not been implemented yet");
  }

  @Nonnull
  @Step("open calendar")
  public Calendar calendar() {
    $(".MuiInputAdornment-root button").click();
    return new Calendar($(".MuiDateCalendar-root"));
  }
}
