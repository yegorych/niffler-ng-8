package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.dao.impl.jdbc.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;
import java.util.UUID;


public class UserdataUserDbClient {

    private final UserdataDao userdataDao = new UdUserDaoJdbc();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.userdataJdbcUrl()
    );

    private static final Config CFG = Config.getInstance();

    public UserJson createUser(UserJson user) {
        return jdbcTxTemplate.execute(() ->
                UserJson.fromEntity(
                        userdataDao.create(UserEntity.fromJson(user)),
                        null
                )
        );
    }

    public Optional<UserJson> findUserByUsername(String username) {
        return jdbcTxTemplate.execute(() ->
                userdataDao.findByUsername(username)
                        .map(userEntity -> UserJson.fromEntity(userEntity, null)));
    }

    public Optional<UserJson> findUserById(UUID id) {
        return jdbcTxTemplate.execute(() ->
                userdataDao.findById(id)
                        .map(userEntity -> UserJson.fromEntity(userEntity, null)));

    }

    public void deleteUser(UserJson user) {
        jdbcTxTemplate.execute(() -> {
            userdataDao.delete(UserEntity.fromJson(user));
            return null;
        });
    }


}
