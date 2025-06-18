package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar> {
    private final SelenideElement yearPickerBtn;
    private final ElementsCollection yearPicker;
    private final SelenideElement previousMonthBtn;
    private final SelenideElement nextMonthBtn;
    private final ElementsCollection daysPicker;

  public Calendar(SelenideElement self) {
    super(self);
      yearPickerBtn = self.$("[aria-label='calendar view is open, switch to year view']");
      yearPicker = self.$$(".MuiPickersYear-yearButton");
      previousMonthBtn = self.$("[aria-label='Previous month']");
      nextMonthBtn = self.$("[aria-label='Next month']");
      daysPicker = self.$$("[role='rowgroup'] button");
  }

  public Calendar() {
    super($(".MuiPickersLayout-root"));
      yearPickerBtn = self.$("[aria-label='calendar view is open, switch to year view']");
      yearPicker = self.$$(".MuiPickersYear-yearButton");
      previousMonthBtn = self.$("[aria-label='Previous month']");
      nextMonthBtn = self.$("[aria-label='Next month']");
      daysPicker = self.$$("[role='rowgroup'] button");
  }

    @Nonnull
    @Step("Select date")
    public Calendar selectDateInCalendar(Date date) {
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        int day = localDate.getDayOfMonth();
        Month month = localDate.getMonth();
        int year = localDate.getYear();
        selectYear(year);
        selectMonth(month);
        selectDay(day);
        return this;
    }


    private void selectYear(int year) {
        yearPickerBtn.click();
        yearPicker.find(text(String.valueOf(year))).scrollIntoCenter().click();
    }


    private void selectMonth(Month month) {
        String monthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int selectedMonthNumber = getMonthNumber(getSelectedMonth());
        SelenideElement monthPickerBtn = month.getValue() > selectedMonthNumber
                ? nextMonthBtn
                : previousMonthBtn;
        while (!getSelectedMonth().equals(monthName)){
            monthPickerBtn.click();
        }
    }

    private void selectDay(int day){
        daysPicker.find(text(String.valueOf(day))).click();
    }


    private static int getMonthNumber(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }

    @Nonnull
    private String getSelectedMonth(){
        return self.$(".MuiPickersCalendarHeader-label").text().split(" ")[0];
    }
}
