package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.service.AuthApi;
import guru.qa.niffler.service.client.AuthClient;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient implements AuthClient {
    public AuthApiClient() {
        super(CFG.authUrl());
    }
    private final AuthApi authApi = create(AuthApi.class);

    @Override
    public void requestRegisterForm() {
        executeCall(authApi.register());
    }

    @Override
    public void register(String username, String password) {
        executeCall(authApi.register(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")));
    }
}
