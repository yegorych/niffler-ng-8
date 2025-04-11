package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.model.AuthUserJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
public class AuthUserEntity implements Serializable {

  private UUID id;
  private String username;
  private String password;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean accountNonLocked;
  private Boolean credentialsNonExpired;

  public static AuthUserEntity fromJson(AuthUserJson json){
    AuthUserEntity aue = new AuthUserEntity();
    aue.setId(json.id());
    aue.setUsername(json.username());
    aue.setPassword(json.password());
    aue.setEnabled(json.enabled());
    aue.setAccountNonExpired(json.accountNonExpired());
    aue.setAccountNonLocked(json.accountNonLocked());
    aue.setCredentialsNonExpired(json.credentialsNonExpired());
    return aue;
  }
}
