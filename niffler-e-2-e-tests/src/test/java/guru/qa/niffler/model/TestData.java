package guru.qa.niffler.model;

import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;

import java.util.ArrayList;
import java.util.List;

public record TestData(String password,
                       List<CategoryJson> categories,
                       List<SpendJson> spendings,
                       List<UserJson> friends,
                       List<UserJson> outcomeInvitations,
                       List<UserJson> incomeInvitations) {

  public TestData (String password){
    this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public List<String> friendsUsernames() {
    return extractUsernames(friends);
  }

  public List<String> outcomeInvitationsUsernames() {
    return extractUsernames(outcomeInvitations);
  }

  public List<String> incomeInvitationsUsernames() {
    return extractUsernames(incomeInvitations);
  }

  private List<String> extractUsernames(List<UserJson> users) {
    return users.stream().map(UserJson::username).toList();
  }

  public List<String> categoryDescriptions() {
    return categories.stream().map(CategoryJson::name).toList();
  }
}
