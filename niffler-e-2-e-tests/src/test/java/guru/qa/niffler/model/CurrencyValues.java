package guru.qa.niffler.model;

import java.util.Arrays;

public enum CurrencyValues {
  RUB("₽"),
  USD("$"),
  EUR("€"),
  KZT("₸");

  private final String symbol;

  public static CurrencyValues fromCurrencyGrpc(guru.qa.niffler.grpc.CurrencyValues currencyGrpc){
      return Arrays.stream(CurrencyValues.values())
              .filter(c -> c.name().equals(currencyGrpc.name()))
              .findFirst()
              .orElseThrow();
  }

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
