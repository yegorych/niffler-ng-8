package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.AuthorityJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity implements Serializable {

  private UUID id;

  private Authority authority;

  private UUID userId;

  public static AuthorityEntity fromJson(AuthorityJson json) {
    AuthorityEntity ae = new AuthorityEntity();
    ae.setId(json.id());
    ae.setAuthority(json.authority());
    ae.setUserId(json.userId());
    return ae;
  }

}
