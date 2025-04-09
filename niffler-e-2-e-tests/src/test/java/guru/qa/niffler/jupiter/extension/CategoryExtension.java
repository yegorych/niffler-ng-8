package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;


public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    private final SpendDbClient spendDbClient = new SpendDbClient();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.categories().length > 0) {
                        Category categoryAnno = anno.categories()[0];
                        CategoryJson category = spendDbClient.createCategory(new CategoryJson(
                                null,
                                randomCategoryName(),
                                anno.username(),
                                categoryAnno.archived()
                        ));
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
        Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class))
                .ifPresent(spendDbClient::deleteCategory);

    }
}
