package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthUserRepository {
  @Nonnull
  AuthUserEntity create(AuthUserEntity user);
  @Nonnull
  AuthUserEntity update(AuthUserEntity user);
  @Nonnull
  Optional<AuthUserEntity> findById(UUID id);
  @Nonnull
  Optional<AuthUserEntity> findByUsername(String username);
  void remove(AuthUserEntity user);
}
