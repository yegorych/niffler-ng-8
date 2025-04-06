package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement submitBtn = $("#save");
  @Getter
  Header header = new Header();

  public void editDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    submitBtn.click();
  }
}
