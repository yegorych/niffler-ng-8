package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.util.List;
import retrofit2.Call;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Date;

@ParametersAreNonnullByDefault
public class SpendApiClient {
  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();
  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @Nullable
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

  public SpendJson addSpend(SpendJson spend) {
      return executeCall(spendApi.addSpend(spend));
  }

  public SpendJson editSpend(SpendJson spend) {
      return executeCall(spendApi.editSpend(spend));
  }

  public SpendJson getSpend(String id, String username) {
      return executeCall(spendApi.getSpend(id, username));
  }

  public List<SpendJson> getAllSpends(String username,
                                      @Nullable CurrencyValues currency,
                                      @Nullable Date from,
                                      @Nullable Date to) {
      return executeCall(spendApi.getAllSpends(username, currency, from, to));
  }

  public void removeSpend(String username, List<String> id) {
      executeCall(spendApi.removeSpend(username, id));
  }

  public List<CategoryJson> getAllCategories(String username, boolean excludeArchived) {
      return executeCall(spendApi.getAllCategories(username, excludeArchived));
  }
  public CategoryJson addCategory(CategoryJson category) {
      return executeCall(spendApi.addCategory(category));
  }

  public CategoryJson updateCategory(CategoryJson category) {
      return executeCall(spendApi.updateCategory(category));
  }


}
