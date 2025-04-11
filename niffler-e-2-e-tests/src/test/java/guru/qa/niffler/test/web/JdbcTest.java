package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.spend.AuthUserEntity;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

    @Test
    void successfulXaTransactionTest() {
        UserDbClient userDbClient = new UserDbClient();
        userDbClient.createUserAuthAndUserdata(
                new AuthUserJson(
                        null,
                        "yegor_test2",
                        "12345",
                        true,
                        true,
                        true,
                        true
                ),
                new UserJson(
                        null,
                        "yegor_test2",
                        "egor",
                        "rysh",
                        "yegorych",
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
}
