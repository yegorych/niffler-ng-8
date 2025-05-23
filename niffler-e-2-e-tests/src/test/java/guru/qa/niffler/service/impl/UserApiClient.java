package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserApi;
import guru.qa.niffler.service.UsersClient;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class UserApiClient implements UsersClient {
    private static final Config CFG = Config.getInstance();
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.userdataUrl())
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final UserApi userApi = retrofit.create(UserApi.class);

    private <T> T executeCall(Call<T> call){
        final Response<T> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertTrue(response.isSuccessful());
        return response.body();
    }

    @Override
    public UserJson createUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
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
