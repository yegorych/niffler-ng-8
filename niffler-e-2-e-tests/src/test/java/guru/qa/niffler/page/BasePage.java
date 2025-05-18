package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;

public abstract class BasePage<T extends BasePage<?>> {
    @SuppressWarnings("unchecked")
    public <T extends BasePage<?>> T goToPage(String url, T expectedPage) {
        return (T) Selenide.open(url, expectedPage.getClass());
    }

    public abstract T checkThatPageLoaded();


}
