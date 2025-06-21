package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class SpendConditions {
    //public record SpendFront(String category, Double amount, CurrencyValues currency, String description, String date) {}
    public record SpendFront(String category, Double amount, CurrencyValues currency, String description, Date date) {}

    @Nonnull
    public static WebElementsCondition spend(SpendJson... spends){
        return new WebElementsCondition() {

            final List<SpendFront> expectedSpendsFrontList = Arrays.stream(spends).map(SpendJson::toSpendFrontend).toList();

            @NotNull
            @Override
            public CheckResult check(@NotNull Driver driver, @NotNull List<WebElement> elements) {

                if (ArrayUtils.isEmpty(spends) && !elements.isEmpty()){
                    throw new IllegalArgumentException("No expected spends given");
                }

                if (spends.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", spends.length, elements.size());
                    throw new AssertionError(message);
                }

                List<SpendFront> actualSpendsFrontList = elements.stream().map(el -> {
                    ElementsCollection spend = $$(el.findElements(By.tagName("td")));
                    String categoryName = spend.get(1).getText();
                    String[] amountWithCurrency = spend.get(2).getText().split(" ");
                    Double amount = Double.parseDouble(amountWithCurrency[0]);
                    CurrencyValues currency = CurrencyValues.fromSymbol(amountWithCurrency[1]);
                    String description = spend.get(3).getText();
                    Date date = convertToDate(spend.get(4).getText());
                    return new SpendFront(categoryName, amount, currency, description, date);
                }).toList();

                boolean passed = true;
                StringBuilder stringBuilder = new StringBuilder("\n");
                for (int i=0; i<expectedSpendsFrontList.size(); i++) {
                    SpendFront actualSpend = actualSpendsFrontList.get(i);
                    SpendFront expectedSpend = expectedSpendsFrontList.get(i);

                    if (!actualSpend.category.equals(expectedSpend.category)) {
                        stringBuilder.append(
                                String.format(
                                        "Spend category mismatch (expected: %s, actual: %s)\n",
                                        expectedSpend.category,
                                        actualSpend.category
                                )
                        );
                    }
                    if (!actualSpend.date.equals(expectedSpend.date)) {
                        stringBuilder.append(
                                String.format(
                                        "Spend date mismatch (expected: %s, actual: %s)\n",
                                        expectedSpend.date,
                                        actualSpend.date
                                )
                        );
                    }
                    if (!actualSpend.currency.equals(expectedSpend.currency)) {
                        stringBuilder.append(
                                String.format(
                                        "Spend currency mismatch (expected: %s, actual: %s)\n",
                                        expectedSpend.currency,
                                        actualSpend.currency
                                )
                        );
                    }
                    if (!actualSpend.currency.equals(expectedSpend.currency)) {
                        stringBuilder.append(
                                String.format(
                                        "Spend currency mismatch (expected: %s, actual: %s)\n",
                                        expectedSpend.currency,
                                        actualSpend.currency
                                )
                        );
                    }
                    if (!actualSpend.amount.equals(expectedSpend.amount)) {
                        stringBuilder.append(
                                String.format(
                                        "Spend amount mismatch (expected: %s, actual: %s)\n",
                                        expectedSpend.amount,
                                        actualSpend.amount
                                )
                        );
                    }
                    if (!actualSpend.description.equals(expectedSpend.description)) {
                        stringBuilder.append(
                                String.format(
                                        "Spend description mismatch (expected: %s, actual: %s)\n",
                                        expectedSpend.description,
                                        actualSpend.description
                                )
                        );
                    }
                }

                if (stringBuilder.toString().length() > 1){
                    return Assertions.fail(stringBuilder.toString());
                }
                return accepted();
            }

            @NotNull
            @Override
            public String toString() {
                return expectedSpendsFrontList.toString();
            }

            @Nonnull
            private Date convertToDate(String dateStr) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
                LocalDate localDate = LocalDate.parse(dateStr, formatter);
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        };
    }
}
