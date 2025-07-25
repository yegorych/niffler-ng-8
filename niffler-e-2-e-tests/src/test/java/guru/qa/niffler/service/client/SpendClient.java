package guru.qa.niffler.service.client;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendClient {
    @Nonnull
    SpendJson createSpend(SpendJson spend);
    @Nonnull
    SpendJson updateSpend(SpendJson spend);
    void deleteSpend(SpendJson spendJson);
    @Nullable
    SpendJson findSpendById(UUID id);
    @Nullable
    SpendJson findSpend(SpendJson spendJson);
    @Nonnull
    CategoryJson createCategory(CategoryJson category);
    void deleteCategory(CategoryJson category);
    @Nonnull
    List<SpendJson> getSpendsForUser(String username, @Nullable CurrencyValues currency, @Nullable Date from, @Nullable Date to);
    @Nonnull
    List<CategoryJson> getAllCategories(String username, boolean excludeArchived);
}
