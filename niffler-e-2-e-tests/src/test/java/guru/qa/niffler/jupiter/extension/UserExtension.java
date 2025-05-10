package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Friendship;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nullable;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UserExtension implements BeforeEachCallback, ParameterResolver {
    private final UsersClient usersClient = new UsersDbClient();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "12345";

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if ("".equals(anno.username())){
                        final String username = randomUsername();
                        UserJson user = usersClient.createUser(username, defaultPassword);

                        if (ArrayUtils.isNotEmpty(anno.friendships())){
                            for (Friendship friendship : anno.friendships()){
                                int count = friendship.count();
                                switch (friendship.status()){
                                    case FRIEND -> usersClient.createFriends(user, count);
                                    case INVITE_RECEIVED -> usersClient.createIncomeInvitations(user, count);
                                    case INVITE_SENT -> usersClient.createOutcomeInvitations(user, count);
                                    default -> {}
                                }
                            }
                        }
                        context.getStore(NAMESPACE).put(context.getUniqueId(), user.withPassword(defaultPassword));
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdUser();
    }

    public static @Nullable UserJson createdUser(){
        final ExtensionContext context = TestsMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
    }

}




