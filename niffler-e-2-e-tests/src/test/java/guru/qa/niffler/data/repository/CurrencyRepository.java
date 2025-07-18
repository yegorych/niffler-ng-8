package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.currency.CurrencyEntity;
import guru.qa.niffler.model.CurrencyValues;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface CurrencyRepository {
    @Nonnull
    Optional<CurrencyEntity> findByCurrency(@Nonnull CurrencyValues currency);

    @Nonnull
    List<CurrencyEntity> findAllCurrencies();
}
