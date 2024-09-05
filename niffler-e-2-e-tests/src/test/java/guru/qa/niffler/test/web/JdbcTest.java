package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-2",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        null
                )
        );

        System.out.println(spend);
    }

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

//    @Test
//    void successfulXaTransactionTest() {
//        UsersDbClient userDbClient = new UsersDbClient();
//        String username = RandomDataUtils.randomUsername();
//        userDbClient.createUserAuthAndUserdata(
//                new AuthUserJson(
//                        null,
//                        username,
//                        "12345",
//                        true,
//                        true,
//                        true,
//                        true
//                ),
//                new UserJson(
//                        null,
//                        username,
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null
//                )
//        );
//    }

  @Test
  void springJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUser(
        new UserJson(
            null,
            "valentin-4",
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
