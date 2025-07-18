package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.*;
import guru.qa.niffler.utils.CurrencyConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class CurrencyGrpcTest extends BaseGrpcTest{
    private final CurrencyConverter converter = new CurrencyConverter();

    @Test
    void allCurrenciesShouldReturned() {
        final CurrencyResponse response = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
        final List<Currency> allCurrenciesList = response.getAllCurrenciesList();
        Assertions.assertEquals(4, allCurrenciesList.size());
    }

    @ParameterizedTest
    @MethodSource("allOrderedPairs")
    void correctCalculatedAmountShouldBeReturnedForAllCurrencies(CurrencyValues spendCurrency, CurrencyValues desiredCurrency) {
        double amount = ThreadLocalRandom.current().nextDouble(0.0, 10000.0);
        final double expectedCalculatedAmount = converter.convertSpendTo(amount,
                guru.qa.niffler.model.CurrencyValues.fromCurrencyGrpc(spendCurrency),
                guru.qa.niffler.model.CurrencyValues.fromCurrencyGrpc(desiredCurrency)
        );
        final double calculatedAmount = blockingStub.calculateRate(
                CalculateRequest.newBuilder()
                        .setAmount(amount)
                        .setSpendCurrency(spendCurrency)
                        .setDesiredCurrency(desiredCurrency)
                        .build())
                .getCalculatedAmount();
        Assertions.assertEquals(expectedCalculatedAmount, calculatedAmount);
    }

    static Stream<Arguments> allOrderedPairs() {
        CurrencyValues[] values = Arrays.stream(CurrencyValues.values())
                .filter(value -> !value.equals(CurrencyValues.UNRECOGNIZED) && !value.equals(CurrencyValues.UNSPECIFIED) )
                .toArray(CurrencyValues[]::new);
        List<Arguments> result = new ArrayList<>();
        for (CurrencyValues first : values) {
            for (CurrencyValues second : values) {
                if (first != second) {
                    result.add(Arguments.of(first, second));
                }
            }
        }
        return result.stream();
    }


}
