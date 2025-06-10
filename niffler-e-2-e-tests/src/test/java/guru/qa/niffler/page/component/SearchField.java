package guru.qa.niffler.page.component;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {
    private final SelenideElement searchInput = self.$("input[aria-label='search']");
    private final SelenideElement searchInputClear = self.$("#input-clear");

    protected SearchField() {
        super($("form.MuiBox-root"));
    }

    @Nonnull
    @Step("Search {0}")
    public SearchField search(String query) {
        searchInput.sendKeys(query);
        searchInput.pressEnter();
        return this;
    }

    @Nonnull
    @Step("Clear search input if not empty")
    public SearchField clearIfNotEmpty() {
        if (searchInputClear.isDisplayed())
            searchInputClear.click();
        return this;
    }
}
