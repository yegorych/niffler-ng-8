package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.TransactionIsolation;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserdataUserDbClient {

    private static final Config CFG = Config.getInstance();

    public UserJson createUser(UserJson user) {
        return transaction(connection -> {
            return UserJson.fromEntity(new UdUserDaoJdbc(connection).create(UserEntity.fromJson(user)));
        }, CFG.userdataJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);
    }

    public Optional<UserJson> findUserByUsername(String username) {
        return transaction(connection -> {
            return new UdUserDaoJdbc(connection).findByUsername(username).map(UserJson::fromEntity);
        }, CFG.userdataJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);
    }
    public Optional<UserJson> findUserById(UUID id) {
        return transaction(connection -> {
            return new UdUserDaoJdbc(connection).findById(id).map(UserJson::fromEntity);
        }, CFG.userdataJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);

    }

    public void deleteUser(UserJson user) {
        transaction(connection -> {new UdUserDaoJdbc(connection).delete(UserEntity.fromJson(user));},
                CFG.userdataJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);
    }


}
