package guru.qa.niffler.service;

import retrofit2.Call;
import retrofit2.http.*;

public interface AuthApi {
    @GET("register")
    Call<Void> register();

    @FormUrlEncoded
    @POST("register")
    Call<Void> register(@Field("username") String username,
                        @Field("password") String password,
                        @Field("passwordSubmit") String passwordSubmit,
                        @Field("_csrf") String cookie);

    @GET(("oauth2/authorize"))
    Call<Void> authorize(@Query("response_type") String responseType,
                         @Query("client_id") String clientId,
                         @Query("scope") String scope,
                         @Query(value = "redirect_uri", encoded = true) String redirectUri,
                         @Query("code_challenge") String codeChallenge,
                         @Query("code_challenge_method") String codeChallengeMethod);

    @FormUrlEncoded
    @POST("login")
    Call<String> login(@Field("username") String username,
                     @Field("password") String password,
                     @Field("_csrf") String cookie);

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<String> token(@Field("code") String code,
                       @Field("redirect_uri") String redirectUri,
                       @Field("code_verifier") String codeVerifier,
                       @Field("grant_type") String grantType,
                       @Field("client_id") String clientId);

}
