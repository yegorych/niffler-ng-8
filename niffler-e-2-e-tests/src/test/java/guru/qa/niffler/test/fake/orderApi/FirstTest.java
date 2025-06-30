package guru.qa.niffler.test.fake.orderApi;

import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.client.UsersClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

@Order(1)
public class FirstTest {
    @Test
    void firstTest() {
        UsersClient usersClient = new UsersApiClient();
        List<UserJson> list = usersClient.allUsersWithout("");
        Assertions.assertTrue(list.isEmpty());
    }
}
