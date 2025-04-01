package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.component.Header;
import lombok.Getter;

import java.security.PublicKey;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final ElementsCollection categories = $$(".MuiChip-label");
    private final SelenideElement showArchivedCheckbox = $("[type='checkbox']");
    private final SelenideElement approveArchiveBtn = $(".MuiDialogActions-root").find(byText("Archive"));
    private final SelenideElement approveUnarchiveBtn = $(".MuiDialogActions-root").find(byText("Unarchive"));
    private final SelenideElement categoryArchivingMessage = $(".MuiAlert-message");
    private final String archiveCategoryBtnLocator = "button[aria-label='Archive category";
    private final String unarchiveCategoryBtnLocator = "button[aria-label='Unarchive category";
    @Getter
    Header header = new Header();

    public ProfilePage showArchivedCategories() {
        showArchivedCheckbox.click();
        return this;
    }

    public ProfilePage archiveCategory(String categoryName) {
        categories.findBy(text(categoryName))
                .shouldBe(visible)
                .closest(".MuiBox-root")
                .find(archiveCategoryBtnLocator)
                .click();
        approveArchiveCategory();
        return this;
    }

    public ProfilePage unarchiveCategory(String categoryName) {
        categories.findBy(text(categoryName))
                .shouldBe(visible)
                .closest(".MuiBox-root")
                .find(unarchiveCategoryBtnLocator)
                .click();
        approveUnarchiveCategory();
        return this;
    }

    public ProfilePage approveArchiveCategory() {
        approveArchiveBtn.click();
        return this;
    }

    public ProfilePage approveUnarchiveCategory() {
        approveUnarchiveBtn.click();
        return this;
    }

    public void assertCategoryIsArchived(String categoryName) {
        assertAlertMessage("Category %s is archived".formatted(categoryName));
        categories.findBy(text(categoryName)).shouldNot(visible);
        showArchivedCategories();
        categories.findBy(text(categoryName)).should(visible);
    }

    public void assertCategoryIsUnarchived(String categoryName) {
        assertAlertMessage("Category %s is unarchived".formatted(categoryName));
        categories.findBy(text(categoryName)).should(visible);
    }

    private void assertAlertMessage(String message) {
        categoryArchivingMessage
                .should(Condition.appear)
                .should(text(message));
    }



}
