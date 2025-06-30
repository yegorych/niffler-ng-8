package guru.qa.niffler.service.client;

import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.service.impl.UsersDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UsersClient{

  static UsersClient getInstance() {
    return "api".equals(System.getProperty("client.impl"))
        ? new UsersApiClient()
        : new UsersDbClient();
  }

  @Nonnull
  UserJson createUser(String username, String password);
  List<UserJson> allUsersWithout(String username);
  List<UserJson> getFriends(String username);
  List<UserJson> getIncomeInvitations(String username);
  List<UserJson> getOutcomeInvitations(String username);
  List<UserJson> addIncomeInvitations(UserJson targetUser, int count);
  List<UserJson> addOutcomeInvitations(UserJson targetUser, int count);
  List<UserJson> addFriends(UserJson targetUser, int count);

}
