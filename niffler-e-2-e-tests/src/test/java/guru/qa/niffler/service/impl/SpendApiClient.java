package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

public class SpendApiClient implements SpendClient {
  private static final Config CFG = Config.getInstance();
  private final OkHttpClient client = new OkHttpClient.Builder().build();
  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .client(client)
      .addConverterFactory(JacksonConverterFactory.create())
      .build();
  private final SpendApi spendApi = retrofit.create(SpendApi.class);

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
    public SpendJson createSpend(SpendJson spend) {
        if (spend.category().id() == null) {
            createCategory(spend.category());
        }
        return executeCall(spendApi.addSpend(spend));
    }

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

    @Override
    public SpendJson findSpend(SpendJson spendJson) {
        return executeCall(spendApi.getSpend(spendJson.id().toString(), spendJson.username()));
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return executeCall(spendApi.addCategory(category));
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
