package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.Getter;
import org.apache.commons.lang.NotImplementedException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.extension.ScreenShotTestExtension.ASSERT_SCREEN_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {
    public static final String URL = CFG.frontUrl() + "profile";
    private final ElementsCollection categories = $$(".MuiChip-label");
    private final SelenideElement showArchivedCheckbox = $("[type='checkbox']");
    private final SelenideElement approveArchiveBtn = $(".MuiDialogActions-root").find(byText("Archive"));
    private final SelenideElement approveUnarchiveBtn = $(".MuiDialogActions-root").find(byText("Unarchive"));
    private final SelenideElement categoryArchivingMessage = $(".MuiAlert-message");
    private final String archiveCategoryBtnLocator = "button[aria-label='Archive category";
    private final String unarchiveCategoryBtnLocator = "button[aria-label='Unarchive category";
    private final SelenideElement uploadPictureInput = $("input[type='file']");
    private final SelenideElement saveChangesButton = $("[type='submit']");
    private final SelenideElement avatar = $("#image__input").parent().$("img");
    private final SelenideElement username = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");

    @Getter
    Header header = new Header();

    @Nonnull
    @Step("show archived categories")
    public ProfilePage showArchivedCategories() {
        showArchivedCheckbox.click();
        return this;
    }

    @Nonnull
    @Step("archive category with name {0}")
    public ProfilePage archiveCategory(String categoryName) {
        categories.findBy(text(categoryName))
                .shouldBe(visible)
                .closest(".MuiBox-root")
                .find(archiveCategoryBtnLocator)
                .click();
        approveArchiveCategory();
        return this;
    }

    @Nonnull
    @Step("unarchive category with name {0}")
    public ProfilePage unarchiveCategory(String categoryName) {
        categories.findBy(text(categoryName))
                .shouldBe(visible)
                .closest(".MuiBox-root")
                .find(unarchiveCategoryBtnLocator)
                .click();
        approveUnarchiveCategory();
        return this;
    }

    @Step("approve category archiving")
    public void approveArchiveCategory() {
        approveArchiveBtn.click();
    }

    @Step("approve category unarchiving")
    public void approveUnarchiveCategory() {
        approveUnarchiveBtn.click();
    }

    @Step("check that category is archived")
    public void assertCategoryIsArchived(String categoryName) {
        assertAlertMessage("Category %s is archived".formatted(categoryName));
        categories.findBy(text(categoryName)).shouldNot(visible);
        showArchivedCategories();
        categories.findBy(text(categoryName)).should(visible);
    }

    @Step("check that category is unarchived")
    public void assertCategoryIsUnarchived(String categoryName) {
        assertAlertMessage("Category %s is unarchived".formatted(categoryName));
        categories.findBy(text(categoryName)).should(visible);
    }

    @Nonnull
    @Step("upload new profile photo")
    public ProfilePage uploadPicture(String pathToImage) {
        uploadPictureInput.uploadFromClasspath(pathToImage);
        saveChangesButton.click();
        return this;
    }

    @Step("check that alert message has text {0}")
    private void assertAlertMessage(String message) {
        categoryArchivingMessage
                .should(Condition.appear)
                .should(text(message));
    }

    @Step("check profile photo")
    public void assertProfileAvatar(BufferedImage expectedImage) throws IOException {
        Selenide.sleep(2000);
        BufferedImage actualImage = ImageIO.read(Objects.requireNonNull(avatar.screenshot()));
        assertFalse(new ScreenDiffResult(
                expectedImage,
                actualImage
        ),
            ASSERT_SCREEN_MESSAGE);
    }

    @Step("Check photo exist")
    @Nonnull
    public ProfilePage checkPhotoExist() {
        avatar.should(attributeMatching("src", "data:image.*"));
        return this;
    }

    @Nonnull
    @Step("update username")
    public ProfilePage updateUsername(String newUsername) {
        username.clear();
        username.sendKeys(newUsername);
        saveChangesButton.click();
        return this;
    }

    @Nonnull
    @Step("update name")
    public ProfilePage updateName(String newName) {
        nameInput.clear();
        nameInput.sendKeys(newName);
        saveChangesButton.click();
        return this;
    }



    @Override
    @Step("check that profile page loaded")
    public ProfilePage checkThatPageLoaded() {
        throw new NotImplementedException("This method has not been implemented yet");
    }

    @Step("Check name: '{0}'")
    @Nonnull
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }


    @Step("Set category: '{0}'")
    @Nonnull
    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    @Step("Check category: '{0}'")
    @Nonnull
    public ProfilePage checkCategoryExists(String category) {
        bubbles.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Check that category input is disabled")
    @Nonnull
    public ProfilePage checkThatCategoryInputDisabled() {
        categoryInput.should(disabled);
        return this;
    }



}
