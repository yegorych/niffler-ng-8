package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.TransactionIsolation;
import guru.qa.niffler.model.UserJson;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.xaTransaction;

@SuppressWarnings("unchecked, rawtypes")
public class UserDbClient {
    private static final Config CFG = Config.getInstance();


    public UserJson createUserAuthAndUserdata(AuthUserJson authUser, UserJson userdataUser) {
        XaFunction xaFunAuthUser = new XaFunction<>(
                connection -> {
                    AuthUserEntity authUserEntity = AuthUserEntity.fromJson(authUser);
                    AuthUserJson.fromEntity(new AuthUserDaoJdbc(connection).create(authUserEntity)).id();
                    return null;
                }, CFG.authJdbcUrl()
        );
        XaFunction<UserJson> xaFunUser = new XaFunction<>(
                connection -> {
                    UserEntity userEntity = UserEntity.fromJson(userdataUser);
                    return UserJson.fromEntity(new UserdataUserDaoJdbc(connection).createUser(userEntity));
                }, CFG.userdataJdbcUrl()
        );

        return xaTransaction(TransactionIsolation.READ_UNCOMMITTED, xaFunAuthUser, xaFunUser);
    }

    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(TransactionIsolation.READ_UNCOMMITTED,
                        new XaFunction<>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword("12345");
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDaoJdbc(con).create(authUser);
                                    new AuthAuthorityDaoJdbc(con).create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                                AuthorityEntity ae = new AuthorityEntity();
                                                                ae.setUserId(authUser.getId());
                                                                ae.setAuthority(a);
                                                                return ae;
                                                            }
                                                    ).toArray(AuthorityEntity[]::new));
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new XaFunction<>(
                                con -> {
                                    UserEntity ue = new UserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    new UserdataUserDaoJdbc(con).createUser(ue);
                                    return ue;
                                },
                                CFG.userdataJdbcUrl()
                        )
                ));
    }
}
