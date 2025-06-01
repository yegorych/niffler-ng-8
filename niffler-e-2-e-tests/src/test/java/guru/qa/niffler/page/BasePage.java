package guru.qa.niffler.page;

import com.codeborne.selenide.*;
import guru.qa.niffler.config.Config;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public abstract class BasePage<T extends BasePage<?>> {
    protected static final Config CFG = Config.getInstance();

    private final SelenideElement alert = $(".MuiSnackbar-root");
    private final ElementsCollection formErrors = $$("p.Mui-error, .input__helper-text");

    @SuppressWarnings("unchecked")
    @Nonnull
    public T checkAlertMessage(String expectedText) {
        alert.should(Condition.visible).should(Condition.text(expectedText));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public T checkFormErrorMessage(String... expectedText) {
        formErrors.should(CollectionCondition.textsInAnyOrder(expectedText));
        return (T) this;
    }

    public <T extends BasePage<?>> T goToPage(String url, Class<T> pageClass) {
        //return (T) Selenide.open(url, expectedPage.getClass());
        return Selenide.open(url, pageClass);
    }

    public abstract T checkThatPageLoaded();


}
