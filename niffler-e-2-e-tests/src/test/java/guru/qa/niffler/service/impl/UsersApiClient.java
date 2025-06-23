package guru.qa.niffler.service.impl;


import com.codeborne.selenide.Selenide;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.api.UserApi;
import guru.qa.niffler.service.client.UsersClient;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@ParametersAreNonnullByDefault
public class UsersApiClient extends RestClient implements UsersClient{
    private static final Config CFG = Config.getInstance();

    private final UserApi userApi = create(UserApi.class);

    public UsersApiClient() {
        super(CFG.userdataUrl());
    }
    private final AuthApiClient authClient = new AuthApiClient();



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
        return executeCall(userApi.getUsers(username));
    }

    @Override
    public List<UserJson> getFriends(String username) {
        return Objects.requireNonNull(executeCall(
                userApi.getFriends(username)))
                .stream()
                .filter(userJson ->
                                FriendshipStatus.FRIEND.equals(userJson.friendshipStatus()))
                .toList();
    }

    @Override
    public List<UserJson> getIncomeInvitations(String username) {
        return Objects.requireNonNull(executeCall(
                        userApi.getFriends(username)))
                .stream()
                .filter(userJson ->
                        FriendshipStatus.INVITE_RECEIVED.equals(userJson.friendshipStatus()))
                .toList();
    }

    @Override
    public List<UserJson> getOutcomeInvitations(String username) {
        return Objects.requireNonNull(executeCall(
                        userApi.getUsers(username)))
                .stream()
                .filter(userJson ->
                        FriendshipStatus.INVITE_SENT.equals(userJson.friendshipStatus()))
                .toList();
    }

    @Override
    public List<UserJson> addIncomeInvitations(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserJson> addOutcomeInvitations(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserJson> addFriends(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
