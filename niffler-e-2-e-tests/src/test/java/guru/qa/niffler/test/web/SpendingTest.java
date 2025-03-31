package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.jupiter.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

  private static final Config CFG = Config.getInstance();
  private final String username = "yegor";
  private final String password = "12345";

  @Spend(
      username = username,
      category = "Обучение",
      description = "Обучение Niffler 2.0",
      amount = 89000.00,
      currency = CurrencyValues.RUB
  )
  @Test
  void spendingDescriptionShouldBeUpdatedByTableAction(SpendJson spend) {
    final String newDescription = "Обучение Niffler NG";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(username, password)
        .editSpending(spend.description())
        .editDescription(newDescription);

    new MainPage().checkThatTableContains(newDescription);
  }

  @Category(
          username = username,
          archived = false
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doLogin(username, password)
            .getHeader()
            .openMenu()
            .goToProfilePage()
            .archiveCategory(category.name())
            .assertCategoryIsArchived(category.name());
  }

  @Category(
          username = username,
          archived = true
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doLogin(username, password)
            .getHeader()
            .openMenu()
            .goToProfilePage()
            .showArchivedCategories()
            .unarchiveCategory(category.name())
            .assertCategoryIsUnarchived(category.name());
  }
}
