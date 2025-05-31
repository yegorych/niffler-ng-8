package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.utils.ScreenDiffResult;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static guru.qa.niffler.jupiter.extension.ScreenShotTestExtension.ASSERT_SCREEN_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProfilePage {
    private final ElementsCollection categories;
    private final SelenideElement showArchivedCheckbox;
    private final SelenideElement approveArchiveBtn;
    private final SelenideElement approveUnarchiveBtn;
    private final SelenideElement categoryArchivingMessage;
    private final String archiveCategoryBtnLocator;
    private final String unarchiveCategoryBtnLocator;
    private final SelenideElement uploadPictureInput;
    private final SelenideElement saveChangesButton;
    private final SelenideElement avatar;

    @Getter
    Header header;
    private final SelenideDriver driver;

    public ProfilePage(SelenideDriver driver) {
        this.driver = driver;
        this.header = new Header(driver);
        categories = driver.$$(".MuiChip-label");
        showArchivedCheckbox = driver.$("[type='checkbox']");
        approveArchiveBtn = driver.$(".MuiDialogActions-root").find(byText("Archive"));
        approveUnarchiveBtn = driver.$(".MuiDialogActions-root").find(byText("Unarchive"));
        categoryArchivingMessage = driver.$(".MuiAlert-message");
        archiveCategoryBtnLocator = "button[aria-label='Archive category";
        unarchiveCategoryBtnLocator = "button[aria-label='Unarchive category";
        uploadPictureInput = driver.$("input[type='file']");
        saveChangesButton = driver.$("[type='submit']");
        avatar = driver.$("#image__input").parent().$("img");
        
    }

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

    public ProfilePage uploadPicture(String pathToImage) {
        uploadPictureInput.uploadFromClasspath(pathToImage);
        saveChangesButton.click();
        return this;
    }

    private void assertAlertMessage(String message) {
        categoryArchivingMessage
                .should(Condition.appear)
                .should(text(message));
    }

    public ProfilePage assertProfileAvatar(BufferedImage expectedImage) throws IOException, InterruptedException {
        Thread.sleep(1000);
        BufferedImage actualImage = ImageIO.read(Objects.requireNonNull(avatar.screenshot()));
        assertFalse(new ScreenDiffResult(
                expectedImage,
                actualImage
        ),
            ASSERT_SCREEN_MESSAGE);
        return this;
    }



}
