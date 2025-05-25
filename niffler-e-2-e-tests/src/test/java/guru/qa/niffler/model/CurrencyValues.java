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

  public static CurrencyValues fromSymbol(String symbol) {
    for (CurrencyValues value : CurrencyValues.values()) {
      if (value.getSymbol().equals(symbol)) {
        return value;
      }
    }
    throw new IllegalArgumentException("Unknown currency symbol: " + symbol);
  }
}
