package guru.qa.niffler.api;

import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(@Path("id") String id, @Query("username") String username);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getAllSpends(@Query("username") String username,
                                     @Query("filterCurrency") CurrencyValues currency,
                                     @Query("from") Date from,
                                     @Query("to") Date to);

  @DELETE("internal/spends/remove")
  Call<Void> removeSpend(@Query("username") String username,
                              @Query("ids") List<String> ids);

  @GET("/internal/categories/all")
  Call<List<CategoryJson>> getAllCategories(@Query("username") String username,
                                         @Query("excludeArchived") boolean excludeArchived);

  @POST("/internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("/internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);

}
