package guru.qa.niffler.page;

import com.codeborne.selenide.*;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.StatComponent;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import static guru.qa.niffler.condition.SpendConditions.spend;

public class MainPage extends BasePage<MainPage> {
  @Getter
  private final Header header;
  private final ElementsCollection tableRows;
  private final SelenideElement statistics;
  private final SelenideElement historySpending;
  private final SelenideElement searchClearButton;
  private final SelenideElement searchInput;
  private final SelenideElement deleteButton;
  private final SelenideElement dialogDeleteButton;
  @Getter
  private final StatComponent statComponent;
  private final SelenideDriver driver;

  public MainPage(SelenideDriver driver) {
    this.driver = driver;
    header = new Header(driver);
    tableRows = driver.$$("#spendings tbody tr");
    statistics = driver.$("#stat");
    historySpending = driver.$("#spendings");
    searchClearButton = driver.$("#input-clear");
    searchInput = driver.$("input[type='text']");
    deleteButton = driver.$("#delete");
    dialogDeleteButton = driver.$$("div[role='dialog'] [type='button']").find(text("Delete"));
    statComponent = new StatComponent(driver);
  }

  public EditSpendingPage editSpending(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
        .$$("td")
        .get(5)
        .click();
    return new EditSpendingPage(driver);
  }

  public MainPage selectSpending(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
            .$$("td")
            .get(0)
            .click();
    return this;
  }

  public MainPage deleteSpending(String spendingDescription) {
    selectSpending(spendingDescription);
    deleteButton.click();
    dialogDeleteButton.click();
    return this;
  }

  public void checkThatTableContains(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
        .should(visible);
  }


  public MainPage assertStatisticsIsVisible() {
    statistics.should(visible);
    return this;
  }

  public MainPage assertHistorySpendingIsVisible() {
    historySpending.should(visible);
    return this;
  }

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


  @Override
  public MainPage checkThatPageLoaded() {
    statistics.should(visible);
    historySpending.should(visible);
    return this;
  }
}
