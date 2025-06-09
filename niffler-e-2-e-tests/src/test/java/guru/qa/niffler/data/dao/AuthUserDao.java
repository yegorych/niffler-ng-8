package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthUserDao {
    @Nonnull
    AuthUserEntity create(AuthUserEntity authUser);
    @Nonnull
    Optional<AuthUserEntity> findById(UUID id);
    @Nonnull
    Optional<AuthUserEntity> findByUsername(String username);
    void delete(AuthUserEntity authUser);
    @Nonnull
    List<AuthUserEntity> findAll();
    @Nonnull
    AuthUserEntity update(AuthUserEntity authUser);
}
