package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.component.Header;
import lombok.Getter;

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
    @Getter
    Header header = new Header();

    public ProfilePage showArchivedCategories() {
        showArchivedCheckbox.click();
        return this;
    }

    public ProfilePage archiveCategory(String categoryName) {
        archiveOrUnarchiveCategory(categoryName, true);
        return this;
    }

    public ProfilePage unarchiveCategory(String categoryName) {
        archiveOrUnarchiveCategory(categoryName, false);
        return this;
    }

    private void archiveOrUnarchiveCategory(String categoryName, boolean archived) {
        categories.findBy(text(categoryName))
                .shouldBe(visible)
                .closest(".MuiBox-root")
                .find("button[aria-label='%s category']".formatted(archived ? "Archive" : "Unarchive"))
                .click();
        (archived ? approveArchiveBtn : approveUnarchiveBtn ).click();
    }

    public void assertCategoryIsArchived(String categoryName) {
        categoryArchivingMessage
                .should(Condition.appear)
                .should(text("Category %s is archived".formatted(categoryName)));
        categories.findBy(text(categoryName)).shouldNot(visible);
        showArchivedCategories();
        categories.findBy(text(categoryName)).should(visible);
    }

    public void assertCategoryIsUnarchived(String categoryName) {
        categoryArchivingMessage
                .should(Condition.appear)
                .should(text("Category %s is unarchived".formatted(categoryName)));
        categories.findBy(text(categoryName)).should(visible);
    }

}
