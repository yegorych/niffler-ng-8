package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static guru.qa.type.CurrencyValues.*;

public class StatGraphQlTest extends BaseGraphQlTest {

  @User
  @Test
  @ApiLogin
  void statTest(@Token String bearerToken) {
    final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
            .filterCurrency(null)
            .statCurrency(null)
            .filterPeriod(null)
            .build())
        .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
    final StatQuery.Data data = response.dataOrThrow();
    StatQuery.Stat result = data.stat;
    Assertions.assertEquals(
        0.0,
        result.total
    );
  }

    @User(spendings =
            @Spend(
                    category = "Обучение архивное",
                    description = "курсы",
                    amount = 10000,
                    currency = CurrencyValues.RUB
            ),
            categories = @Category(archived = true, name = "Обучение архивное")
    )
    @Test
    @ApiLogin
    void archiveCategoryShouldBeReturnedFromGatewayWithArchivedName(@Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(null)
                        .statCurrency(null)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        Assertions.assertEquals("Archived", data.stat.statByCategories.getFirst().categoryName);


    }


    @User(spendings = @Spend(
            category = "Обучение",
            description = "книги",
            amount = 1500,
            currency = CurrencyValues.USD))
    @Test
    @ApiLogin
    void activeCategoryShouldBeReturnedFromGateway(@Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(null)
                        .statCurrency(null)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        Assertions.assertEquals("Обучение", data.stat.statByCategories.getFirst().categoryName);
    }


    @User(spendings = {
            @Spend(category = "RUB",
                    description = "desc rub",
                    amount = 100.0,
                    currency = CurrencyValues.RUB
            ),
            @Spend(category = "USD",
                    description = "desc usd",
                    amount = 50.0,
                    currency = CurrencyValues.USD
            ),
            @Spend(category = "EUR",
                    description = "desc eur",
                    amount = 150.0,
                    currency = CurrencyValues.EUR
            ),
            @Spend(category = "KZT",
                    description = "desc kzt",
                    amount = 250.0,
                    currency = CurrencyValues.KZT
            )
    })
    @ParameterizedTest
    @ApiLogin
    @MethodSource("currencyValuesValues")
    void filteredCategoriesShouldBeReturnedFromGateway(guru.qa.type.CurrencyValues currency,  @Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(currency)
                        .statCurrency(currency)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        for (StatQuery.StatByCategory stat : data.stat.statByCategories){
            Assertions.assertEquals(currency, stat.currency);
            Assertions.assertEquals(currency.rawValue, stat.categoryName);
        }
    }

     static Stream<guru.qa.type.CurrencyValues> currencyValuesValues() {
        return Stream.of(RUB, USD, KZT, EUR);
    }




}
