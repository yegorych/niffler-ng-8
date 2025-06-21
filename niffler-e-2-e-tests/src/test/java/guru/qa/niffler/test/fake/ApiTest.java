package guru.qa.niffler.test.fake;

import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.client.UsersClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApiTest {
    @Test
    void authApiTest() {
        UsersClient usersClient = new UsersApiClient();
        UserJson userJson = usersClient.createUser("duck99", "12345");
        Assertions.assertNotNull(userJson.id());
    }
}
