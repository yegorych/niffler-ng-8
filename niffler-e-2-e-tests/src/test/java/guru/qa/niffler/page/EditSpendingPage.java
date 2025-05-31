package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;


public class EditSpendingPage {

  private final SelenideElement descriptionInput;
  private final SelenideElement amountInput;
  private final SelenideElement submitBtn;
  @Getter
  Header header;
  private final SelenideDriver driver;

  public EditSpendingPage(SelenideDriver driver) {
      this.driver = driver;
      this.descriptionInput = driver.$("#description");
      this.amountInput = driver.$("#amount");
      this.submitBtn = driver.$("#save");
      this.header = new Header(driver);
  }

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
    return new MainPage(driver);
  }

}
