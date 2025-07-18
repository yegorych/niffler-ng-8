package guru.qa.niffler.service.client;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.CurrencyJson;
import guru.qa.niffler.model.rest.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface CurrencyClient {
    @Nonnull
    CurrencyJson findCurrencyByValue(CurrencyValues currencyValue);
    @Nonnull
    List<CurrencyJson> findAll();
}
