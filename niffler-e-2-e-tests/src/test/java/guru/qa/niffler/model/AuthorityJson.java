package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.spend.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("authority")
        Authority authority,
        @JsonProperty("user")
        AuthUserJson user
) {
    public static AuthorityJson fromEntity(AuthorityEntity entity){
        return new AuthorityJson(
                entity.getId(),
                entity.getAuthority(),
                AuthUserJson.fromEntity(entity.getUser())
        );
    }
}
