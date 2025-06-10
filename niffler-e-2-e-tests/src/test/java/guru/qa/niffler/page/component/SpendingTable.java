package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable> {
    private final SearchField searchField = new SearchField();
    private final SelenideElement periodBtn = self.$("input[name='period']");
    private final ElementsCollection tableRows = self.$$("#spendings tbody tr");
    private final SelenideElement deleteButton = self.$("#delete");
    private final SelenideElement dialogDeleteButton = self.$$("div[role='dialog'] [type='button']").find(text("Delete"));
    private final SelenideElement nextPageBtn = self.$("#page-next");

    public SpendingTable() {
        super($(".MuiTableContainer-root"));
    }

    @Nonnull
    @Step("Select period")
    public SpendingTable selectPeriod(DataFilterValues period){
        periodBtn.click();
        self.$(String.format("li[data-value='%s']", period)).click();
        return this;
    }

    @Nonnull
    @Step("Edit spending with description {0}")
    public EditSpendingPage editSpending(String description){
        searchSpendingByDescription(description);
        tableRows.find(text(description))
                .$$("td")
                .get(5)
                .click();
        return new EditSpendingPage();
    }

    @Nonnull
    @Step("search spending by description {0}")
    public SpendingTable searchSpendingByDescription(String description){
        searchField.clearIfNotEmpty().search(description);
        return this;
    }

    @Nonnull
    @Step("delete spending by description")
    public SpendingTable deleteSpending(String description){
        selectSpending(description);
        deleteButton.click();
        dialogDeleteButton.click();
        return this;
    }

    @Nonnull
    @Step("select spending by description")
    public SpendingTable selectSpending(String spendingDescription) {
        searchSpendingByDescription(spendingDescription);
        tableRows.find(text(spendingDescription))
                .$$("td")
                .get(0)
                .click();
        return this;
    }

    @Nonnull
    @Step("check spending table")
    public SpendingTable checkTableContains(String... expectedSpends){
        for (String expectedSpend : expectedSpends) {
            searchSpendingByDescription(expectedSpend);
            tableRows.find(text(expectedSpend)).should(visible);
        }
        return this;
    }

    @Nonnull
    @Step("check spending table size")
    public SpendingTable checkTableSize(int expectedSize){
        int tableSize = 0;
        boolean nextClicable = true;
        while (nextClicable){
            nextClicable = false;
            tableSize = tableSize + tableRows.size();
            if (nextPageBtn.isEnabled()){
                nextClicable = true;
                nextPageBtn.click();
            }
        }
        Assertions.assertEquals(expectedSize, tableSize);
        return this;
    }

}
