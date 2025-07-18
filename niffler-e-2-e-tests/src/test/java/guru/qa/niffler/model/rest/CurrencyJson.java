package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.currency.CurrencyEntity;
import guru.qa.niffler.model.CurrencyValues;

public record CurrencyJson(@JsonProperty("currency")
                           CurrencyValues currency,
                           @JsonProperty("currencyRate")
                           Double currencyRate) {

    public static CurrencyJson fromEntity(CurrencyEntity currencyEntity) {
        return new CurrencyJson(currencyEntity.getCurrency(), currencyEntity.getCurrencyRate());
    }

}
