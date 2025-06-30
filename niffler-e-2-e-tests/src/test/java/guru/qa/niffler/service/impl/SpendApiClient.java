package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.service.client.SpendClient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;


@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient implements SpendClient{
  private final SpendApi spendApi = create(SpendApi.class);

  public SpendApiClient() {
      super(CFG.spendUrl());
  }

    @NotNull
    @Override
    public SpendJson createSpend(SpendJson spend) {
        if (spend.category().id() == null) {
            createCategory(spend.category());
        }
        return Objects.requireNonNull(executeCall(spendApi.addSpend(spend)));
    }

    @NotNull
    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return Objects.requireNonNull(executeCall(spendApi.editSpend(spend)));
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
        return Objects.requireNonNull(executeCall(spendApi.addCategory(category)));
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @NotNull
    @Override
    public List<SpendJson> getSpendsForUser(String username,
                                            @Nullable CurrencyValues currency,
                                            @Nullable Date from,
                                            @Nullable Date to) {
      return Objects.requireNonNull(
              executeCall(spendApi.getSpends(username, currency, from, to))
      );
    }

    @NotNull
    @Override
    public List<CategoryJson> getAllCategories(String username, boolean excludeArchived) {
        return Objects.requireNonNull(executeCall(spendApi.getCategories(username, excludeArchived)));
    }


}
