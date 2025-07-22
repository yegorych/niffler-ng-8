package guru.qa.niffler.utils;


import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.service.client.CurrencyClient;
import guru.qa.niffler.service.impl.CurrencyDbClient;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {
    private final  CurrencyClient currencyClient = new CurrencyDbClient();
    @Nonnull
    public double convertSpendTo(double spend,
                              @Nonnull CurrencyValues spendCurrency,
                              @Nonnull CurrencyValues desiredCurrency) {
        BigDecimal spendInUsd = spendCurrency == CurrencyValues.USD
                ? BigDecimal.valueOf(spend)
                : BigDecimal.valueOf(spend).multiply(courseForCurrency(spendCurrency));

        return spendInUsd.divide(
                courseForCurrency(desiredCurrency),
                2,
                RoundingMode.HALF_UP
        ).doubleValue();
    }

    @Nonnull
    private BigDecimal courseForCurrency(@Nonnull CurrencyValues currency) {
        return BigDecimal.valueOf(currencyClient.findCurrencyByValue(currency).currencyRate());
    }
}
