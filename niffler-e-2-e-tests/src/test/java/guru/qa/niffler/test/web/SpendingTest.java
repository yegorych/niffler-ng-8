package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;


@WebTest
public class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @User(spendings = @Spend(
          category = "Обучение",
          description = "Обучение Niffler 2.0",
          amount = 89000.00,
          currency = CurrencyValues.RUB
  ))
  @Test
  void spendingDescriptionShouldBeUpdatedByTableAction(UserJson user) {
    final String newDescription = "Обучение Niffler NG";
    SpendJson spend = user.testData().spendings().getFirst();
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .editSpending(spend.description())
        .editDescription(newDescription);

    new MainPage().checkThatTableContains(newDescription);
  }

  @User(
          spendings = @Spend(
                  category = "Обучение",
                  description = "Обучение Advanced 2.0",
                  amount = 79990,
                  currency = CurrencyValues.RUB
          )
  )
  @ScreenShotTest("img/expected-stat.png")
  void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doLogin(user.username(), user.testData().password())
            .asserStatisticsDiagram(expected);
  }


  @User
  @ScreenShotTest(value = "img/expected-avatar.png", rewriteExpected = true)
  void avatarDisplayTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doLogin(user.username(), user.testData().password())
            .getHeader()
            .openMenu()
            .goToProfilePage()
            .uploadPicture("img/avatar.png")
            .assertProfilePhotoScreen(expected);
  }

  @User(
          spendings = @Spend(
                  category = "Обучение",
                  description = "Обучение 1",
                  amount = 5000,
                  currency = CurrencyValues.RUB
          )
  )
  @ScreenShotTest(value = "img/expected-delete-stat.png", rewriteExpected = true)
  void deleteStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
    SpendJson spend = user.testData().spendings().getFirst();
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doLogin(user.username(), user.testData().password())
            .deleteSpending(spend.description())
            .assertStatisticsRowIsNotVisible(spend.getStatisticsRowName())
            .asserStatisticsDiagram(expected);
  }

  @User(
          spendings = @Spend(
                  category = "Обучение",
                  description = "Обучение 1",
                  amount = 5000,
                  currency = CurrencyValues.RUB
          )
  )
  @ScreenShotTest(value = "img/expected-edit-stat.png", rewriteExpected = true)
  void editStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
    SpendJson spend = user.testData().spendings().getFirst();
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doLogin(user.username(), user.testData().password())
            .editSpending(spend.description())
            .editAmount("2000")
            .submit()
            .assertStatisticsRowIsNotVisible(spend.getStatisticsRowName())
            .asserStatisticsDiagram(expected);
  }

}
