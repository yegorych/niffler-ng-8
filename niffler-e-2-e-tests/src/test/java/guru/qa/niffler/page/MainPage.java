package guru.qa.niffler.page;

import com.codeborne.selenide.*;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.SpendConditions.spend;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
  public static final String URL = CFG.frontUrl() + "main";
  private final Header header = new Header();
  private final ElementsCollection tableRows = $$("#spendings tbody tr");
  private final SelenideElement statistics = $("#stat");
  private final SelenideElement historySpending = $("#spendings");
  private final SelenideElement searchClearButton = $("#input-clear");
  private final SelenideElement searchInput = $("input[type='text']");
  private final SelenideElement deleteButton = $("#delete");
  private final SelenideElement dialogDeleteButton = $$("div[role='dialog'] [type='button']").find(text("Delete"));

  @Nonnull
  public StatComponent getStatComponent() {
    return statComponent;
  }

  @Nonnull
  public SpendingTable getSpendingTable() {
    return spendingTable;
  }

  private final StatComponent statComponent = new StatComponent();
  private final SpendingTable spendingTable = new SpendingTable();


  @Nonnull
  public Header getHeader() {
    return header;
  }

  @Nonnull
  @Step("go to edit spending page")
  public EditSpendingPage editSpending(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
        .$$("td")
        .get(5)
        .click();
    return new EditSpendingPage();
  }

  @Nonnull
  @Step("select spending by {0}")
  public MainPage selectSpending(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
            .$$("td")
            .get(0)
            .click();
    return this;
  }

  @Nonnull
  @Step("delete spending by desc {0}")
  public MainPage deleteSpending(String spendingDescription) {
    selectSpending(spendingDescription);
    deleteButton.click();
    dialogDeleteButton.click();
    return this;
  }


  @Step("check that table contains spend description")
  public void checkThatTableContains(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
        .should(visible);
  }


  @Nonnull
  @Step("check that statistic is visible ")
  public MainPage assertStatisticsIsVisible() {
    statistics.should(visible);
    return this;
  }

  @Nonnull
  @Step("check that history spending is visible")
  public MainPage assertHistorySpendingIsVisible() {
    historySpending.should(visible);
    return this;
  }

  @Nonnull
  @Step("check that table contains spendings")
  public MainPage assertSpending(SpendJson... spendings) {
    tableRows.should(spend(spendings));
    return this;
  }

  private void findSpend(String spendingDescription) {
    if (searchClearButton.has(visible)){
      searchClearButton.click();
    }
    searchInput.sendKeys(spendingDescription);
    searchInput.pressEnter();
  }


  @Nonnull
  @Override
  @Step("check that main page loaded")
  public MainPage checkThatPageLoaded() {
    statistics.should(visible);
    searchInput.should(visible);
    deleteButton.should(visible);
    return this;
  }
}
