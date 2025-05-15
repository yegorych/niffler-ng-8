package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
  @Getter
  private final Header header = new Header();
  private final ElementsCollection tableRows = $$("#spendings tbody tr");
  private final SelenideElement statistics = $("#stat");
  private final SelenideElement historySpending = $("#spendings");
  private final SelenideElement searchClearButton = $("#input-clear");
  private final SelenideElement searchInput = $("input[type='text']");
  private final SelenideElement deleteButton = $("#delete");
  private final SelenideElement dialogDeleteButton = $$("div[role='dialog'] [type='button']").find(text("Delete"));
  private final ElementsCollection statisticsRows = $$("ul li");

  public EditSpendingPage editSpending(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
        .$$("td")
        .get(5)
        .click();
    return new EditSpendingPage();
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

  public MainPage assertStatisticsRowIsVisible(String statisticsRowName){
    findStatisticsRow(statisticsRowName).should(visible);
    return this;
  }

  public MainPage assertStatisticsRowIsNotVisible(String statisticsRowName){
    findStatisticsRow(statisticsRowName).shouldNotBe(visible);
    return this;
  }

  private void findSpend(String spendingDescription) {
    if (searchClearButton.has(visible)){
      searchClearButton.click();
    }
    searchInput.sendKeys(spendingDescription);
    searchInput.pressEnter();
  }

  private SelenideElement findStatisticsRow(String statisticsRowName) {
    return statisticsRows.find(text(statisticsRowName));
  }



}
