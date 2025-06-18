package guru.qa.niffler.service.client;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.service.impl.UsersDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient{

  static UsersClient getInstance() {
    return "api".equals(System.getProperty("client.impl"))
        ? new UsersApiClient()
        : new UsersDbClient();
  }

  @Nonnull
  UserJson createUser(String username, String password);
  void createIncomeInvitations(UserJson targetUser, int count);
  void createOutcomeInvitations(UserJson targetUser, int count);
  void createFriends(UserJson targetUser, int count);

}
