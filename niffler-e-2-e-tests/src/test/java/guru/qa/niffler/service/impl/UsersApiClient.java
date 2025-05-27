package guru.qa.niffler.service.impl;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UsersApiClient implements UsersClient {
  @Nonnull
  @Override
  public UserJson createUser(String username, String password) {
    return null;
  }

  @Override
  public void addIncomeInvitation(UserJson targetUser, int count) {

  }

  @Override
  public void addOutcomeInvitation(UserJson targetUser, int count) {

  }

  @Override
  public void addFriend(UserJson targetUser, int count) {

  }
}
