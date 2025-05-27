package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.impl.SpendApiClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

@ParametersAreNonnullByDefault
public class CategoryExtension implements BeforeEachCallback, ParameterResolver {
    private final SpendClient spendClient = new SpendDbClient();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (ArrayUtils.isNotEmpty(anno.categories())){
                        UserJson createdUser = UserExtension.createdUser();
                        final String username = createdUser != null
                                ? createdUser.username()
                                : anno.username();

                        final List<CategoryJson> createdCategories = new ArrayList<>();

                        for (Category category : anno.categories()) {
                            final String categoryName = "".equals(category.name())
                                    ? randomCategoryName()
                                    : category.name();

                            CategoryJson categoryJson = new CategoryJson(
                                    null,
                                    categoryName,
                                    username,
                                    category.archived()
                            );
                            createdCategories.add(spendClient.createCategory(categoryJson));
                        }

                        if (createdUser != null) {
                            createdUser.testData().categories().addAll(createdCategories);
                        } else {
                            context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategories);
                        }
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
    }

    @Override
    public CategoryJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        return (CategoryJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
//                .toArray(CategoryJson[]::new);
        return createdCategories().toArray(CategoryJson[]::new);

    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static List<CategoryJson> createdCategories() {
        final ExtensionContext context = TestsMethodContextExtension.context();
        return Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), List.class))
                .orElse(Collections.emptyList());
    }
}
