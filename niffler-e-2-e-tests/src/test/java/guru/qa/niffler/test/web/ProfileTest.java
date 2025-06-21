package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomName;

@WebTest
public class ProfileTest {
    private static final Config CFG = Config.getInstance();



    @User(
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        final CategoryJson archivedCategory = user.testData().categories().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .openMenu()
                .toProfilePage()
                .archiveCategory(archivedCategory.name())
                .assertCategoryIsArchived(archivedCategory.name());
    }


    @User(
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        final CategoryJson archivedCategory = user.testData().categories().getFirst();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .openMenu()
                .toProfilePage()
                .showArchivedCategories()
                .unarchiveCategory(archivedCategory.name())
                .assertCategoryIsUnarchived(archivedCategory.name());
    }


  @User
  @ScreenShotTest(value = "img/expected-avatar.png", rewriteExpected = true)
  void shouldUpdateProfileWithAllFieldsSet(UserJson user, BufferedImage expectedAvatar) throws IOException {
    final String newName = randomName();

    ProfilePage profilePage = Selenide.open(LoginPage.URL, LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded()
        .getHeader()
        .openMenu()
        .toProfilePage()
        .uploadPicture("img/cat.png")
        .updateName(newName)
        .checkAlertMessage("Profile successfully updated");

    Selenide.refresh();

    profilePage.checkName(newName)
        .checkPhotoExist()
        .assertProfileAvatar(expectedAvatar);
  }

  @User
  @Test
  void shouldUpdateProfileWithOnlyRequiredFields(UserJson user) {
    final String newName = randomName();

    ProfilePage profilePage = Selenide.open(LoginPage.URL, LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded()
        .getHeader().openMenu()
        .toProfilePage()
        .updateName(newName)
        .checkAlertMessage("Profile successfully updated");

    Selenide.refresh();
    profilePage.checkName(newName);
  }

  @User
  @Test
  void shouldAddNewCategory(UserJson user) {
    String newCategory = randomCategoryName();

    Selenide.open(LoginPage.URL, LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded()
        .getHeader()
        .openMenu()
        .toProfilePage()
        .addCategory(newCategory)
        .checkAlertMessage("You've added new category:")
        .checkCategoryExists(newCategory);
  }

  @User(
      categories = {
          @Category(name = "Food"),
          @Category(name = "Bars"),
          @Category(name = "Clothes"),
          @Category(name = "Friends"),
          @Category(name = "Music"),
          @Category(name = "Sports"),
          @Category(name = "Walks"),
          @Category(name = "Books")
      }
  )
  @Test
  void shouldForbidAddingMoreThat8Categories(UserJson user) {
    Selenide.open(LoginPage.URL, LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkThatCategoryInputDisabled();
  }

    @User
    @Test
    void updateProfileTest(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .openMenu()
                .toProfilePage()
                .updateName(randomName())
                .checkAlertMessage("Profile successfully updated");
    }
}
