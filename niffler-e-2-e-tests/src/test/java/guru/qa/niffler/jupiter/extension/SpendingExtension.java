package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.impl.SpendDbClientOld;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;
import java.util.Optional;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
  private final SpendDbClientOld spendDbClientOld = new SpendDbClientOld();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                    .ifPresent(annotation -> {
                        if (annotation.spendings().length > 0) {
                            Spend spend = annotation.spendings()[0];
                            Optional<CategoryJson> cj = spendDbClientOld.findCategoryByUsernameAndCategoryName(annotation.username(), spend.category());
                            CategoryJson categoryJson = new CategoryJson(null, spend.category(), annotation.username(), false);

                            categoryJson = cj.isPresent() ? cj.get() : spendDbClientOld.createCategorySpring(categoryJson);
                            SpendJson spendJson = new SpendJson(
                                    null,
                                    new Date(),
                                    categoryJson,
                                    spend.currency(),
                                    spend.amount(),
                                    spend.description(),
                                    annotation.username()
                            );
                            SpendJson created = spendDbClientOld.createSpendSpring(spendJson);
                            context.getStore(NAMESPACE).put(context.getUniqueId(), created);
                        }
                    });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(SpendingExtension.NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
  }
}
