package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {
    @Nonnull
    UserJson createUser(String username, String password);
    void createIncomeInvitations(UserJson targetUser, int count);
    void createOutcomeInvitations(UserJson targetUser, int count);
    void createFriends(UserJson targetUser, int count);
}
