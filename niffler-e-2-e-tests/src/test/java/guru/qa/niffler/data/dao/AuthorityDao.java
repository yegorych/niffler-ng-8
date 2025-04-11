package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.AuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthorityDao {
    AuthorityEntity create(AuthorityEntity authority);
    List<AuthorityEntity> findByUserId(UUID id);
    void delete(AuthorityEntity authority);
}
