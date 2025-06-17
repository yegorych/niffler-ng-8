package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;

public abstract class BasePage<T extends BasePage<?>> {
    @SuppressWarnings("unchecked")
    public <T extends BasePage<?>> T goToPage(SelenideDriver driver, String url, T expectedPage) {
        return (T) driver.open(url, expectedPage.getClass());
    }

    public abstract T checkThatPageLoaded();


}
