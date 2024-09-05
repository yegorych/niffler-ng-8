package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {
  private UUID id;
  private String username;
  private CurrencyValues currency;
  private String firstname;
  private String surname;
  private String fullname;
  private byte[] photo;
  private byte[] photoSmall;

  public static UserEntity fromJson(UserJson user) {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(user.id());
    userEntity.setUsername(user.username());
    userEntity.setCurrency(user.currency());
    userEntity.setFirstname(user.firstname());
    userEntity.setSurname(user.surname());
    userEntity.setFullname(user.fullname());
    userEntity.setPhoto(user.photo());
    userEntity.setPhotoSmall(user.photoSmall());
    return userEntity;
  }

}