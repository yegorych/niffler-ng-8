package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;


public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    private final SpendApiClient spendApiClient = new SpendApiClient();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if(anno.categories().length > 0) {
                        Category categoryAnno = anno.categories()[0];
                        CategoryJson category = spendApiClient.addCategory(new CategoryJson(
                                null,
                                randomCategoryName(),
                                anno.username(),
                                false
                        ));
                        if (categoryAnno.archived()){
                            CategoryJson archivedCategory = new CategoryJson(
                                    category.id(),
                                    category.name(),
                                    anno.username(),
                                    true
                            );
                            category = spendApiClient.updateCategory(archivedCategory);
                        }
                        context.getStore(NAMESPACE).put(context.getUniqueId(), category);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Optional<CategoryJson> category = Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class));
        category.ifPresent(categoryJson -> {
            if (categoryJson.archived()) {
                spendApiClient.updateCategory(new CategoryJson(
                        categoryJson.id(),
                        categoryJson.name(),
                        categoryJson.username(),
                        true
                ));
            }
        });

    }
}
