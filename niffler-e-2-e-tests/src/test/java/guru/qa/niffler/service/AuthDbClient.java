package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.TransactionIsolation;

import static guru.qa.niffler.data.Databases.transaction;

public class AuthDbClient {
    private static final Config CFG = Config.getInstance();

    public AuthUserJson createUser(AuthUserJson user) {
        return transaction(connection -> {
            AuthUserEntity authUser = new AuthUserDaoJdbc(connection).create(AuthUserEntity.fromJson(user));
            AuthorityEntity authority = new AuthorityEntity();

            authority.setUser(authUser);
            authority.setAuthority(Authority.read);
            new AuthorityDaoJdbc(connection).create(authority);
            authority.setAuthority(Authority.write);
            new AuthorityDaoJdbc(connection).create(authority);

            return AuthUserJson.fromEntity(authUser);
        }, CFG.authJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);
    }


}
