package guru.qa.niffler.service.impl;


import com.codeborne.selenide.Selenide;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserApi;
import guru.qa.niffler.service.client.AuthClient;
import guru.qa.niffler.service.client.UsersClient;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ParametersAreNonnullByDefault
public class UsersApiClient extends RestClient implements UsersClient{
    private static final Config CFG = Config.getInstance();

    private final UserApi userApi = create(UserApi.class);

    public UsersApiClient() {
        super(CFG.userdataUrl());
    }
    private final AuthClient authClient = new AuthApiClient();



    @NotNull
    @Override
    public UserJson createUser(String username, String password) {
        authClient.requestRegisterForm();
        authClient.register(username, password);
        StopWatch sw = StopWatch.createStarted();
        while (sw.getTime(TimeUnit.SECONDS) < 3){
            UserJson userJson = executeCall(userApi.current(username));
            if (userJson != null) {
                return userJson.withPassword(password);
            } else Selenide.sleep(100);
        }
        throw new IllegalStateException("Could not register user");
    }

    @Override
    public List<UserJson> allUsersWithout(String username) {
        return executeCall(userApi.all(username));
    }

    @Override
    public void createIncomeInvitations(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createOutcomeInvitations(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createFriends(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
