package guru.qa.niffler.model;

public enum CurrencyValues {
  RUB("₽"),
  USD("$"),
  EUR("€"),
  KZT("₸");

  private final String symbol;

  CurrencyValues(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }
}
