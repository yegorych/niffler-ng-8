package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Dialog {
    private final SelenideElement self = $(".MuiDialog-container");
    private final ElementsCollection buttons = self.$$("button");

    public void clickButton(String buttonText) {
        buttons.find(text(buttonText)).click();
    }
}
