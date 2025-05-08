package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.UUID;

public interface AuthorityDao {
    void create(AuthorityEntity... authority);
    List<AuthorityEntity> findByUserId(UUID id);
    void delete(AuthorityEntity... authority);
    List<AuthorityEntity> findAll();
    void update(AuthorityEntity... authority);
}
