package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement submitBtn = $("#save");
  @Getter
  Header header = new Header();

  @Nonnull
  public EditSpendingPage editDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    submitBtn.click();
    return this;
  }

  @Nonnull
  public EditSpendingPage editAmount(String amount) {
    amountInput.clear();
    amountInput.setValue(amount);
    return this;
  }

  @Nonnull
  public MainPage submit() {
    submitBtn.click();
    return new MainPage();
  }

  @Override
  public EditSpendingPage checkThatPageLoaded() {
    return null;
  }
}
