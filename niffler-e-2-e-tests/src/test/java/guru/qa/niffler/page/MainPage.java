package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.StatComponent;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.SpendConditions.spend;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
  @Getter
  private final Header header = new Header();
  private final ElementsCollection tableRows = $$("#spendings tbody tr");
  private final SelenideElement statistics = $("#stat");
  private final SelenideElement historySpending = $("#spendings");
  private final SelenideElement searchClearButton = $("#input-clear");
  private final SelenideElement searchInput = $("input[type='text']");
  private final SelenideElement deleteButton = $("#delete");
  private final SelenideElement dialogDeleteButton = $$("div[role='dialog'] [type='button']").find(text("Delete"));
  @Getter
  private final StatComponent statComponent = new StatComponent();


  @Nonnull
  public EditSpendingPage editSpending(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
        .$$("td")
        .get(5)
        .click();
    return new EditSpendingPage();
  }

  @Nonnull
  public MainPage selectSpending(String spendingDescription) {
    findSpend(spendingDescription);
    tableRows.find(text(spendingDescription))
            .$$("td")
            .get(0)
            .click();
    return this;
  }

  @Nonnull
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


  @Nonnull
  public MainPage assertStatisticsIsVisible() {
    statistics.should(visible);
    return this;
  }

  @Nonnull
  public MainPage assertHistorySpendingIsVisible() {
    historySpending.should(visible);
    return this;
  }

  @Nonnull
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
  public MainPage checkThatPageLoaded() {
    statistics.should(visible);
    historySpending.should(visible);
    return this;
  }
}
