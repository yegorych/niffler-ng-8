package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static guru.qa.niffler.utils.SelenideUtils.chromeConfig;

public class ProfileTest {
    private static final Config CFG = Config.getInstance();
    private final String username = "yegor";
    private final String password = "12345";

    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();
    private final SelenideDriver driver = new SelenideDriver(chromeConfig);


    @User(
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        final CategoryJson archivedCategory = user.testData().categories().getFirst();

        driver.open(CFG.frontUrl(), LoginPage.class);
        browserExtension.drivers().add(driver);
        new LoginPage(driver)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToProfilePage()
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
        driver.open(CFG.frontUrl(), LoginPage.class);
        browserExtension.drivers().add(driver);
        new LoginPage(driver)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .openMenu()
                .goToProfilePage()
                .showArchivedCategories()
                .unarchiveCategory(archivedCategory.name())
                .assertCategoryIsUnarchived(archivedCategory.name());
    }
}
