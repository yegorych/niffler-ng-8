package guru.qa.niffler.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.ProfilePage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Header {
    private final SelenideElement menu = $("[aria-label='Menu']");
    private final ElementsCollection menuItems = $$("[role='menuitem']");

    public ProfilePage goToProfilePage() {
        menuItems.findBy(text("Profile")).click();
        return new ProfilePage();
    }

    public Header openMenu(){
        menu.click();
        return this;
    }
}
