package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.TestData;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("firstname")
    String firstname,
    @JsonProperty("surname")
    String surname,
    @JsonProperty("fullname")
    String fullname,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("photo")
    String photo,
    @JsonProperty("photoSmall")
    String photoSmall,
    @JsonProperty("friendshipStatus")
    FriendshipStatus friendshipStatus,
    @JsonIgnore
    TestData testData){

  public static UserJson fromEntity(UserEntity entity, FriendshipStatus friendshipStatus) {
    return new UserJson(
        entity.getId(),
        entity.getUsername(),
        entity.getFirstname(),
        entity.getSurname(),
        entity.getFullname(),
        entity.getCurrency(),
        entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
        entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? new String(entity.getPhotoSmall(), StandardCharsets.UTF_8) : null,
        friendshipStatus,
        new TestData(
               null,
               new ArrayList<>(),
               new ArrayList<>(),
               new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
            )
    );
  }

  public UserJson(String username) {
    this(username, null);
  }

  public UserJson(String username, TestData testData) {
    this(null, username, null, null, null, null, null, null, null, testData);
  }

  public UserJson withPassword(String password) {
      //if (testData != null) {
      return withTestData(
              new TestData(
                      password,
                      testData.categories(),
                      testData.spendings(),
                      testData.friends(),
                      testData.outcomeInvitations(),
                      testData.incomeInvitations()
              )
      );

//      else return withTestData(
//              new TestData(
//                      password,
//                      new ArrayList<>(),
//                      new ArrayList<>(),
//                      new ArrayList<>(),
//                      new ArrayList<>(),
//                      new ArrayList<>()
//              )
//      );
  }

    public UserJson withUsers(List<UserJson> friends,
                              List<UserJson> outcomeInvitations,
                              List<UserJson> incomeInvitations) {
        return withTestData(
                new TestData(
                        testData.password(),
                        testData.categories(),
                        testData.spendings(),
                        friends,
                        outcomeInvitations,
                        incomeInvitations
                )
        );
    }

    public UserJson withTestData(TestData testData) {
        return new UserJson(
                id,
                username,
                firstname,
                surname,
                fullname,
                currency,
                photo,
                photoSmall,
                friendshipStatus,
                testData);
    }
}
