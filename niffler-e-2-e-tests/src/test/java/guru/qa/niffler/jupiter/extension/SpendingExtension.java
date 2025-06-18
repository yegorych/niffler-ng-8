package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.client.SpendClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;


@ParametersAreNonnullByDefault
public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
  private final SpendClient spendClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                    .ifPresent(anno -> {
                        if (ArrayUtils.isNotEmpty(anno.spendings())){
                            UserJson createdUser = UserExtension.createdUser();
                            final String username = createdUser != null
                                    ? createdUser.username()
                                    : anno.username();

                            final List<SpendJson> createdSpendings = new ArrayList<>();

                            for (Spend spendAnno: anno.spendings()){
                                SpendJson spendJson = new SpendJson(
                                        null,
                                        new Date(),
                                        new CategoryJson(
                                                null,
                                                spendAnno.category(),
                                                username,
                                                false
                                        ),
                                        spendAnno.currency(),
                                        spendAnno.amount(),
                                        spendAnno.description(),
                                        username
                                );
                                createdSpendings.add(spendClient.createSpend(spendJson));
                            }
                            if (createdUser != null){
                                createdUser.testData().spendings().addAll(createdSpendings);
                            } else {
                                context.getStore(NAMESPACE).put(context.getUniqueId(), createdSpendings);
                            }
                        }
                    });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
  }

  @Override
  public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//    return (SpendJson[]) extensionContext.getStore(SpendingExtension.NAMESPACE).get(extensionContext.getUniqueId(), List.class)
//            .toArray(SpendJson[]::new);
      return createdSpends(extensionContext).toArray(SpendJson[]::new);
  }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static List<SpendJson> createdSpends(ExtensionContext extensionContext) {
        return Optional.ofNullable(extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class))
                .orElse(Collections.emptyList());
    }
}
