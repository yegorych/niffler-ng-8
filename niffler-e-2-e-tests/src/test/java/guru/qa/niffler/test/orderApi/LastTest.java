package guru.qa.niffler.test.orderApi;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.client.UsersClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.util.List;

@Isolated
@Order(Integer.MAX_VALUE)
public class LastTest {
    @Test
    void lastTest() {
        UsersClient usersClient = new UsersApiClient();
        List<UserJson> list = usersClient.allUsersWithout("");
        Assertions.assertFalse(list.isEmpty());
    }
}
