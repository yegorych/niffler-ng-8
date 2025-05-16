package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement submitBtn = $("#save");
  @Getter
  Header header = new Header();

  public EditSpendingPage editDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    submitBtn.click();
    return this;
  }

  public EditSpendingPage editAmount(String amount) {
    amountInput.clear();
    amountInput.setValue(amount);
    return this;
  }

  public MainPage submit() {
    submitBtn.click();
    return new MainPage();
  }

}
