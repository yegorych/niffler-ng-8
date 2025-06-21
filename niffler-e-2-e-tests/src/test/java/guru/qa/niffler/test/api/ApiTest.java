package guru.qa.niffler.test.api;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.client.AuthClient;
import guru.qa.niffler.service.client.UsersClient;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApiTest {
    private final AuthClient authClient = new AuthApiClient();
    private final UsersClient usersClient = new UsersApiClient();

    @Test
    void authApiCreateUserTest() {
        UserJson userJson = usersClient.createUser("duck9901", "12345");
        Assertions.assertNotNull(userJson.id());
    }

    @Test
    void authApiLoginTest() {
        String codeVerifier = authClient.preRequest();
        Assertions.assertNotNull(codeVerifier);
        String code = authClient.login("duck", "12345");
        Assertions.assertNotNull(code);
        String token = authClient.token(code, codeVerifier);
        Assertions.assertNotNull(token);
        System.out.println("Token: " + token);
    }
}
