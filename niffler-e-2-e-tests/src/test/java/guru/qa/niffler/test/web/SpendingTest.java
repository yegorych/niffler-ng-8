package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.Bubble;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;


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
  @ApiLogin
  void spendingDescriptionShouldBeUpdatedByTableAction(UserJson user) {
    final String newDescription = "Обучение Niffler NG";
    SpendJson spend = user.testData().spendings().getFirst();
    Selenide.open(MainPage.URL, MainPage.class)
        .checkThatPageLoaded()
        .editSpending(spend.description())
        .editDescription(newDescription).submit()
        .checkThatTableContains(newDescription);
  }

  @User(
          spendings = {
                  @Spend(
                          category = "Обучение",
                          description = "Курс",
                          amount = 200
                  ),
                  @Spend(
                          category = "Продукты",
                          description = "Мясо",
                          amount = 300
                  )
          }
  )
  @ScreenShotTest(value = "img/expected-stat.png", rewriteExpected = true)
  @ApiLogin
  void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
    List<SpendJson> spendings = user.testData().spendings();
    Selenide.open(MainPage.URL, MainPage.class)
            .checkThatPageLoaded()
            .assertSpending(spendings.toArray(SpendJson[]::new))
            .getStatComponent()
            .checkStatisticImage(expected)
            .checkBubblesContains(
                    new Bubble(Color.yellow, spendings.getLast().getStatBubbleText())
            );
  }


  @User
  @ScreenShotTest(value = "img/expected-avatar.png", rewriteExpected = true)
  @ApiLogin
  void avatarDisplayTest(UserJson user, BufferedImage expected) throws IOException {
    Selenide.open(ProfilePage.URL, ProfilePage.class)
            .uploadPicture("img/avatar.png")
            .assertProfileAvatar(expected);
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
  @ApiLogin
  void deleteStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
    SpendJson spend = user.testData().spendings().getFirst();
    Selenide.open(MainPage.URL, MainPage.class)
            .deleteSpending(spend.description())
            .getStatComponent()
            .checkStatisticImage(expected)
            .checkBubbles();
  }

  @User(
          spendings = @Spend(
                  category = "Обучение",
                  description = "Обучение 1",
                  amount = 5000,
                  currency = CurrencyValues.RUB
          )
  )
  @ScreenShotTest(value = "img/expected-edit-stat.png")
  @ApiLogin
  void editStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
    Double newAmount = 2000.00;
    SpendJson spend = user.testData().spendings().getFirst();
    SpendJson newSpend = new SpendJson(
            null,
            spend.spendDate(),
            spend.category(),
            spend.currency(),
            newAmount,
            spend.description(),
            spend.username()
    );

    Selenide.open(MainPage.URL, MainPage.class)
            .editSpending(spend.description())
            .editAmount(newAmount.toString())
            .submit()
            .checkThatPageLoaded()
            .assertSpending(newSpend)
            .getStatComponent()
            .checkStatisticImage(expected)
            .checkBubbles(
                    new Bubble(
                            Color.yellow,
                            newSpend.getStatBubbleText()
                    )
            );
  }

  @Test
  @User
  @ApiLogin
  void addNewSpendTest(UserJson user) {
    String description = RandomDataUtils.randomSentence(1);
    Selenide.open(EditSpendingPage.URL, EditSpendingPage.class)
            .editDescription(description)
            .editAmount("10")
            .editCategory(RandomDataUtils.randomCategoryName())
            .submit()
            .checkAlertMessage("New spending is successfully created")
            .checkThatPageLoaded()
            .getSpendingTable()
            .checkTableSize(1)
            .checkTableContains(description);



  }



}
