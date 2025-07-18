package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthorityDao {
    void create(AuthorityEntity... authority);
    @Nonnull
    List<AuthorityEntity> findByUserId(UUID id);
    void delete(AuthorityEntity... authority);
    @Nonnull
    List<AuthorityEntity> findAll();
    void update(AuthorityEntity... authority);
}
