package guru.qa.niffler.test.web;


import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.Bubble;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static guru.qa.niffler.utils.SelenideUtils.chromeConfig;


public class SpendingTest {

  private static final Config CFG = Config.getInstance();


  @RegisterExtension
  private final BrowserExtension browserExtension = new BrowserExtension();
  private final SelenideDriver driver = new SelenideDriver(chromeConfig);

  @User(spendings = @Spend(
          category = "Обучение",
          description = "Обучение Niffler 2.0",
          amount = 89000.00,
          currency = CurrencyValues.RUB
  ))
  @Test
  void spendingDescriptionShouldBeUpdatedByTableAction(UserJson user) {
    browserExtension.drivers().add(driver);

    final String newDescription = "Обучение Niffler NG";
    SpendJson spend = user.testData().spendings().getFirst();
    driver.open(CFG.frontUrl(), LoginPage.class);
    new LoginPage(driver)
        .doLogin(user.username(), user.testData().password())
        .editSpending(spend.description())
        .editDescription(newDescription);

    new MainPage(driver).checkThatTableContains(newDescription);
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
  void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
    List<SpendJson> spendings = user.testData().spendings();
    browserExtension.drivers().add(driver);

    driver.open(CFG.frontUrl(), LoginPage.class);
    new LoginPage(driver)
            .doLogin(user.username(), user.testData().password())
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
  void avatarDisplayTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
    browserExtension.drivers().add(driver);

    driver.open(CFG.frontUrl(), LoginPage.class);
    new LoginPage(driver)
            .doLogin(user.username(), user.testData().password())
            .getHeader()
            .openMenu()
            .goToProfilePage()
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
  void deleteStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
    SpendJson spend = user.testData().spendings().getFirst();
    browserExtension.drivers().add(driver);

    driver.open(CFG.frontUrl(), LoginPage.class);
    new LoginPage(driver)
            .doLogin(user.username(), user.testData().password())
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
  void editStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
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
    browserExtension.drivers().add(driver);

    driver.open(CFG.frontUrl(), LoginPage.class);
    new LoginPage(driver)
            .doLogin(user.username(), user.testData().password())
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

}
