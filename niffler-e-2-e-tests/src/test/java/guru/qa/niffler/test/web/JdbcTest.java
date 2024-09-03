package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class JdbcTest {

    @Test
    void successfulTransactionTest() {
        AuthDbClient authDbClient = new AuthDbClient();
        authDbClient.createUser(
                new AuthUserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        "12345",
                        true,
                        true,
                        true,
                        true
                )
        );
    }

    @Test
    void successfulXaTransactionTest() {
        UserDbClient userDbClient = new UserDbClient();
        String username = RandomDataUtils.randomUsername();
        userDbClient.createUserAuthAndUserdata(
                new AuthUserJson(
                        null,
                        username,
                        "12345",
                        true,
                        true,
                        true,
                        true
                ),
                new UserJson(
                        null,
                        username,
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null
                )
        );
    }

    @Test
    void failedXaTransactionTest() {
        UserDbClient userDbClient = new UserDbClient();
        userDbClient.createUserAuthAndUserdata(
                new AuthUserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        "12345",
                        true,
                        true,
                        true,
                        true
                ),
                new UserJson(
                        null,
                        "duck", //уже есть в бд, будет ошибка
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null
                )
        );
    }

    @Test
    void springJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbc(
                new UserJson(
                        null,
                        "valentin-5",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }
}
