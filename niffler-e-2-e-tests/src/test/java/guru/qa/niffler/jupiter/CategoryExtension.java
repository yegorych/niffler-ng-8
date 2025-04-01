package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;


public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    Faker faker = Faker.instance();
    private final SpendApiClient spendApiClient = new SpendApiClient();
    CategoryJson category;


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                     category = spendApiClient.addCategory(new CategoryJson(
                            null,
                            faker.funnyName().name(),
                            anno.username(),
                            false

                    ));
                    if (anno.archived()){
                        CategoryJson archivedCategory = new CategoryJson(
                                category.id(),
                                category.name(),
                                anno.username(),
                                true
                        );
                        category = spendApiClient.updateCategory(archivedCategory);
                    }
                });

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return category;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (category.archived()) {
            spendApiClient.updateCategory(new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            ));
        }
    }
}
