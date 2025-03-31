package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @Spend(
      username = "duck",
      category = "Обучение",
      description = "Обучение Niffler 2.0",
      amount = 89000.00,
      currency = CurrencyValues.RUB
  )
  @Test
  void spendingDescriptionShouldBeUpdatedByTableAction(SpendJson spend) {
    final String newDescription = "Обучение Niffler NG";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("duck", "12345")
        .editSpending(spend.description())
        .editDescription(newDescription);

    new MainPage().checkThatTableContains(newDescription);
  }
}
