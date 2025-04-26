package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class JdbcTest {

    UsersDbClient usersDbClient = new UsersDbClient();

    /*
    Откат не выполняется только в случае, когда ошибка именно при коммите.
    В FailingCommitJdbcTransactionManager переопределен метод doCommit, в котором всегда будет исключение
     */
    @Test
    void xaTxWithFailedTxTest() {
        UserJson user = usersDbClient.createUserWithoutAtomicos(
                new UserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void springJdbcXaTxTest() {
        String username = RandomDataUtils.randomUsername();
        UserJson user = usersDbClient.createUserUsingSpringJdbcTx(
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
        System.out.println(user);
    }

    @Test
    void springJdbcWithoutTxTest() {
        String username = RandomDataUtils.randomUsername();
        UserJson user = usersDbClient.createUserUsingSpringJdbcWithoutTx(
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
        System.out.println(user);
    }

    @Test
    void simpleJdbcXaTxTest() {
        String username = RandomDataUtils.randomUsername();
        UserJson user = usersDbClient.createUserUsingJdbcTx(
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
        System.out.println(user);
    }

    @Test
    void simpleJdbcWithoutTxTest() {
        String username = RandomDataUtils.randomUsername();
        UserJson user = usersDbClient.createUserUsingJdbcWithoutTx(
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
        System.out.println(user);
    }
}
