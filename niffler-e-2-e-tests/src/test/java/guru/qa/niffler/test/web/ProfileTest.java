package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileTest {
    private static final Config CFG = Config.getInstance();
    private final String username = "yegor";
    private final String password = "12345";

    @Category(
            username = username,
            archived = false
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(username, password)
                .getHeader()
                .openMenu()
                .goToProfilePage()
                .archiveCategory(category.name())
                .assertCategoryIsArchived(category.name());
    }

    @Category(
            username = username,
            archived = true
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(username, password)
                .getHeader()
                .openMenu()
                .goToProfilePage()
                .showArchivedCategories()
                .unarchiveCategory(category.name())
                .assertCategoryIsUnarchived(category.name());
    }
}
