package guru.qa.niffler.service.impl;

import guru.qa.niffler.data.entity.currency.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import guru.qa.niffler.data.repository.impl.CurrencyRepositoryHibernate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.CurrencyJson;
import guru.qa.niffler.service.client.CurrencyClient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CurrencyDbClient implements CurrencyClient {
    CurrencyRepository currencyRepository = new CurrencyRepositoryHibernate();
    @NotNull
    @Override
    public CurrencyJson findCurrencyByValue(CurrencyValues currencyValue) {
        CurrencyEntity currencyEntity = currencyRepository.findByCurrency(currencyValue).orElseThrow();
        return  CurrencyJson.fromEntity(currencyEntity);
    }

    @NotNull
    @Override
    public List<CurrencyJson> findAll() {
       return currencyRepository.findAllCurrencies()
               .stream()
               .map(CurrencyJson::fromEntity)
               .toList();
    }
}
