package guru.qa.niffler.page;

import com.codeborne.selenide.*;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Dialog;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Nonnull
@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {
    protected static final Config CFG = Config.getInstance();

    private final SelenideElement alert = $(".MuiSnackbar-root");
    private final ElementsCollection formErrors = $$("p.Mui-error, .input__helper-text");
    private final Dialog dialog = new Dialog();

    @SuppressWarnings("unchecked")
    @Nonnull
    @Step("check that alert message has text {0}")
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

    @Nonnull
    @SuppressWarnings("unchecked")
    @Step("click on {0} button in dialog")
    public T clickDialogBtn(String buttonText) {
        if ("Log out".equalsIgnoreCase(buttonText)) {
            dialog.clickButton("Log out"); // возвращает LoginPage
            throw new IllegalStateException("Use clickLogOutDialogBtn() instead to get LoginPage");
        }
        dialog.clickButton(buttonText);
        return (T) this;
    }

    @Nonnull
    @Step("click on Log out button in dialog")
    public LoginPage clickLogoutDialogBtn(){
        dialog.clickButton("Log out");
        return new LoginPage();
    }

    @Nonnull
    public <T extends BasePage<?>> T goToPage(String url, Class<T> pageClass) {
        //return (T) Selenide.open(url, expectedPage.getClass());
        return Selenide.open(url, pageClass);
    }

    public abstract T checkThatPageLoaded();


}
