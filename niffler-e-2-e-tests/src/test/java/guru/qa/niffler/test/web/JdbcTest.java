package guru.qa.niffler.test.web;

import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.*;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static guru.qa.niffler.utils.RandomDataUtils.*;

public class JdbcTest {



    @ParameterizedTest
    @MethodSource("spendRepositoryProvider")
    void spendDbClientTest(SpendRepository repository) {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                randomCategoryName(),
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        randomSentence(2),
                        "duck"
                )
        );
        Assertions.assertNotNull(spendDbClient.findSpendById(spend.id()));

        SpendJson newSpend = new SpendJson(
                spend.id(),
                new Date(),
                spend.category(),
                CurrencyValues.USD,
                800.0,
                "desc",
                spend.username());

        spendDbClient.updateSpend(newSpend);
        Assertions.assertEquals("desc", spendDbClient.findSpendById(spend.id()).description());

        spendDbClient.deleteSpend(newSpend);
        Assertions.assertNull(spendDbClient.findSpendById(newSpend.id()));
    }

    static Stream<SpendRepository> spendRepositoryProvider() {
        return Stream.of(
                new SpendRepositoryHibernate(),
                new SpendRepositoryJdbc(),
                new SpendRepositorySpringJdbc()
        );
    }

    @ParameterizedTest
    @MethodSource("userdataAndAuthRepositoryProvider")
    void usersDbClientTest(UserdataUserRepository udRepository, AuthUserRepository authRepository) {
        UsersDbClient usersDbClient = new UsersDbClient();
        String username = randomUsername();
        UserJson user = usersDbClient.createUser(username, "12345");
        usersDbClient.createFriends(user, 1);
        usersDbClient.createIncomeInvitations(user, 2);
        usersDbClient.createOutcomeInvitations(user, 3);
    }

    static Stream<Arguments> userdataAndAuthRepositoryProvider() {
        return Stream.of(
                Arguments.of(new UserdataUserRepositoryHibernate(), new AuthUserRepositoryHibernate()),
                Arguments.of(new UserdataUserRepositoryJdbc(), new AuthUserRepositoryJdbc()),
                Arguments.of(new UserdataUserRepositorySpringJdbc(), new AuthUserRepositorySpringJdbc())
        );
    }


}
