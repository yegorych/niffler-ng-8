package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.client.SpendClient;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;


@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient implements SpendClient{
  private static final Config CFG = Config.getInstance();
//  private final OkHttpClient client = new OkHttpClient.Builder()
//          .addNetworkInterceptor(
//                  new CustomAllureOkHttpInterceptor()
//                          .setRequestTemplate("http-request.ftl")
//                          .setResponseTemplate("http-response.ftl")
//          ).build();
//
//  private final Retrofit retrofit = new Retrofit.Builder()
//      .baseUrl(CFG.spendUrl())
//      .client(client)
//      .addConverterFactory(JacksonConverterFactory.create())
//      .build();
  private final SpendApi spendApi = create(SpendApi.class);

    public SpendApiClient(String baseUrl) {
        super(baseUrl);
    }


//    private <T> T executeCall(Call<T> call){
//      final Response<T> response;
//      try {
//          response = call.execute();
//      } catch (IOException e) {
//          throw new AssertionError(e);
//      }
//      Assertions.assertTrue(response.isSuccessful());
//      return response.body();
//  }

    @NotNull
    @Override
    public SpendJson createSpend(SpendJson spend) {
        if (spend.category().id() == null) {
            createCategory(spend.category());
        }
        return executeCall(spendApi.addSpend(spend));
    }

    @NotNull
    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return executeCall(spendApi.editSpend(spend));
    }

    @Override
    public void deleteSpend(SpendJson spendJson) {
        executeCall(spendApi.removeSpend(spendJson.username(), Collections.singletonList(spendJson.id().toString())));
    }

    @Override
    public SpendJson findSpendById(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Nullable
    @Override
    public SpendJson findSpend(SpendJson spendJson) {
        return executeCall(spendApi.getSpend(spendJson.id().toString(), spendJson.username()));
    }

    @NotNull
    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return executeCall(spendApi.addCategory(category));
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
