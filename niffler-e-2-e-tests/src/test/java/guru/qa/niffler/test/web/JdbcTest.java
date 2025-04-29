package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class JdbcTest {

    UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    void xaTxWithFailedTxTest() {
        UserJson user = usersDbClient.createUserWithFriend(
                new UserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                ),
                new UserJson(
                        null,
                        RandomDataUtils.randomUsername(),
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
