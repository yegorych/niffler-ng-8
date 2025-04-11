package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    AuthUserEntity create(AuthUserEntity authUser);
    Optional<AuthUserEntity> findById(UUID id);
    Optional<AuthUserEntity> findByUsername(String username);
    void delete(AuthUserEntity authUser);
}
