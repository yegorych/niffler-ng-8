package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.TransactionIsolation;


public class AuthDbClient {
    private static final Config CFG = Config.getInstance();
    private final AuthUserDao userDao = new AuthUserDaoJdbc();
    private final AuthorityDao authorityDao = new AuthAuthorityDaoJdbc();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.authJdbcUrl()
    );

    public AuthUserJson createUser(AuthUserJson user) {
        return jdbcTxTemplate.execute(() ->{
            AuthUserEntity authUser = userDao.create(AuthUserEntity.fromJson(user));
            AuthorityEntity authority = new AuthorityEntity();
            authority.setUserId(authUser.getId());
            authority.setAuthority(Authority.read);
            authorityDao.create(authority);
            authority.setAuthority(Authority.write);
            authorityDao.create(authority);
            return AuthUserJson.fromEntity(authUser);
        });
    }


}
