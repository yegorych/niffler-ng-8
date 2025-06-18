package guru.qa.niffler.service;

import guru.qa.niffler.model.SpendJson;
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

}
