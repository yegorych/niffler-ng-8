package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.condition.SpendConditions.SpendFront;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public record SpendJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("spendDate")
    Date spendDate,
    @JsonProperty("category")
    CategoryJson category,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("amount")
    Double amount,
    @JsonProperty("description")
    String description,
    @JsonProperty("username")
    String username) {

    public static SpendJson fromEntity(SpendEntity entity) {
        final CategoryEntity category = entity.getCategory();
        final String username = entity.getUsername();

        return new SpendJson(
            entity.getId(),
            entity.getSpendDate(),
            new CategoryJson(
                category.getId(),
                category.getName(),
                username,
                category.isArchived()
            ),
            entity.getCurrency(),
            entity.getAmount(),
            entity.getDescription(),
            username
        );
    }

    public String getStatBubbleText(){
        String categoryName = category.archived() ? "Archived" : category().name();
        String amountStr = amount() % 1 == 0
                ? String.format("%.0f", amount())
                : new DecimalFormat("#.##").format(amount());;
        String currencyStr = currency().getSymbol();
        return String.join(" ", categoryName, amountStr, currencyStr);
    }

    private String toFrontendDateFormat(Date date){
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        int day = localDate.getDayOfMonth();
        String monthName = localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = localDate.getYear();
        return monthName + " " + day + ", " + year;
    }

    public SpendFront toSpendFrontend(){
        return new SpendFront(
                category.name(),
                amount,
                currency,
                description,
                Date.from(
                        spendDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                )
                //toFrontendDateFormat(spendDate)
        );
    }


}
