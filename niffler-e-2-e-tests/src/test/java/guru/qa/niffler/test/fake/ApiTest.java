package guru.qa.niffler.test.fake;


import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.client.UsersClient;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.apache.kafka.common.security.auth.Login;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApiTest {
    private final AuthApiClient authClient = new AuthApiClient();
    private final UsersClient usersClient = new UsersApiClient();

    @Test
    void authApiCreateUserTest() {
        UserJson userJson = usersClient.createUser("duck9901", "12345");
        Assertions.assertNotNull(userJson.id());
    }

    @Test
    void authApiLoginTest() {
        String token = authClient.login("duck9901", "12345");
        System.out.println("Token: " + token);
    }

    @Test
    @ApiLogin(username = "duck", password = "12345")
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        Assertions.assertNotNull(user.id());

    }
}
