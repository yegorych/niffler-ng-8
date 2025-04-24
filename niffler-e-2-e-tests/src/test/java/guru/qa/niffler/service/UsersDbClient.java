package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.UdUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.FailingCommitJdbcTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;


public class UsersDbClient {
    private static final Config CFG = Config.getInstance();
    private final AuthUserDao authUserDaoSpring = new AuthUserDaoSpringJdbc();
    private final AuthorityDao authAuthorityDaoSpring = new AuthAuthorityDaoSpringJdbc();
    private final UserdataDao udUserDaoSpring = new UdUserDaoSpringJdbc();
    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();
    private final UserdataDao udUserDao = new UdUserDaoJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new FailingCommitJdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl())),
                    new JdbcTransactionManager(DataSources.dataSource(CFG.userdataJdbcUrl()))
            )
    );


    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUserUsingSpringJdbcTx(UserJson user) {
        return xaTransactionTemplate.execute(() -> createUser(user, true));
    }

    public UserJson createUserUsingSpringJdbcWithoutTx(UserJson user) {
        return createUser(user, true);
    }

    public UserJson createUserUsingJdbcTx(UserJson user) {
        return createUser(user, false);
    }

    public UserJson createUserUsingJdbcWithoutTx(UserJson user) {
        return xaTransactionTemplate.execute(() -> createUser(user, false));
    }


    private UserJson createUser(UserJson user, boolean springJdbs) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword("12345");
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = springJdbs ? authUserDaoSpring.create(authUser) : authUserDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);
        UserEntity userEntity;
        if (springJdbs) {
            authAuthorityDaoSpring.create(authorityEntities);
            userEntity = udUserDaoSpring.create(UserEntity.fromJson(user));
        } else {
            authAuthorityDao.create(authorityEntities);
            userEntity = udUserDao.create(UserEntity.fromJson(user));
        }
        return UserJson.fromEntity(userEntity);
    }

    public UserJson createUserWithoutAtomicos(UserJson user) {
        return transactionTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword("12345");
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDaoSpring.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDaoSpring.create(authorityEntities);
                    return UserJson.fromEntity(udUserDaoSpring.create(UserEntity.fromJson(user)));

                }
        );
    }
}
