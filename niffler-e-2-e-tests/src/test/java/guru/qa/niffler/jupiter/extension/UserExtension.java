package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.client.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.jupiter.extension.TestsMethodContextExtension.context;

@ParametersAreNonnullByDefault
public class UserExtension implements
    BeforeEachCallback,
    ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);

    private static final String defaultPassword = "12345";
    private final UsersClient usersClient = UsersClient.getInstance();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if ("".equals(userAnno.username())) {
            final String username = RandomDataUtils.randomUsername();

            UserJson user = usersClient.createUser(
                username,
                defaultPassword
            );

            List<UserJson> friends = new ArrayList<>();
            List<UserJson> income = new ArrayList<>();
            List<UserJson> outcome = new ArrayList<>();

            if (userAnno.friends() > 0) {
              friends = usersClient.addFriends(user, userAnno.friends());
            }
            if (userAnno.incomeInvitations() > 0) {
              income = usersClient.addIncomeInvitations(user, userAnno.incomeInvitations());
            }
            if (userAnno.outcomeInvitations() > 0) {
              outcome = usersClient.addOutcomeInvitations(user, userAnno.outcomeInvitations());
            }

            setUser(
                user.withPassword(
                    defaultPassword
                ).withUsers(
                    friends,
                    outcome,
                    income
                )
            );
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return createdUser();
  }

  @Nullable
  public static UserJson createdUser() {
    final ExtensionContext context = context();
    return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
  }


  public static void setUser(UserJson testUser) {
    final ExtensionContext context = context();
    context.getStore(NAMESPACE).put(
        context.getUniqueId(),
        testUser
    );
  }
}
