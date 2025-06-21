package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.service.AuthApi;
import guru.qa.niffler.service.client.AuthClient;
import guru.qa.niffler.utils.OauthUtils;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient implements AuthClient {

    public AuthApiClient() {
        super(CFG.authUrl(), true, ScalarsConverterFactory.create(), HttpLoggingInterceptor.Level.BODY);
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



    @SneakyThrows
    @Override
    public String preRequest(){
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);
        executeCall(authApi.authorize(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "authorized",
                codeChallenge,
                "S256")
        );
        return codeVerifier;
    }

    @Override
    public String login(String username, String password) {
        String location = Objects.requireNonNull(
                executeCallFullResponse(
                        authApi.login(
                                username,
                                password,
                                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
                        )
                ).raw()
                 .priorResponse())
                 .header("Location");

        if (StringUtils.isNotEmpty(location) && location.contains("code")) {
            return Objects.requireNonNull(HttpUrl.parse(location)).queryParameter("code");
        } else {
            throw new IllegalArgumentException("Location does not contain query 'code'");
        }
    }

    @SneakyThrows
    @Override
    public String token(String code, String codeVerifier) {
        String body = executeCall(
                authApi.token(
                        code,
                        CFG.frontUrl() + "authorized",
                        codeVerifier,
                        "authorization_code",
                        "client"
                ));
        Assertions.assertNotNull(body);
        return new ObjectMapper()
                .readTree(body.getBytes(StandardCharsets.UTF_8))
                .get("id_token")
                .asText();
    }
}
